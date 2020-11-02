package com.gitlab.amirmehdi.service.strategy;

import com.gitlab.amirmehdi.repository.BoardRepository;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.Market;
import com.gitlab.amirmehdi.service.OptionStatsService;
import com.gitlab.amirmehdi.service.dto.StrategyResponse;
import net.jodah.expiringmap.ExpiringMap;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LegalSupplyStrategy extends Strategy {
    private final BoardRepository boardRepository;
    private final Map<String, Float> cachedIsin = ExpiringMap.builder()
        .expirationPolicy(ExpiringMap.ExpirationPolicy.CREATED)
        .expiration(90, TimeUnit.MINUTES)
        .build();

    protected LegalSupplyStrategy(OptionRepository optionRepository, OptionStatsService optionStatsService, Market market, BoardRepository boardRepository) {
        super(optionRepository, optionStatsService, market);
        this.boardRepository = boardRepository;
    }

    @Override
    public StrategyResponse getSignals() {
        return StrategyResponse.builder()
            .callSignals(boardRepository.findAllForLegalSupplyStrategy()
                .stream()
                .filter(board -> !cachedIsin.containsKey(board.getIsin()))
                .peek(board -> cachedIsin.put(board.getIsin(), (float) (board.getLegalBuyVolume()) / board.getTradeVolume()))
                .map(board -> getSignal(board.getIsin()))
                .collect(Collectors.toList()))
            .publicChatId(optionEraChatId)
            .sendOrderType(StrategyResponse.SendOrderType.NONE)
            .build();
    }

    @Override
    protected String getStrategyDesc() {
        return "عرضهـحقوقی حقوقی درحال عرضه است";
    }

    @Override
    protected String getStrategyRisk() {
        return "متوسط";
    }

    @Override
    public String getCron() {
        return "30 * 9-12 * * *";
    }
}
