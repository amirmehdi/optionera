package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.domain.OptionStats;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import com.gitlab.amirmehdi.util.JalaliCalendar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public abstract class Strategy {
    protected final OptionRepository optionRepository;
    protected final OptionStatsService optionStatsService;

    @Value("${application.channel-id}")
    protected String optionEraChatId;
    @Value("${application.api-token}")
    private String apiToken;


    protected Strategy(OptionRepository optionRepository, OptionStatsService optionStatsService) {
        this.optionRepository = optionRepository;
        this.optionStatsService = optionStatsService;
    }

    public abstract List<TelegramMessageDto> getSignals();

    public String getCron() {
        return null;
    }

    protected String getMessageTemplate(OptionStats optionStats, String strategy, String risk) {
        String s = "\uD83C\uDF10نماد: %s " +
            "<a href='%s'>TSETMC</a> \n" +
            "\uD83D\uDCDDدارایی پایه: %s " +
            "<a href='%s'>TSETMC</a> \n" +
            "\uD83D\uDDD3تاریخ: %s \n" +
            "⏰ساعت: %s\n" +
            "\uD83D\uDCB0قیمت بهترین عرضه: %s\n" +
            "\uD83D\uDD06تعداد عرضه: %s\n" +
            "\uD83D\uDCB2بلک شولز: %s\n" +
            "〽️سر به سری: %s\n" +
            "⬆️اهرم: %s\n" +
            "\uD83D\uDCB5استراتژی: #%s\n" +
            "\uD83D\uDEA8ریسک: #%s\n" +
            "@optionera";
        return String.format(s
            , "ض" + optionStats.getOption().getName()
            , getTseLink(optionStats.getOption().getCallTseId())
            , optionStats.getOption().getInstrument().getName()
            , getTseLink(optionStats.getOption().getInstrument().getTseId())
            , new JalaliCalendar(new Date()).toStringRTL()
            , LocalTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_TIME)
            , optionStats.getCallBidAsk().getAskPrice()
            , optionStats.getCallBidAsk().getAskQuantity()
            , optionStats.getCallBlackScholes30()
            , optionStats.getCallBreakEven()
            , optionStats.getCallLeverage()
            , strategy
            , risk);
    }

    private String getTseLink(String callTseId) {
        return "http://www.tsetmc.com/Loader.aspx?ParTree=151311&i=" + callTseId;
    }


    protected TelegramMessageDto getTelegramMessageDto(String text) {
        return new TelegramMessageDto(apiToken, optionEraChatId, text);
    }
}
