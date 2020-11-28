package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.OptionStats;
import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.Signal;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.Market;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.StrategyResponse;
import com.gitlab.amirmehdi.service.dto.core.BidAsk;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import com.gitlab.amirmehdi.util.JalaliCalendar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public abstract class Strategy {
    protected final OptionRepository optionRepository;
    protected final OptionStatsService optionStatsService;
    protected final Market market;

    @Value("${application.telegram.publicChat}")
    protected String optionEraChatId;


    protected Strategy(OptionRepository optionRepository, OptionStatsService optionStatsService, Market market) {
        this.optionRepository = optionRepository;
        this.optionStatsService = optionStatsService;
        this.market = market;
    }

    public abstract StrategyResponse getSignals();

    public String getCron() {
        return null;
    }

    public String getMessageTemplate(String isin) {
        OptionStats optionStats = optionStatsService.findByCallOrPutIsin(isin);
        String s = "\uD83C\uDF10نماد: #%s " +
            "<a href='%s'>TSETMC</a> \n" +
            "\uD83D\uDCDDدارایی پایه: #%s " +
            "<a href='%s'>TSETMC</a> \n" +
            "⏰تاریخ: %s \n" +
            "⏳روز تا سررسید: %s \n" +
            "\uD83D\uDD04حجم معاملات: %s\n" +
            "↔️بازه ی روز: %s\n" +
            "\uD83D\uDCB0بهترین عرضه: %s\n" +
            "\uD83D\uDD06تعداد عرضه: %s\n" +
            "\uD83D\uDCB2بلک شولز: %s\n" +
            "〽️سر به سری: %s\n" +
            "⬆️اهرم: %s\n" +
            "\uD83D\uDCB5استراتژی: #%s\n" +
            "\uD83D\uDEA8ریسک: #%s\n" +
            "@optionera";
        return String.format(s
            , "ض" + optionStats.getOption().getName() + "-" + optionStats.getOption().getStrikePrice() + '-' + new JalaliCalendar(optionStats.getOption().getExpDate()).toStringRTL()
            , getTseLink(optionStats.getOption().getCallTseId())
            , optionStats.getOption().getInstrument().getName()
            , getTseLink(optionStats.getOption().getInstrument().getTseId())
            , new JalaliCalendar(new Date()).toStringRTL() + "-" + LocalTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_TIME)
            , ChronoUnit.DAYS.between(LocalDate.now(), optionStats.getOption().getExpDate())
            , optionStats.getCallStockWatch().getTradeVolume()
            , optionStats.getCallStockWatch().getLow() + " - " + optionStats.getCallStockWatch().getHigh()
            , optionStats.getCallBidAsk().getAskPrice()
            , optionStats.getCallBidAsk().getAskQuantity()
            , optionStats.getCallBlackScholes30()
            , optionStats.getCallBreakEven()
            , optionStats.getCallLeverage()
            , getStrategyDesc()
            , getStrategyRisk());
    }

    private String getTseLink(String callTseId) {
        return "http://www.tsetmc.com/Loader.aspx?ParTree=151311&i=" + callTseId;
    }

    protected abstract String getStrategyDesc();

    protected abstract String getStrategyRisk();


    protected final Signal getSignal(String isin) {
        Option option = optionRepository.findByCallIsinOrPutIsin(isin).orElseThrow(RuntimeException::new);
        StockWatch stockWatch = market.getStockWatch(isin);
        if (stockWatch.getState() != null && !stockWatch.getState().equals("A")){
            return null;
        }
        StockWatch baseStockwatch = market.getStockWatch(option.getInstrument().getIsin());
        BidAsk bidAsk = market.getBidAsk(isin);
        return new Signal()
            .type(getClass().getSimpleName())
            .isin(isin)
            .last(stockWatch.getLast())
            .tradeVolume(stockWatch.getTradeVolume())
            .bidVolume(bidAsk.getBestBidAsk().getBidQuantity())
            .bidPrice(bidAsk.getBestBidAsk().getBidPrice())
            .askVolume(bidAsk.getBestBidAsk().getAskQuantity())
            .askPrice(bidAsk.getBestBidAsk().getAskPrice())
            .baseInstrumentLast(baseStockwatch.getLast());
    }

    public String getMessageTemplateWithOrderLink(Signal signal) {
        String template = getMessageTemplate(signal.getIsin());
        return String.format(template.substring(0, template.lastIndexOf("\n"))
                + " \n<a href='http://optionera.ir/signal/%s'>BUY</a> "
            , signal.getId());
    }

    public List<Order> getOrder(Signal signal) {
        return null;
    }
}
