package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.Signal;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.domain.enumeration.Side;
import com.gitlab.amirmehdi.domain.enumeration.Validity;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.Market;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.calculator.ValuablePrice;
import com.gitlab.amirmehdi.service.dto.StrategyResponse;
import com.gitlab.amirmehdi.service.dto.core.BidAskItem;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ExerciseOptionsStrategy extends Strategy {
    //isin x2
    private final Map<String, Long> cachedIsin = ExpiringMap.builder()
        .expirationPolicy(ExpiringMap.ExpirationPolicy.CREATED)
        .expiration(5, TimeUnit.MINUTES)
        .build();

    protected ExerciseOptionsStrategy(OptionRepository optionRepository, OptionStatsService optionStatsService, Market market) {
        super(optionRepository, optionStatsService, market);
    }

    @Override
    public StrategyResponse getSignals() {
        StrategyResponse strategyResponse = StrategyResponse.builder()
            .publicChatId(null)
            .sendOrderType(StrategyResponse.SendOrderType.NEED_ALLOW)
            .callSignals(new ArrayList<>())
            .build();
        optionRepository.findAllOptionsByExpDate(LocalDate.now())
            .stream()
            .collect(Collectors.groupingBy(option -> option.getInstrument().getIsin()))
            .forEach((isin, options) -> {
                StockWatch stockWatch = market.getStockWatch(isin);
                long valuablePrice = ValuablePrice.calc(stockWatch.getClosing(), stockWatch.getLast(), stockWatch.getReferencePrice(), stockWatch.getMax());
                List<Signal> signals = options
                    .stream()
                    .filter(option -> {
                        BidAskItem callBidAsk = market.getBidAsk(option.getCallIsin()).getBestBidAsk();
                        int callEffectivePrice = getCallEffectivePrice(option, callBidAsk);
                        if (callEffectivePrice < valuablePrice) {
                            if (!cachedIsin.containsKey(option.getCallIsin()) || callEffectivePrice - valuablePrice < cachedIsin.get(option.getCallIsin())) {
                                cachedIsin.put(option.getCallIsin(), callEffectivePrice - valuablePrice);
                                return true;
                            }
                        }
                        return false;
                    })
                    .map(option -> getSignal(option.getCallIsin()))
                    .collect(Collectors.toList());
                if (!signals.isEmpty()) {
                    strategyResponse.getCallSignals().addAll(signals);
                }
            });

        return strategyResponse;
    }

    private int getCallEffectivePrice(Option option, BidAskItem callBidAsk) {
        if (callBidAsk.getAskPrice() == 0) {
            return Integer.MAX_VALUE;
        }
        return option.getStrikePrice() + callBidAsk.getAskPrice();
    }

    @Override
    public List<Order> getOrder(Signal signal) {
        Option option = optionRepository.findByCallIsinOrPutIsin(signal.getIsin()).get();
        StockWatch baseStockwatch = market.getStockWatch(option.getInstrument().getIsin());
        int quantity = (int) Math.min(1.5 * signal.getAskVolume(), 40_000_000.0 / (option.getContractSize() * baseStockwatch.getLast()));
        return Collections.singletonList(new Order()
            .isin(signal.getIsin())
            .side(Side.BUY)
            .validity(Validity.FILL_AND_KILL)
            .price(signal.getAskPrice())
            .quantity(quantity)
            .broker(Broker.REFAH)
            .signal(signal));
    }

    @Override
    protected String getStrategyDesc() {
        return "خریدـبـقصدـاعمال";
    }

    @Override
    protected String getStrategyRisk() {
        return "ریسک کم";
    }
}
