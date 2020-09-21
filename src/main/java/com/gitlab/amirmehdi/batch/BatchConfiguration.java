package com.gitlab.amirmehdi.batch;

import com.gitlab.amirmehdi.batch.model.DayCandleBatchItem;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.domain.InstrumentHistory;
import com.gitlab.amirmehdi.repository.InstrumentHistoryRepository;
import com.gitlab.amirmehdi.repository.InstrumentRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

@Configuration
@EnableBatchProcessing
@Log4j2
public class BatchConfiguration implements CommandLineRunner {
    private final JobRegistry jobRegistry;
    private final JobLauncher jobLauncher;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobCompletionNotificationListener listener;

    private final InstrumentRepository instrumentRepository;
    private final InstrumentHistoryRepository instrumentHistoryRepository;

    public BatchConfiguration(JobRegistry jobRegistry, JobLauncher jobLauncher, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, JobCompletionNotificationListener listener, InstrumentRepository instrumentRepository, InstrumentHistoryRepository instrumentHistoryRepository) {
        this.jobRegistry = jobRegistry;
        this.jobLauncher = jobLauncher;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.listener = listener;
        this.instrumentRepository = instrumentRepository;
        this.instrumentHistoryRepository = instrumentHistoryRepository;
    }


    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring batch");
    }


    @Bean
    ItemReader<Instrument> indexItemReader() {
        RepositoryItemReader<Instrument> reader = new RepositoryItemReader<>();
        reader.setRepository(instrumentRepository);
        reader.setMethodName("findAll");
        reader.setSort(Collections.singletonMap("isin", Sort.Direction.ASC));
        return reader;
    }

    @Bean
    public DayCandleProcessor candleProcessor() {
        return new DayCandleProcessor();
    }

    @Bean
    public ItemWriter<DayCandleBatchItem> candleItemWriter() {
        return batchItems -> {
            for (DayCandleBatchItem batchItem : batchItems) {
                log.info("batch save for isin:{} day candles size:{} ", batchItem.getIsin(), batchItem.getMinTickDataList() == null ? 0 : batchItem.getMinTickDataList().size());
                if (batchItem.getMinTickDataList() != null && !batchItem.getMinTickDataList().isEmpty()) {
                    instrumentHistoryRepository.saveAll(batchItem.getMinTickDataList().subList(0, 200));
                }
            }
        };
    }

    @Bean
    @Qualifier("dayCandleStep")
    public Step dayCandleStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("getDayCandles")
            .<Instrument, DayCandleBatchItem>chunk(100)
            .reader(indexItemReader())
            .processor(candleProcessor())
            .writer(candleItemWriter())
            .taskExecutor(taskExecutor())
            .build();
    }

    @Bean
    public Job getMarketData() {
        return jobBuilderFactory.get(JOBS.DAY_CANDLE.toString())
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(dayCandleStep(stepBuilderFactory))
            .end()
            .build();
    }

    public void runJobs(String[] jobs) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobException {
        for (String job : jobs) {
            JobParameters jobParameters = new JobParametersBuilder()
//                .addString("funds", funds)
                .toJobParameters();
            JobExecution jobExecution = jobLauncher.run(jobRegistry.getJob(job), jobParameters);
            log.info("!!! JOB {} {}! Time to verify the results", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus().toString());
        }
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void runJobs() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobException {
        runJobs(new String[]{JOBS.DAY_CANDLE.toString()});
        List<Instrument> instruments = instrumentRepository.findAll().parallelStream().peek(instrument -> {
            List<InstrumentHistory> history = instrumentHistoryRepository.findAllByIsin(instrument.getIsin(), PageRequest.of(0, 128, Sort.by(Sort.Order.desc("date"))));
            List<Double> logReturn = history.stream().mapToDouble(value -> 1.0 * value.getClose() / value.getReferencePrice()).boxed().collect(Collectors.toList());
            List<Double> logReturn2 = logReturn.stream().mapToDouble(value -> pow(value, 2)).boxed().collect(Collectors.toList());
            instrument.setVolatility30(calcVolatility(22, logReturn, logReturn2));
            instrument.setVolatility60(calcVolatility(66, logReturn, logReturn2));
            instrument.setVolatility90(calcVolatility(128, logReturn, logReturn2));
        }).collect(Collectors.toList());
        instrumentRepository.saveAll(instruments);
    }

    private double calcVolatility(int n, List<Double> logReturn, List<Double> logReturn2) {
        double var = (logReturn2.stream().limit(n).mapToDouble(Double::doubleValue).sum()) / (n - 1)
            - pow(logReturn.stream().limit(n).mapToDouble(Double::doubleValue).sum(), 2) / (n * (n - 1));
        double volatility = sqrt(var) * sqrt(240);
        return volatility > 1 ? 0.6 : volatility;
    }

    @Override
    public void run(String... args) throws Exception {
        runJobs();
    }

    public enum JOBS {
        DAY_CANDLE
    }
}
