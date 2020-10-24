package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.config.ApplicationProperties;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.Signal;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.domain.enumeration.Side;
import com.gitlab.amirmehdi.domain.enumeration.Validity;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.Market;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.StrategyResponse;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UnusualPrices1Strategy extends Strategy {
    private final Map<Long, Float> cachedIsin = ExpiringMap.builder()
        .expirationPolicy(ExpiringMap.ExpirationPolicy.CREATED)
        .expiration(15, TimeUnit.MINUTES)
        .build();

    @Autowired
    private ApplicationProperties properties;

    protected UnusualPrices1Strategy(OptionRepository optionRepository, OptionStatsService optionStatsService, Market market) {
        super(optionRepository, optionStatsService, market);
    }

    @Override
    public StrategyResponse getSignals() {
        return StrategyResponse.builder()
            .callSignals(optionRepository.findAllByCallBreakEvenIsLessThanEqual(properties.getUnusual1Threshold())
                .stream()
                .filter(option -> !cachedIsin.containsKey(option.getId()) || cachedIsin.get(option.getId()) - 1 > option.getCallBreakEven())
                .peek(option -> cachedIsin.put(option.getId(), option.getCallBreakEven()))
                .map(option -> getSignal(option.getCallIsin()))
                .collect(Collectors.toList()))
            .publicChatId(optionEraChatId)
            .sendOrderType(StrategyResponse.SendOrderType.NEED_ALLOW)
            .build();
    }

    @Override
    public String getCron() {
        return "*/10 * 9-12 * * *";
    }

    @Override
    protected String getStrategyDesc() {
        return "آربیتراژ قیمت موثر اختیار برای اعمال کمتر از قیمت سهم است";
    }

    @Override
    protected String getStrategyRisk() {
        return "کم";
    }

    @Override
    public List<Order> getOrder(Signal signal) {
        Option option = optionRepository.findByCallIsinOrPutIsin(signal.getIsin()).get();
        StockWatch baseStockwatch = market.getStockWatch(option.getInstrument().getIsin());
        int quantity = (int) Math.min(1.5 * signal.getAskVolume(), 40_000_000.0 / (option.getContractSize() * baseStockwatch.getLast()));
        return Collections.singletonList(new Order()
            .isin(signal.getIsin())
            .side(Side.BUY)
            .validity(Validity.DAY)
            .price(signal.getAskPrice())
            .quantity(quantity)
            .broker(Broker.REFAH)
            .signal(signal));
    }
}
