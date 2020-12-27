package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Board;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.repository.BoardRepository;
import com.gitlab.amirmehdi.service.dto.core.BidAskItem;
import com.gitlab.amirmehdi.service.dto.core.ClientsInfo;
import com.gitlab.amirmehdi.service.dto.core.StockWatch;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Board}.
 */
@Service
@Transactional
public class BoardService {

    private final Logger log = LoggerFactory.getLogger(BoardService.class);

    private final BoardRepository boardRepository;
    private final OptionService optionService;
    private final Market market;
    private final InstrumentService instrumentService;

    public BoardService(BoardRepository boardRepository, OptionService optionService, Market market, InstrumentService instrumentService) {
        this.boardRepository = boardRepository;
        this.optionService = optionService;
        this.market = market;
        this.instrumentService = instrumentService;
    }

    public void updateAllBoard(String s) {
        List<String> isins = new ArrayList<>();
        isins.add(s);
        isins.addAll(optionService.findAllCallAndPutIsinsByInstrumentIsin(s));
        updateBoardForIsins(isins);
    }

    @Async
    public void updateAllBoard() {
        List<String> isins = instrumentService.findAll().stream().map(Instrument::getIsin).collect(Collectors.toList());
        isins.addAll(optionService.findAllCallAndPutIsins());

        Lists.partition(isins, 100).forEach(this::updateBoardForIsins);
    }

    private void updateBoardForIsins(List<String> strings) {
        List<Board> boards = strings.stream()
            .map(this::getBoard)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        boardRepository.saveAll(boards);
    }

    private Board getBoard(String isin) {
        StockWatch stockWatch = market.getStockWatch(isin);
        if (stockWatch == null) {
            return null;
        }
        BidAskItem bidAsk = market.getBidAsk(isin).getBestBidAsk();
        ClientsInfo clientsInfo = market.getClientsInfo(isin);
        Board board = new Board()
            .isin(isin);

        board.close(stockWatch.getClosing())
            .last(stockWatch.getLast())
            .first(stockWatch.getFirst())
            .referencePrice(stockWatch.getReferencePrice())
            .low(stockWatch.getLow())
            .high(stockWatch.getHigh())
            .min(stockWatch.getMin())
            .max(stockWatch.getMax())
            .tradeCount(stockWatch.getTradesCount())
            .tradeVolume(stockWatch.getTradeVolume())
            .tradeValue(stockWatch.getTradeValue())
            .state(stockWatch.getState());

        board.askPrice(bidAsk.getAskPrice())
            .bidPrice(bidAsk.getBidPrice())
            .bidVolume(bidAsk.getBidQuantity())
            .askVolume(bidAsk.getAskQuantity());
        if (clientsInfo != null) {
            board.legalBuyVolume(clientsInfo.getNaturalBuyVolume())
                .legalSellVolume(clientsInfo.getNaturalSellVolume())
                .individualBuyVolume(clientsInfo.getIndividualBuyVolume())
                .individualSellVolume(clientsInfo.getIndividualSellVolume());
        }
        return board;
    }

    /**
     * Save a board.
     *
     * @param board the entity to save.
     * @return the persisted entity.
     */
    public Board save(Board board) {
        log.debug("Request to save Board : {}", board);
        return boardRepository.save(board);
    }

    public void save(String isin) {
        log.debug("Request to save Board for isin : {}", isin);
        Board board = getBoard(isin);
        if (board == null) {
            return;
        }
        boardRepository.save(board);
    }

    /**
     * Get all the boards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Board> findAll(Pageable pageable) {
        log.debug("Request to get all Boards");
        return boardRepository.findAll(pageable);
    }

    /**
     * Get one board by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Board> findOne(String id) {
        log.debug("Request to get Board : {}", id);
        return boardRepository.findById(id);
    }
}
