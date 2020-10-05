package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.domain.OptionStats;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public abstract class Strategy {
    protected final OptionRepository optionRepository;
    protected final OptionStatsService optionStatsService;

    protected final String optionEraChatId = "-1001318208609";
    private final String apiToken = "1154072624:AAG1HWOZDAU4FxgP0aek84zt7Ap4mfe4wJo";


    protected Strategy(OptionRepository optionRepository, OptionStatsService optionStatsService) {
        this.optionRepository = optionRepository;
        this.optionStatsService = optionStatsService;
    }

    public abstract List<TelegramMessageDto> getSignals();

    public String getCron() {
        return null;
    }

    protected String getMessageTemplate(OptionStats optionStats, String strategy, String risk) {
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

        return String.format(s, "ض" + optionStats.getOption().getName()
            , optionStats.getOption().getInstrument().getName()
            , LocalDate.now().toString()
            , LocalTime.now()
            , optionStats.getCallBidAsk().getAskPrice()
            , optionStats.getCallBidAsk().getAskQuantity()
            , optionStats.getCallBlackScholes30()
            , optionStats.getCallBreakEven()
            , strategy
            , risk);
    }


    protected TelegramMessageDto getTelegramMessageDto(String text) {
        return new TelegramMessageDto(apiToken, optionEraChatId, text);
    }
}
