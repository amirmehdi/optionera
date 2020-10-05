package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.OptionStats;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.util.MarketTimeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class StrategyService {
    private final TelegramMessageSender telegramMessageSender;
    private final OptionRepository optionRepository;
    private final OptionStatsService optionStatsService;
    private String chatId = "-1001318208609";

    @Value("${application.market-time-check}")
    private boolean marketTimeCheck;

    public StrategyService(TelegramMessageSender telegramMessageSender, OptionRepository optionRepository, OptionStatsService optionStatsService) {
        this.telegramMessageSender = telegramMessageSender;
        this.optionRepository = optionRepository;
        this.optionStatsService = optionStatsService;
    }

    public void getArbitrageBetweenAssetAndOption() {
        Option option = optionRepository.findAllByExpDateGreaterThanEqual(LocalDate.now(), PageRequest.of(0, 1, Sort.by(Sort.Order.asc("callBreakEven")))).getContent().get(0);
        if (option.getCallBreakEven() > 0) {
            return;
        }
        String text = getMessageTemplate(option, "آربیتراژ روی سهم و اختیار خرید به قصد اعمال اختیار", "کم");
        telegramMessageSender.sendMessage(chatId, text);
    }

    public void getOptionWithPriceLowerThanBS() {
        Option option = optionRepository.findAllByExpDateGreaterThanEqual(LocalDate.now(), PageRequest.of(0, 1, Sort.by(Sort.Order.asc("callAskToBS")))).getContent().get(0);
        if (option.getCallAskToBS() > 0) {
            return;
        }
        String text = getMessageTemplate(option, "اختیار با قیمتی کمتر از قیمت تئوری قابل خرید است", "متوسط");
        telegramMessageSender.sendMessage(chatId, text);
    }

    private String getMessageTemplate(Option option, String strategy, String risk) {
        String s = "\uD83C\uDF10نماد: %s\n" +
            "\uD83D\uDCDDدارایی پایه: %s\n" +
            "\uD83D\uDDD3تاریخ: %s\n" +
            "⏰ساعت:%s\n" +
            "\uD83D\uDCB0قیمت بهترین عرضه: %s\n" +
            "\uD83D\uDD06تعداد عرضه: %s\n" +
            "\uD83D\uDCB2بلک شولز: %s\n" +
            "〽️سر به سری: %s\n" +
            "\uD83D\uDCB5استراتژی: %s\n" +
            " \uD83D\uDEA8ریسک:%s";

        OptionStats optionStats = optionStatsService.findOne(option.getId()).orElseThrow(RuntimeException::new);
        String text = String.format(s, "ض" + option.getName()
            , option.getInstrument().getName()
            , LocalDate.now().toString()
            , LocalTime.now()
            , optionStats.getCallBidAsk().getAskPrice()
            , optionStats.getCallBidAsk().getAskQuantity()
            , optionStats.getCallBlackScholes30()
            , optionStats.getCallBreakEven()
            , strategy
            , risk);
        text = text.replace("\n", "%0A");
        return text;
    }

    @Scheduled(cron = "0 0,20,40 9-12 * * *")
    public void runStrategies() {
        if (marketTimeCheck && !MarketTimeUtil.isMarketOpen())
            return;
        run();
    }

    public void run() {
        getArbitrageBetweenAssetAndOption();
        getOptionWithPriceLowerThanBS();
    }
}
