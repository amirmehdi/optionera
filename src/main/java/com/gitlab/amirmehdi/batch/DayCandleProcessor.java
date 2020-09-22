package com.gitlab.amirmehdi.batch;

import com.gitlab.amirmehdi.batch.model.DayCandleBatchItem;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.domain.InstrumentHistory;
import com.gitlab.amirmehdi.util.DateUtil;
import com.gitlab.amirmehdi.util.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * author : Amirmehdi Naghavi
 * 5/31/2019
 */
public class DayCandleProcessor implements ItemProcessor<Instrument, DayCandleBatchItem> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public DayCandleBatchItem process(Instrument item) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String url = "http://members.tsetmc.com/tsev2/data/InstTradeHistory.aspx?a=0&top=9999999&i=" + item.getTseId();
        DayCandleBatchItem historyBatchItem = new DayCandleBatchItem();
        historyBatchItem.setIsin(item.getIsin());

        StopWatch stopWatch = new StopWatch("get candle for isin: " + item.getIsin());
        stopWatch.start("load data");
        ResponseEntity<byte[]> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(url, byte[].class);
        } catch (Exception e) {
            logger.error("get candle of instrument : {} get error", item.getIsin(), e);
            return historyBatchItem;
        }
        BufferedReader in = ZipUtil.getBufferedReader(responseEntity.getBody());
        stopWatch.stop();
        stopWatch.start("parse data");
        ArrayList<InstrumentHistory> candles = new ArrayList<>();
        String readed;
        while ((readed = in.readLine()) != null) {
            String[] ohlc = readed.split(";");
            for (String s : ohlc) {
                String[] values = s.split("@");
                LocalDate date2 = DateUtil.convertToLocalDateViaInstant(simpleDateFormat.parse(values[0]));
                candles.add(new InstrumentHistory(item.getIsin(),date2, Double.valueOf(values[4]).intValue()
                    , Double.valueOf(values[3]).intValue(), Double.valueOf(values[5]).intValue()
                    , Double.valueOf(values[6]).intValue(), Double.valueOf(values[2]).intValue()
                    , Double.valueOf(values[1]).intValue(),  Double.valueOf(values[9]).intValue()
                    , Double.valueOf(values[8]).longValue(), Double.valueOf(values[7]).longValue()));
            }
        }
        historyBatchItem.setMinTickDataList(candles);
        stopWatch.stop();
        logger.debug(stopWatch.prettyPrint());
        return historyBatchItem;
    }
}
