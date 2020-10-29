package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.ETradeApp;
import com.gitlab.amirmehdi.domain.Board;
import com.gitlab.amirmehdi.repository.BoardRepository;
import com.gitlab.amirmehdi.service.BoardQueryService;
import com.gitlab.amirmehdi.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BoardResource} REST controller.
 */
@SpringBootTest(classes = ETradeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class BoardResourceIT {

    private static final String DEFAULT_ISIN = "AAAAAAAAAA";
    private static final String UPDATED_ISIN = "BBBBBBBBBB";

    private static final Date DEFAULT_DATE = new Date();
    private static final Date UPDATED_DATE = Date.from(Instant.now().truncatedTo(ChronoUnit.MILLIS));

    private static final Integer DEFAULT_LAST = 1;
    private static final Integer UPDATED_LAST = 2;
    private static final Integer SMALLER_LAST = 1 - 1;

    private static final Integer DEFAULT_CLOSE = 1;
    private static final Integer UPDATED_CLOSE = 2;
    private static final Integer SMALLER_CLOSE = 1 - 1;

    private static final Integer DEFAULT_FIRST = 1;
    private static final Integer UPDATED_FIRST = 2;
    private static final Integer SMALLER_FIRST = 1 - 1;

    private static final Integer DEFAULT_LOW = 1;
    private static final Integer UPDATED_LOW = 2;
    private static final Integer SMALLER_LOW = 1 - 1;

    private static final Integer DEFAULT_HIGH = 1;
    private static final Integer UPDATED_HIGH = 2;
    private static final Integer SMALLER_HIGH = 1 - 1;

    private static final Integer DEFAULT_TRADE_COUNT = 1;
    private static final Integer UPDATED_TRADE_COUNT = 2;
    private static final Integer SMALLER_TRADE_COUNT = 1 - 1;

    private static final Long DEFAULT_TRADE_VOLUME = 1L;
    private static final Long UPDATED_TRADE_VOLUME = 2L;
    private static final Long SMALLER_TRADE_VOLUME = 1L - 1L;

    private static final Long DEFAULT_TRADE_VALUE = 1L;
    private static final Long UPDATED_TRADE_VALUE = 2L;
    private static final Long SMALLER_TRADE_VALUE = 1L - 1L;

    private static final Integer DEFAULT_ASK_PRICE = 1;
    private static final Integer UPDATED_ASK_PRICE = 2;
    private static final Integer SMALLER_ASK_PRICE = 1 - 1;

    private static final Integer DEFAULT_ASK_VOLUME = 1;
    private static final Integer UPDATED_ASK_VOLUME = 2;
    private static final Integer SMALLER_ASK_VOLUME = 1 - 1;

    private static final Integer DEFAULT_BID_PRICE = 1;
    private static final Integer UPDATED_BID_PRICE = 2;
    private static final Integer SMALLER_BID_PRICE = 1 - 1;

    private static final Integer DEFAULT_BID_VOLUME = 1;
    private static final Integer UPDATED_BID_VOLUME = 2;
    private static final Integer SMALLER_BID_VOLUME = 1 - 1;

    private static final Integer DEFAULT_INDIVIDUAL_BUY_VOLUME = 1;
    private static final Integer UPDATED_INDIVIDUAL_BUY_VOLUME = 2;
    private static final Integer SMALLER_INDIVIDUAL_BUY_VOLUME = 1 - 1;

    private static final Integer DEFAULT_INDIVIDUAL_SELL_VOLUME = 1;
    private static final Integer UPDATED_INDIVIDUAL_SELL_VOLUME = 2;
    private static final Integer SMALLER_INDIVIDUAL_SELL_VOLUME = 1 - 1;

    private static final Integer DEFAULT_LEGAL_BUY_VOLUME = 1;
    private static final Integer UPDATED_LEGAL_BUY_VOLUME = 2;
    private static final Integer SMALLER_LEGAL_BUY_VOLUME = 1 - 1;

    private static final Integer DEFAULT_LEGAL_SELL_VOLUME = 1;
    private static final Integer UPDATED_LEGAL_SELL_VOLUME = 2;
    private static final Integer SMALLER_LEGAL_SELL_VOLUME = 1 - 1;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardQueryService boardQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBoardMockMvc;

    private Board board;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Board createEntity(EntityManager em) {
        Board board = new Board()
            .isin(DEFAULT_ISIN)
            .date(DEFAULT_DATE)
            .last(DEFAULT_LAST)
            .close(DEFAULT_CLOSE)
            .first(DEFAULT_FIRST)
            .low(DEFAULT_LOW)
            .high(DEFAULT_HIGH)
            .tradeCount(DEFAULT_TRADE_COUNT)
            .tradeVolume(DEFAULT_TRADE_VOLUME)
            .tradeValue(DEFAULT_TRADE_VALUE)
            .askPrice(DEFAULT_ASK_PRICE)
            .askVolume(DEFAULT_ASK_VOLUME)
            .bidPrice(DEFAULT_BID_PRICE)
            .bidVolume(DEFAULT_BID_VOLUME)
            .individualBuyVolume(DEFAULT_INDIVIDUAL_BUY_VOLUME)
            .individualSellVolume(DEFAULT_INDIVIDUAL_SELL_VOLUME)
            .legalBuyVolume(DEFAULT_LEGAL_BUY_VOLUME)
            .legalSellVolume(DEFAULT_LEGAL_SELL_VOLUME);
        return board;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Board createUpdatedEntity(EntityManager em) {
        Board board = new Board()
            .isin(UPDATED_ISIN)
            .date(UPDATED_DATE)
            .last(UPDATED_LAST)
            .close(UPDATED_CLOSE)
            .first(UPDATED_FIRST)
            .low(UPDATED_LOW)
            .high(UPDATED_HIGH)
            .tradeCount(UPDATED_TRADE_COUNT)
            .tradeVolume(UPDATED_TRADE_VOLUME)
            .tradeValue(UPDATED_TRADE_VALUE)
            .askPrice(UPDATED_ASK_PRICE)
            .askVolume(UPDATED_ASK_VOLUME)
            .bidPrice(UPDATED_BID_PRICE)
            .bidVolume(UPDATED_BID_VOLUME)
            .individualBuyVolume(UPDATED_INDIVIDUAL_BUY_VOLUME)
            .individualSellVolume(UPDATED_INDIVIDUAL_SELL_VOLUME)
            .legalBuyVolume(UPDATED_LEGAL_BUY_VOLUME)
            .legalSellVolume(UPDATED_LEGAL_SELL_VOLUME);
        return board;
    }

    @BeforeEach
    public void initTest() {
        board = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllBoards() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList
        restBoardMockMvc.perform(get("/api/boards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].isin").value(hasItem(DEFAULT_ISIN)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].last").value(hasItem(DEFAULT_LAST)))
            .andExpect(jsonPath("$.[*].close").value(hasItem(DEFAULT_CLOSE)))
            .andExpect(jsonPath("$.[*].first").value(hasItem(DEFAULT_FIRST)))
            .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW)))
            .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH)))
            .andExpect(jsonPath("$.[*].tradeCount").value(hasItem(DEFAULT_TRADE_COUNT)))
            .andExpect(jsonPath("$.[*].tradeVolume").value(hasItem(DEFAULT_TRADE_VOLUME.intValue())))
            .andExpect(jsonPath("$.[*].tradeValue").value(hasItem(DEFAULT_TRADE_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].askPrice").value(hasItem(DEFAULT_ASK_PRICE)))
            .andExpect(jsonPath("$.[*].askVolume").value(hasItem(DEFAULT_ASK_VOLUME)))
            .andExpect(jsonPath("$.[*].bidPrice").value(hasItem(DEFAULT_BID_PRICE)))
            .andExpect(jsonPath("$.[*].bidVolume").value(hasItem(DEFAULT_BID_VOLUME)))
            .andExpect(jsonPath("$.[*].individualBuyVolume").value(hasItem(DEFAULT_INDIVIDUAL_BUY_VOLUME)))
            .andExpect(jsonPath("$.[*].individualSellVolume").value(hasItem(DEFAULT_INDIVIDUAL_SELL_VOLUME)))
            .andExpect(jsonPath("$.[*].legalBuyVolume").value(hasItem(DEFAULT_LEGAL_BUY_VOLUME)))
            .andExpect(jsonPath("$.[*].legalSellVolume").value(hasItem(DEFAULT_LEGAL_SELL_VOLUME)));
    }

    @Test
    @Transactional
    public void getBoard() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get the board
        restBoardMockMvc.perform(get("/api/boards/{id}", board.getIsin()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.isin").value(DEFAULT_ISIN))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.last").value(DEFAULT_LAST))
            .andExpect(jsonPath("$.close").value(DEFAULT_CLOSE))
            .andExpect(jsonPath("$.first").value(DEFAULT_FIRST))
            .andExpect(jsonPath("$.low").value(DEFAULT_LOW))
            .andExpect(jsonPath("$.high").value(DEFAULT_HIGH))
            .andExpect(jsonPath("$.tradeCount").value(DEFAULT_TRADE_COUNT))
            .andExpect(jsonPath("$.tradeVolume").value(DEFAULT_TRADE_VOLUME.intValue()))
            .andExpect(jsonPath("$.tradeValue").value(DEFAULT_TRADE_VALUE.intValue()))
            .andExpect(jsonPath("$.askPrice").value(DEFAULT_ASK_PRICE))
            .andExpect(jsonPath("$.askVolume").value(DEFAULT_ASK_VOLUME))
            .andExpect(jsonPath("$.bidPrice").value(DEFAULT_BID_PRICE))
            .andExpect(jsonPath("$.bidVolume").value(DEFAULT_BID_VOLUME))
            .andExpect(jsonPath("$.individualBuyVolume").value(DEFAULT_INDIVIDUAL_BUY_VOLUME))
            .andExpect(jsonPath("$.individualSellVolume").value(DEFAULT_INDIVIDUAL_SELL_VOLUME))
            .andExpect(jsonPath("$.legalBuyVolume").value(DEFAULT_LEGAL_BUY_VOLUME))
            .andExpect(jsonPath("$.legalSellVolume").value(DEFAULT_LEGAL_SELL_VOLUME));
    }


    @Test
    @Transactional
    public void getBoardsByIdFiltering() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        String id = board.getIsin();

        defaultBoardShouldBeFound("id.equals=" + id);
        defaultBoardShouldNotBeFound("id.notEquals=" + id);

        defaultBoardShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBoardShouldNotBeFound("id.greaterThan=" + id);

        defaultBoardShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBoardShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBoardsByIsinIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where isin equals to DEFAULT_ISIN
        defaultBoardShouldBeFound("isin.equals=" + DEFAULT_ISIN);

        // Get all the boardList where isin equals to UPDATED_ISIN
        defaultBoardShouldNotBeFound("isin.equals=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllBoardsByIsinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where isin not equals to DEFAULT_ISIN
        defaultBoardShouldNotBeFound("isin.notEquals=" + DEFAULT_ISIN);

        // Get all the boardList where isin not equals to UPDATED_ISIN
        defaultBoardShouldBeFound("isin.notEquals=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllBoardsByIsinIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where isin in DEFAULT_ISIN or UPDATED_ISIN
        defaultBoardShouldBeFound("isin.in=" + DEFAULT_ISIN + "," + UPDATED_ISIN);

        // Get all the boardList where isin equals to UPDATED_ISIN
        defaultBoardShouldNotBeFound("isin.in=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllBoardsByIsinIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where isin is not null
        defaultBoardShouldBeFound("isin.specified=true");

        // Get all the boardList where isin is null
        defaultBoardShouldNotBeFound("isin.specified=false");
    }
                @Test
    @Transactional
    public void getAllBoardsByIsinContainsSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where isin contains DEFAULT_ISIN
        defaultBoardShouldBeFound("isin.contains=" + DEFAULT_ISIN);

        // Get all the boardList where isin contains UPDATED_ISIN
        defaultBoardShouldNotBeFound("isin.contains=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllBoardsByIsinNotContainsSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where isin does not contain DEFAULT_ISIN
        defaultBoardShouldNotBeFound("isin.doesNotContain=" + DEFAULT_ISIN);

        // Get all the boardList where isin does not contain UPDATED_ISIN
        defaultBoardShouldBeFound("isin.doesNotContain=" + UPDATED_ISIN);
    }


    @Test
    @Transactional
    public void getAllBoardsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where date equals to DEFAULT_DATE
        defaultBoardShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the boardList where date equals to UPDATED_DATE
        defaultBoardShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBoardsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where date not equals to DEFAULT_DATE
        defaultBoardShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the boardList where date not equals to UPDATED_DATE
        defaultBoardShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBoardsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where date in DEFAULT_DATE or UPDATED_DATE
        defaultBoardShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the boardList where date equals to UPDATED_DATE
        defaultBoardShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBoardsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where date is not null
        defaultBoardShouldBeFound("date.specified=true");

        // Get all the boardList where date is null
        defaultBoardShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByLastIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where last equals to DEFAULT_LAST
        defaultBoardShouldBeFound("last.equals=" + DEFAULT_LAST);

        // Get all the boardList where last equals to UPDATED_LAST
        defaultBoardShouldNotBeFound("last.equals=" + UPDATED_LAST);
    }

    @Test
    @Transactional
    public void getAllBoardsByLastIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where last not equals to DEFAULT_LAST
        defaultBoardShouldNotBeFound("last.notEquals=" + DEFAULT_LAST);

        // Get all the boardList where last not equals to UPDATED_LAST
        defaultBoardShouldBeFound("last.notEquals=" + UPDATED_LAST);
    }

    @Test
    @Transactional
    public void getAllBoardsByLastIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where last in DEFAULT_LAST or UPDATED_LAST
        defaultBoardShouldBeFound("last.in=" + DEFAULT_LAST + "," + UPDATED_LAST);

        // Get all the boardList where last equals to UPDATED_LAST
        defaultBoardShouldNotBeFound("last.in=" + UPDATED_LAST);
    }

    @Test
    @Transactional
    public void getAllBoardsByLastIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where last is not null
        defaultBoardShouldBeFound("last.specified=true");

        // Get all the boardList where last is null
        defaultBoardShouldNotBeFound("last.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByLastIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where last is greater than or equal to DEFAULT_LAST
        defaultBoardShouldBeFound("last.greaterThanOrEqual=" + DEFAULT_LAST);

        // Get all the boardList where last is greater than or equal to UPDATED_LAST
        defaultBoardShouldNotBeFound("last.greaterThanOrEqual=" + UPDATED_LAST);
    }

    @Test
    @Transactional
    public void getAllBoardsByLastIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where last is less than or equal to DEFAULT_LAST
        defaultBoardShouldBeFound("last.lessThanOrEqual=" + DEFAULT_LAST);

        // Get all the boardList where last is less than or equal to SMALLER_LAST
        defaultBoardShouldNotBeFound("last.lessThanOrEqual=" + SMALLER_LAST);
    }

    @Test
    @Transactional
    public void getAllBoardsByLastIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where last is less than DEFAULT_LAST
        defaultBoardShouldNotBeFound("last.lessThan=" + DEFAULT_LAST);

        // Get all the boardList where last is less than UPDATED_LAST
        defaultBoardShouldBeFound("last.lessThan=" + UPDATED_LAST);
    }

    @Test
    @Transactional
    public void getAllBoardsByLastIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where last is greater than DEFAULT_LAST
        defaultBoardShouldNotBeFound("last.greaterThan=" + DEFAULT_LAST);

        // Get all the boardList where last is greater than SMALLER_LAST
        defaultBoardShouldBeFound("last.greaterThan=" + SMALLER_LAST);
    }


    @Test
    @Transactional
    public void getAllBoardsByCloseIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where close equals to DEFAULT_CLOSE
        defaultBoardShouldBeFound("close.equals=" + DEFAULT_CLOSE);

        // Get all the boardList where close equals to UPDATED_CLOSE
        defaultBoardShouldNotBeFound("close.equals=" + UPDATED_CLOSE);
    }

    @Test
    @Transactional
    public void getAllBoardsByCloseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where close not equals to DEFAULT_CLOSE
        defaultBoardShouldNotBeFound("close.notEquals=" + DEFAULT_CLOSE);

        // Get all the boardList where close not equals to UPDATED_CLOSE
        defaultBoardShouldBeFound("close.notEquals=" + UPDATED_CLOSE);
    }

    @Test
    @Transactional
    public void getAllBoardsByCloseIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where close in DEFAULT_CLOSE or UPDATED_CLOSE
        defaultBoardShouldBeFound("close.in=" + DEFAULT_CLOSE + "," + UPDATED_CLOSE);

        // Get all the boardList where close equals to UPDATED_CLOSE
        defaultBoardShouldNotBeFound("close.in=" + UPDATED_CLOSE);
    }

    @Test
    @Transactional
    public void getAllBoardsByCloseIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where close is not null
        defaultBoardShouldBeFound("close.specified=true");

        // Get all the boardList where close is null
        defaultBoardShouldNotBeFound("close.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByCloseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where close is greater than or equal to DEFAULT_CLOSE
        defaultBoardShouldBeFound("close.greaterThanOrEqual=" + DEFAULT_CLOSE);

        // Get all the boardList where close is greater than or equal to UPDATED_CLOSE
        defaultBoardShouldNotBeFound("close.greaterThanOrEqual=" + UPDATED_CLOSE);
    }

    @Test
    @Transactional
    public void getAllBoardsByCloseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where close is less than or equal to DEFAULT_CLOSE
        defaultBoardShouldBeFound("close.lessThanOrEqual=" + DEFAULT_CLOSE);

        // Get all the boardList where close is less than or equal to SMALLER_CLOSE
        defaultBoardShouldNotBeFound("close.lessThanOrEqual=" + SMALLER_CLOSE);
    }

    @Test
    @Transactional
    public void getAllBoardsByCloseIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where close is less than DEFAULT_CLOSE
        defaultBoardShouldNotBeFound("close.lessThan=" + DEFAULT_CLOSE);

        // Get all the boardList where close is less than UPDATED_CLOSE
        defaultBoardShouldBeFound("close.lessThan=" + UPDATED_CLOSE);
    }

    @Test
    @Transactional
    public void getAllBoardsByCloseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where close is greater than DEFAULT_CLOSE
        defaultBoardShouldNotBeFound("close.greaterThan=" + DEFAULT_CLOSE);

        // Get all the boardList where close is greater than SMALLER_CLOSE
        defaultBoardShouldBeFound("close.greaterThan=" + SMALLER_CLOSE);
    }


    @Test
    @Transactional
    public void getAllBoardsByFirstIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where first equals to DEFAULT_FIRST
        defaultBoardShouldBeFound("first.equals=" + DEFAULT_FIRST);

        // Get all the boardList where first equals to UPDATED_FIRST
        defaultBoardShouldNotBeFound("first.equals=" + UPDATED_FIRST);
    }

    @Test
    @Transactional
    public void getAllBoardsByFirstIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where first not equals to DEFAULT_FIRST
        defaultBoardShouldNotBeFound("first.notEquals=" + DEFAULT_FIRST);

        // Get all the boardList where first not equals to UPDATED_FIRST
        defaultBoardShouldBeFound("first.notEquals=" + UPDATED_FIRST);
    }

    @Test
    @Transactional
    public void getAllBoardsByFirstIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where first in DEFAULT_FIRST or UPDATED_FIRST
        defaultBoardShouldBeFound("first.in=" + DEFAULT_FIRST + "," + UPDATED_FIRST);

        // Get all the boardList where first equals to UPDATED_FIRST
        defaultBoardShouldNotBeFound("first.in=" + UPDATED_FIRST);
    }

    @Test
    @Transactional
    public void getAllBoardsByFirstIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where first is not null
        defaultBoardShouldBeFound("first.specified=true");

        // Get all the boardList where first is null
        defaultBoardShouldNotBeFound("first.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByFirstIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where first is greater than or equal to DEFAULT_FIRST
        defaultBoardShouldBeFound("first.greaterThanOrEqual=" + DEFAULT_FIRST);

        // Get all the boardList where first is greater than or equal to UPDATED_FIRST
        defaultBoardShouldNotBeFound("first.greaterThanOrEqual=" + UPDATED_FIRST);
    }

    @Test
    @Transactional
    public void getAllBoardsByFirstIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where first is less than or equal to DEFAULT_FIRST
        defaultBoardShouldBeFound("first.lessThanOrEqual=" + DEFAULT_FIRST);

        // Get all the boardList where first is less than or equal to SMALLER_FIRST
        defaultBoardShouldNotBeFound("first.lessThanOrEqual=" + SMALLER_FIRST);
    }

    @Test
    @Transactional
    public void getAllBoardsByFirstIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where first is less than DEFAULT_FIRST
        defaultBoardShouldNotBeFound("first.lessThan=" + DEFAULT_FIRST);

        // Get all the boardList where first is less than UPDATED_FIRST
        defaultBoardShouldBeFound("first.lessThan=" + UPDATED_FIRST);
    }

    @Test
    @Transactional
    public void getAllBoardsByFirstIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where first is greater than DEFAULT_FIRST
        defaultBoardShouldNotBeFound("first.greaterThan=" + DEFAULT_FIRST);

        // Get all the boardList where first is greater than SMALLER_FIRST
        defaultBoardShouldBeFound("first.greaterThan=" + SMALLER_FIRST);
    }


    @Test
    @Transactional
    public void getAllBoardsByLowIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where low equals to DEFAULT_LOW
        defaultBoardShouldBeFound("low.equals=" + DEFAULT_LOW);

        // Get all the boardList where low equals to UPDATED_LOW
        defaultBoardShouldNotBeFound("low.equals=" + UPDATED_LOW);
    }

    @Test
    @Transactional
    public void getAllBoardsByLowIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where low not equals to DEFAULT_LOW
        defaultBoardShouldNotBeFound("low.notEquals=" + DEFAULT_LOW);

        // Get all the boardList where low not equals to UPDATED_LOW
        defaultBoardShouldBeFound("low.notEquals=" + UPDATED_LOW);
    }

    @Test
    @Transactional
    public void getAllBoardsByLowIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where low in DEFAULT_LOW or UPDATED_LOW
        defaultBoardShouldBeFound("low.in=" + DEFAULT_LOW + "," + UPDATED_LOW);

        // Get all the boardList where low equals to UPDATED_LOW
        defaultBoardShouldNotBeFound("low.in=" + UPDATED_LOW);
    }

    @Test
    @Transactional
    public void getAllBoardsByLowIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where low is not null
        defaultBoardShouldBeFound("low.specified=true");

        // Get all the boardList where low is null
        defaultBoardShouldNotBeFound("low.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByLowIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where low is greater than or equal to DEFAULT_LOW
        defaultBoardShouldBeFound("low.greaterThanOrEqual=" + DEFAULT_LOW);

        // Get all the boardList where low is greater than or equal to UPDATED_LOW
        defaultBoardShouldNotBeFound("low.greaterThanOrEqual=" + UPDATED_LOW);
    }

    @Test
    @Transactional
    public void getAllBoardsByLowIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where low is less than or equal to DEFAULT_LOW
        defaultBoardShouldBeFound("low.lessThanOrEqual=" + DEFAULT_LOW);

        // Get all the boardList where low is less than or equal to SMALLER_LOW
        defaultBoardShouldNotBeFound("low.lessThanOrEqual=" + SMALLER_LOW);
    }

    @Test
    @Transactional
    public void getAllBoardsByLowIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where low is less than DEFAULT_LOW
        defaultBoardShouldNotBeFound("low.lessThan=" + DEFAULT_LOW);

        // Get all the boardList where low is less than UPDATED_LOW
        defaultBoardShouldBeFound("low.lessThan=" + UPDATED_LOW);
    }

    @Test
    @Transactional
    public void getAllBoardsByLowIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where low is greater than DEFAULT_LOW
        defaultBoardShouldNotBeFound("low.greaterThan=" + DEFAULT_LOW);

        // Get all the boardList where low is greater than SMALLER_LOW
        defaultBoardShouldBeFound("low.greaterThan=" + SMALLER_LOW);
    }


    @Test
    @Transactional
    public void getAllBoardsByHighIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where high equals to DEFAULT_HIGH
        defaultBoardShouldBeFound("high.equals=" + DEFAULT_HIGH);

        // Get all the boardList where high equals to UPDATED_HIGH
        defaultBoardShouldNotBeFound("high.equals=" + UPDATED_HIGH);
    }

    @Test
    @Transactional
    public void getAllBoardsByHighIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where high not equals to DEFAULT_HIGH
        defaultBoardShouldNotBeFound("high.notEquals=" + DEFAULT_HIGH);

        // Get all the boardList where high not equals to UPDATED_HIGH
        defaultBoardShouldBeFound("high.notEquals=" + UPDATED_HIGH);
    }

    @Test
    @Transactional
    public void getAllBoardsByHighIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where high in DEFAULT_HIGH or UPDATED_HIGH
        defaultBoardShouldBeFound("high.in=" + DEFAULT_HIGH + "," + UPDATED_HIGH);

        // Get all the boardList where high equals to UPDATED_HIGH
        defaultBoardShouldNotBeFound("high.in=" + UPDATED_HIGH);
    }

    @Test
    @Transactional
    public void getAllBoardsByHighIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where high is not null
        defaultBoardShouldBeFound("high.specified=true");

        // Get all the boardList where high is null
        defaultBoardShouldNotBeFound("high.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByHighIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where high is greater than or equal to DEFAULT_HIGH
        defaultBoardShouldBeFound("high.greaterThanOrEqual=" + DEFAULT_HIGH);

        // Get all the boardList where high is greater than or equal to UPDATED_HIGH
        defaultBoardShouldNotBeFound("high.greaterThanOrEqual=" + UPDATED_HIGH);
    }

    @Test
    @Transactional
    public void getAllBoardsByHighIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where high is less than or equal to DEFAULT_HIGH
        defaultBoardShouldBeFound("high.lessThanOrEqual=" + DEFAULT_HIGH);

        // Get all the boardList where high is less than or equal to SMALLER_HIGH
        defaultBoardShouldNotBeFound("high.lessThanOrEqual=" + SMALLER_HIGH);
    }

    @Test
    @Transactional
    public void getAllBoardsByHighIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where high is less than DEFAULT_HIGH
        defaultBoardShouldNotBeFound("high.lessThan=" + DEFAULT_HIGH);

        // Get all the boardList where high is less than UPDATED_HIGH
        defaultBoardShouldBeFound("high.lessThan=" + UPDATED_HIGH);
    }

    @Test
    @Transactional
    public void getAllBoardsByHighIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where high is greater than DEFAULT_HIGH
        defaultBoardShouldNotBeFound("high.greaterThan=" + DEFAULT_HIGH);

        // Get all the boardList where high is greater than SMALLER_HIGH
        defaultBoardShouldBeFound("high.greaterThan=" + SMALLER_HIGH);
    }


    @Test
    @Transactional
    public void getAllBoardsByTradeCountIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeCount equals to DEFAULT_TRADE_COUNT
        defaultBoardShouldBeFound("tradeCount.equals=" + DEFAULT_TRADE_COUNT);

        // Get all the boardList where tradeCount equals to UPDATED_TRADE_COUNT
        defaultBoardShouldNotBeFound("tradeCount.equals=" + UPDATED_TRADE_COUNT);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeCount not equals to DEFAULT_TRADE_COUNT
        defaultBoardShouldNotBeFound("tradeCount.notEquals=" + DEFAULT_TRADE_COUNT);

        // Get all the boardList where tradeCount not equals to UPDATED_TRADE_COUNT
        defaultBoardShouldBeFound("tradeCount.notEquals=" + UPDATED_TRADE_COUNT);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeCountIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeCount in DEFAULT_TRADE_COUNT or UPDATED_TRADE_COUNT
        defaultBoardShouldBeFound("tradeCount.in=" + DEFAULT_TRADE_COUNT + "," + UPDATED_TRADE_COUNT);

        // Get all the boardList where tradeCount equals to UPDATED_TRADE_COUNT
        defaultBoardShouldNotBeFound("tradeCount.in=" + UPDATED_TRADE_COUNT);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeCount is not null
        defaultBoardShouldBeFound("tradeCount.specified=true");

        // Get all the boardList where tradeCount is null
        defaultBoardShouldNotBeFound("tradeCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeCount is greater than or equal to DEFAULT_TRADE_COUNT
        defaultBoardShouldBeFound("tradeCount.greaterThanOrEqual=" + DEFAULT_TRADE_COUNT);

        // Get all the boardList where tradeCount is greater than or equal to UPDATED_TRADE_COUNT
        defaultBoardShouldNotBeFound("tradeCount.greaterThanOrEqual=" + UPDATED_TRADE_COUNT);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeCount is less than or equal to DEFAULT_TRADE_COUNT
        defaultBoardShouldBeFound("tradeCount.lessThanOrEqual=" + DEFAULT_TRADE_COUNT);

        // Get all the boardList where tradeCount is less than or equal to SMALLER_TRADE_COUNT
        defaultBoardShouldNotBeFound("tradeCount.lessThanOrEqual=" + SMALLER_TRADE_COUNT);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeCountIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeCount is less than DEFAULT_TRADE_COUNT
        defaultBoardShouldNotBeFound("tradeCount.lessThan=" + DEFAULT_TRADE_COUNT);

        // Get all the boardList where tradeCount is less than UPDATED_TRADE_COUNT
        defaultBoardShouldBeFound("tradeCount.lessThan=" + UPDATED_TRADE_COUNT);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeCount is greater than DEFAULT_TRADE_COUNT
        defaultBoardShouldNotBeFound("tradeCount.greaterThan=" + DEFAULT_TRADE_COUNT);

        // Get all the boardList where tradeCount is greater than SMALLER_TRADE_COUNT
        defaultBoardShouldBeFound("tradeCount.greaterThan=" + SMALLER_TRADE_COUNT);
    }


    @Test
    @Transactional
    public void getAllBoardsByTradeVolumeIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeVolume equals to DEFAULT_TRADE_VOLUME
        defaultBoardShouldBeFound("tradeVolume.equals=" + DEFAULT_TRADE_VOLUME);

        // Get all the boardList where tradeVolume equals to UPDATED_TRADE_VOLUME
        defaultBoardShouldNotBeFound("tradeVolume.equals=" + UPDATED_TRADE_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeVolumeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeVolume not equals to DEFAULT_TRADE_VOLUME
        defaultBoardShouldNotBeFound("tradeVolume.notEquals=" + DEFAULT_TRADE_VOLUME);

        // Get all the boardList where tradeVolume not equals to UPDATED_TRADE_VOLUME
        defaultBoardShouldBeFound("tradeVolume.notEquals=" + UPDATED_TRADE_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeVolumeIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeVolume in DEFAULT_TRADE_VOLUME or UPDATED_TRADE_VOLUME
        defaultBoardShouldBeFound("tradeVolume.in=" + DEFAULT_TRADE_VOLUME + "," + UPDATED_TRADE_VOLUME);

        // Get all the boardList where tradeVolume equals to UPDATED_TRADE_VOLUME
        defaultBoardShouldNotBeFound("tradeVolume.in=" + UPDATED_TRADE_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeVolumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeVolume is not null
        defaultBoardShouldBeFound("tradeVolume.specified=true");

        // Get all the boardList where tradeVolume is null
        defaultBoardShouldNotBeFound("tradeVolume.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeVolumeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeVolume is greater than or equal to DEFAULT_TRADE_VOLUME
        defaultBoardShouldBeFound("tradeVolume.greaterThanOrEqual=" + DEFAULT_TRADE_VOLUME);

        // Get all the boardList where tradeVolume is greater than or equal to UPDATED_TRADE_VOLUME
        defaultBoardShouldNotBeFound("tradeVolume.greaterThanOrEqual=" + UPDATED_TRADE_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeVolumeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeVolume is less than or equal to DEFAULT_TRADE_VOLUME
        defaultBoardShouldBeFound("tradeVolume.lessThanOrEqual=" + DEFAULT_TRADE_VOLUME);

        // Get all the boardList where tradeVolume is less than or equal to SMALLER_TRADE_VOLUME
        defaultBoardShouldNotBeFound("tradeVolume.lessThanOrEqual=" + SMALLER_TRADE_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeVolumeIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeVolume is less than DEFAULT_TRADE_VOLUME
        defaultBoardShouldNotBeFound("tradeVolume.lessThan=" + DEFAULT_TRADE_VOLUME);

        // Get all the boardList where tradeVolume is less than UPDATED_TRADE_VOLUME
        defaultBoardShouldBeFound("tradeVolume.lessThan=" + UPDATED_TRADE_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeVolumeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeVolume is greater than DEFAULT_TRADE_VOLUME
        defaultBoardShouldNotBeFound("tradeVolume.greaterThan=" + DEFAULT_TRADE_VOLUME);

        // Get all the boardList where tradeVolume is greater than SMALLER_TRADE_VOLUME
        defaultBoardShouldBeFound("tradeVolume.greaterThan=" + SMALLER_TRADE_VOLUME);
    }


    @Test
    @Transactional
    public void getAllBoardsByTradeValueIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeValue equals to DEFAULT_TRADE_VALUE
        defaultBoardShouldBeFound("tradeValue.equals=" + DEFAULT_TRADE_VALUE);

        // Get all the boardList where tradeValue equals to UPDATED_TRADE_VALUE
        defaultBoardShouldNotBeFound("tradeValue.equals=" + UPDATED_TRADE_VALUE);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeValue not equals to DEFAULT_TRADE_VALUE
        defaultBoardShouldNotBeFound("tradeValue.notEquals=" + DEFAULT_TRADE_VALUE);

        // Get all the boardList where tradeValue not equals to UPDATED_TRADE_VALUE
        defaultBoardShouldBeFound("tradeValue.notEquals=" + UPDATED_TRADE_VALUE);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeValueIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeValue in DEFAULT_TRADE_VALUE or UPDATED_TRADE_VALUE
        defaultBoardShouldBeFound("tradeValue.in=" + DEFAULT_TRADE_VALUE + "," + UPDATED_TRADE_VALUE);

        // Get all the boardList where tradeValue equals to UPDATED_TRADE_VALUE
        defaultBoardShouldNotBeFound("tradeValue.in=" + UPDATED_TRADE_VALUE);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeValue is not null
        defaultBoardShouldBeFound("tradeValue.specified=true");

        // Get all the boardList where tradeValue is null
        defaultBoardShouldNotBeFound("tradeValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeValue is greater than or equal to DEFAULT_TRADE_VALUE
        defaultBoardShouldBeFound("tradeValue.greaterThanOrEqual=" + DEFAULT_TRADE_VALUE);

        // Get all the boardList where tradeValue is greater than or equal to UPDATED_TRADE_VALUE
        defaultBoardShouldNotBeFound("tradeValue.greaterThanOrEqual=" + UPDATED_TRADE_VALUE);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeValue is less than or equal to DEFAULT_TRADE_VALUE
        defaultBoardShouldBeFound("tradeValue.lessThanOrEqual=" + DEFAULT_TRADE_VALUE);

        // Get all the boardList where tradeValue is less than or equal to SMALLER_TRADE_VALUE
        defaultBoardShouldNotBeFound("tradeValue.lessThanOrEqual=" + SMALLER_TRADE_VALUE);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeValueIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeValue is less than DEFAULT_TRADE_VALUE
        defaultBoardShouldNotBeFound("tradeValue.lessThan=" + DEFAULT_TRADE_VALUE);

        // Get all the boardList where tradeValue is less than UPDATED_TRADE_VALUE
        defaultBoardShouldBeFound("tradeValue.lessThan=" + UPDATED_TRADE_VALUE);
    }

    @Test
    @Transactional
    public void getAllBoardsByTradeValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where tradeValue is greater than DEFAULT_TRADE_VALUE
        defaultBoardShouldNotBeFound("tradeValue.greaterThan=" + DEFAULT_TRADE_VALUE);

        // Get all the boardList where tradeValue is greater than SMALLER_TRADE_VALUE
        defaultBoardShouldBeFound("tradeValue.greaterThan=" + SMALLER_TRADE_VALUE);
    }


    @Test
    @Transactional
    public void getAllBoardsByAskPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askPrice equals to DEFAULT_ASK_PRICE
        defaultBoardShouldBeFound("askPrice.equals=" + DEFAULT_ASK_PRICE);

        // Get all the boardList where askPrice equals to UPDATED_ASK_PRICE
        defaultBoardShouldNotBeFound("askPrice.equals=" + UPDATED_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllBoardsByAskPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askPrice not equals to DEFAULT_ASK_PRICE
        defaultBoardShouldNotBeFound("askPrice.notEquals=" + DEFAULT_ASK_PRICE);

        // Get all the boardList where askPrice not equals to UPDATED_ASK_PRICE
        defaultBoardShouldBeFound("askPrice.notEquals=" + UPDATED_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllBoardsByAskPriceIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askPrice in DEFAULT_ASK_PRICE or UPDATED_ASK_PRICE
        defaultBoardShouldBeFound("askPrice.in=" + DEFAULT_ASK_PRICE + "," + UPDATED_ASK_PRICE);

        // Get all the boardList where askPrice equals to UPDATED_ASK_PRICE
        defaultBoardShouldNotBeFound("askPrice.in=" + UPDATED_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllBoardsByAskPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askPrice is not null
        defaultBoardShouldBeFound("askPrice.specified=true");

        // Get all the boardList where askPrice is null
        defaultBoardShouldNotBeFound("askPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByAskPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askPrice is greater than or equal to DEFAULT_ASK_PRICE
        defaultBoardShouldBeFound("askPrice.greaterThanOrEqual=" + DEFAULT_ASK_PRICE);

        // Get all the boardList where askPrice is greater than or equal to UPDATED_ASK_PRICE
        defaultBoardShouldNotBeFound("askPrice.greaterThanOrEqual=" + UPDATED_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllBoardsByAskPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askPrice is less than or equal to DEFAULT_ASK_PRICE
        defaultBoardShouldBeFound("askPrice.lessThanOrEqual=" + DEFAULT_ASK_PRICE);

        // Get all the boardList where askPrice is less than or equal to SMALLER_ASK_PRICE
        defaultBoardShouldNotBeFound("askPrice.lessThanOrEqual=" + SMALLER_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllBoardsByAskPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askPrice is less than DEFAULT_ASK_PRICE
        defaultBoardShouldNotBeFound("askPrice.lessThan=" + DEFAULT_ASK_PRICE);

        // Get all the boardList where askPrice is less than UPDATED_ASK_PRICE
        defaultBoardShouldBeFound("askPrice.lessThan=" + UPDATED_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllBoardsByAskPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askPrice is greater than DEFAULT_ASK_PRICE
        defaultBoardShouldNotBeFound("askPrice.greaterThan=" + DEFAULT_ASK_PRICE);

        // Get all the boardList where askPrice is greater than SMALLER_ASK_PRICE
        defaultBoardShouldBeFound("askPrice.greaterThan=" + SMALLER_ASK_PRICE);
    }


    @Test
    @Transactional
    public void getAllBoardsByAskVolumeIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askVolume equals to DEFAULT_ASK_VOLUME
        defaultBoardShouldBeFound("askVolume.equals=" + DEFAULT_ASK_VOLUME);

        // Get all the boardList where askVolume equals to UPDATED_ASK_VOLUME
        defaultBoardShouldNotBeFound("askVolume.equals=" + UPDATED_ASK_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByAskVolumeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askVolume not equals to DEFAULT_ASK_VOLUME
        defaultBoardShouldNotBeFound("askVolume.notEquals=" + DEFAULT_ASK_VOLUME);

        // Get all the boardList where askVolume not equals to UPDATED_ASK_VOLUME
        defaultBoardShouldBeFound("askVolume.notEquals=" + UPDATED_ASK_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByAskVolumeIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askVolume in DEFAULT_ASK_VOLUME or UPDATED_ASK_VOLUME
        defaultBoardShouldBeFound("askVolume.in=" + DEFAULT_ASK_VOLUME + "," + UPDATED_ASK_VOLUME);

        // Get all the boardList where askVolume equals to UPDATED_ASK_VOLUME
        defaultBoardShouldNotBeFound("askVolume.in=" + UPDATED_ASK_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByAskVolumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askVolume is not null
        defaultBoardShouldBeFound("askVolume.specified=true");

        // Get all the boardList where askVolume is null
        defaultBoardShouldNotBeFound("askVolume.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByAskVolumeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askVolume is greater than or equal to DEFAULT_ASK_VOLUME
        defaultBoardShouldBeFound("askVolume.greaterThanOrEqual=" + DEFAULT_ASK_VOLUME);

        // Get all the boardList where askVolume is greater than or equal to UPDATED_ASK_VOLUME
        defaultBoardShouldNotBeFound("askVolume.greaterThanOrEqual=" + UPDATED_ASK_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByAskVolumeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askVolume is less than or equal to DEFAULT_ASK_VOLUME
        defaultBoardShouldBeFound("askVolume.lessThanOrEqual=" + DEFAULT_ASK_VOLUME);

        // Get all the boardList where askVolume is less than or equal to SMALLER_ASK_VOLUME
        defaultBoardShouldNotBeFound("askVolume.lessThanOrEqual=" + SMALLER_ASK_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByAskVolumeIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askVolume is less than DEFAULT_ASK_VOLUME
        defaultBoardShouldNotBeFound("askVolume.lessThan=" + DEFAULT_ASK_VOLUME);

        // Get all the boardList where askVolume is less than UPDATED_ASK_VOLUME
        defaultBoardShouldBeFound("askVolume.lessThan=" + UPDATED_ASK_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByAskVolumeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where askVolume is greater than DEFAULT_ASK_VOLUME
        defaultBoardShouldNotBeFound("askVolume.greaterThan=" + DEFAULT_ASK_VOLUME);

        // Get all the boardList where askVolume is greater than SMALLER_ASK_VOLUME
        defaultBoardShouldBeFound("askVolume.greaterThan=" + SMALLER_ASK_VOLUME);
    }


    @Test
    @Transactional
    public void getAllBoardsByBidPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidPrice equals to DEFAULT_BID_PRICE
        defaultBoardShouldBeFound("bidPrice.equals=" + DEFAULT_BID_PRICE);

        // Get all the boardList where bidPrice equals to UPDATED_BID_PRICE
        defaultBoardShouldNotBeFound("bidPrice.equals=" + UPDATED_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllBoardsByBidPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidPrice not equals to DEFAULT_BID_PRICE
        defaultBoardShouldNotBeFound("bidPrice.notEquals=" + DEFAULT_BID_PRICE);

        // Get all the boardList where bidPrice not equals to UPDATED_BID_PRICE
        defaultBoardShouldBeFound("bidPrice.notEquals=" + UPDATED_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllBoardsByBidPriceIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidPrice in DEFAULT_BID_PRICE or UPDATED_BID_PRICE
        defaultBoardShouldBeFound("bidPrice.in=" + DEFAULT_BID_PRICE + "," + UPDATED_BID_PRICE);

        // Get all the boardList where bidPrice equals to UPDATED_BID_PRICE
        defaultBoardShouldNotBeFound("bidPrice.in=" + UPDATED_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllBoardsByBidPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidPrice is not null
        defaultBoardShouldBeFound("bidPrice.specified=true");

        // Get all the boardList where bidPrice is null
        defaultBoardShouldNotBeFound("bidPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByBidPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidPrice is greater than or equal to DEFAULT_BID_PRICE
        defaultBoardShouldBeFound("bidPrice.greaterThanOrEqual=" + DEFAULT_BID_PRICE);

        // Get all the boardList where bidPrice is greater than or equal to UPDATED_BID_PRICE
        defaultBoardShouldNotBeFound("bidPrice.greaterThanOrEqual=" + UPDATED_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllBoardsByBidPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidPrice is less than or equal to DEFAULT_BID_PRICE
        defaultBoardShouldBeFound("bidPrice.lessThanOrEqual=" + DEFAULT_BID_PRICE);

        // Get all the boardList where bidPrice is less than or equal to SMALLER_BID_PRICE
        defaultBoardShouldNotBeFound("bidPrice.lessThanOrEqual=" + SMALLER_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllBoardsByBidPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidPrice is less than DEFAULT_BID_PRICE
        defaultBoardShouldNotBeFound("bidPrice.lessThan=" + DEFAULT_BID_PRICE);

        // Get all the boardList where bidPrice is less than UPDATED_BID_PRICE
        defaultBoardShouldBeFound("bidPrice.lessThan=" + UPDATED_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllBoardsByBidPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidPrice is greater than DEFAULT_BID_PRICE
        defaultBoardShouldNotBeFound("bidPrice.greaterThan=" + DEFAULT_BID_PRICE);

        // Get all the boardList where bidPrice is greater than SMALLER_BID_PRICE
        defaultBoardShouldBeFound("bidPrice.greaterThan=" + SMALLER_BID_PRICE);
    }


    @Test
    @Transactional
    public void getAllBoardsByBidVolumeIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidVolume equals to DEFAULT_BID_VOLUME
        defaultBoardShouldBeFound("bidVolume.equals=" + DEFAULT_BID_VOLUME);

        // Get all the boardList where bidVolume equals to UPDATED_BID_VOLUME
        defaultBoardShouldNotBeFound("bidVolume.equals=" + UPDATED_BID_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByBidVolumeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidVolume not equals to DEFAULT_BID_VOLUME
        defaultBoardShouldNotBeFound("bidVolume.notEquals=" + DEFAULT_BID_VOLUME);

        // Get all the boardList where bidVolume not equals to UPDATED_BID_VOLUME
        defaultBoardShouldBeFound("bidVolume.notEquals=" + UPDATED_BID_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByBidVolumeIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidVolume in DEFAULT_BID_VOLUME or UPDATED_BID_VOLUME
        defaultBoardShouldBeFound("bidVolume.in=" + DEFAULT_BID_VOLUME + "," + UPDATED_BID_VOLUME);

        // Get all the boardList where bidVolume equals to UPDATED_BID_VOLUME
        defaultBoardShouldNotBeFound("bidVolume.in=" + UPDATED_BID_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByBidVolumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidVolume is not null
        defaultBoardShouldBeFound("bidVolume.specified=true");

        // Get all the boardList where bidVolume is null
        defaultBoardShouldNotBeFound("bidVolume.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByBidVolumeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidVolume is greater than or equal to DEFAULT_BID_VOLUME
        defaultBoardShouldBeFound("bidVolume.greaterThanOrEqual=" + DEFAULT_BID_VOLUME);

        // Get all the boardList where bidVolume is greater than or equal to UPDATED_BID_VOLUME
        defaultBoardShouldNotBeFound("bidVolume.greaterThanOrEqual=" + UPDATED_BID_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByBidVolumeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidVolume is less than or equal to DEFAULT_BID_VOLUME
        defaultBoardShouldBeFound("bidVolume.lessThanOrEqual=" + DEFAULT_BID_VOLUME);

        // Get all the boardList where bidVolume is less than or equal to SMALLER_BID_VOLUME
        defaultBoardShouldNotBeFound("bidVolume.lessThanOrEqual=" + SMALLER_BID_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByBidVolumeIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidVolume is less than DEFAULT_BID_VOLUME
        defaultBoardShouldNotBeFound("bidVolume.lessThan=" + DEFAULT_BID_VOLUME);

        // Get all the boardList where bidVolume is less than UPDATED_BID_VOLUME
        defaultBoardShouldBeFound("bidVolume.lessThan=" + UPDATED_BID_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByBidVolumeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where bidVolume is greater than DEFAULT_BID_VOLUME
        defaultBoardShouldNotBeFound("bidVolume.greaterThan=" + DEFAULT_BID_VOLUME);

        // Get all the boardList where bidVolume is greater than SMALLER_BID_VOLUME
        defaultBoardShouldBeFound("bidVolume.greaterThan=" + SMALLER_BID_VOLUME);
    }


    @Test
    @Transactional
    public void getAllBoardsByIndividualBuyVolumeIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualBuyVolume equals to DEFAULT_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldBeFound("individualBuyVolume.equals=" + DEFAULT_INDIVIDUAL_BUY_VOLUME);

        // Get all the boardList where individualBuyVolume equals to UPDATED_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("individualBuyVolume.equals=" + UPDATED_INDIVIDUAL_BUY_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualBuyVolumeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualBuyVolume not equals to DEFAULT_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("individualBuyVolume.notEquals=" + DEFAULT_INDIVIDUAL_BUY_VOLUME);

        // Get all the boardList where individualBuyVolume not equals to UPDATED_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldBeFound("individualBuyVolume.notEquals=" + UPDATED_INDIVIDUAL_BUY_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualBuyVolumeIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualBuyVolume in DEFAULT_INDIVIDUAL_BUY_VOLUME or UPDATED_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldBeFound("individualBuyVolume.in=" + DEFAULT_INDIVIDUAL_BUY_VOLUME + "," + UPDATED_INDIVIDUAL_BUY_VOLUME);

        // Get all the boardList where individualBuyVolume equals to UPDATED_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("individualBuyVolume.in=" + UPDATED_INDIVIDUAL_BUY_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualBuyVolumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualBuyVolume is not null
        defaultBoardShouldBeFound("individualBuyVolume.specified=true");

        // Get all the boardList where individualBuyVolume is null
        defaultBoardShouldNotBeFound("individualBuyVolume.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualBuyVolumeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualBuyVolume is greater than or equal to DEFAULT_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldBeFound("individualBuyVolume.greaterThanOrEqual=" + DEFAULT_INDIVIDUAL_BUY_VOLUME);

        // Get all the boardList where individualBuyVolume is greater than or equal to UPDATED_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("individualBuyVolume.greaterThanOrEqual=" + UPDATED_INDIVIDUAL_BUY_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualBuyVolumeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualBuyVolume is less than or equal to DEFAULT_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldBeFound("individualBuyVolume.lessThanOrEqual=" + DEFAULT_INDIVIDUAL_BUY_VOLUME);

        // Get all the boardList where individualBuyVolume is less than or equal to SMALLER_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("individualBuyVolume.lessThanOrEqual=" + SMALLER_INDIVIDUAL_BUY_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualBuyVolumeIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualBuyVolume is less than DEFAULT_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("individualBuyVolume.lessThan=" + DEFAULT_INDIVIDUAL_BUY_VOLUME);

        // Get all the boardList where individualBuyVolume is less than UPDATED_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldBeFound("individualBuyVolume.lessThan=" + UPDATED_INDIVIDUAL_BUY_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualBuyVolumeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualBuyVolume is greater than DEFAULT_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("individualBuyVolume.greaterThan=" + DEFAULT_INDIVIDUAL_BUY_VOLUME);

        // Get all the boardList where individualBuyVolume is greater than SMALLER_INDIVIDUAL_BUY_VOLUME
        defaultBoardShouldBeFound("individualBuyVolume.greaterThan=" + SMALLER_INDIVIDUAL_BUY_VOLUME);
    }


    @Test
    @Transactional
    public void getAllBoardsByIndividualSellVolumeIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualSellVolume equals to DEFAULT_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldBeFound("individualSellVolume.equals=" + DEFAULT_INDIVIDUAL_SELL_VOLUME);

        // Get all the boardList where individualSellVolume equals to UPDATED_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("individualSellVolume.equals=" + UPDATED_INDIVIDUAL_SELL_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualSellVolumeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualSellVolume not equals to DEFAULT_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("individualSellVolume.notEquals=" + DEFAULT_INDIVIDUAL_SELL_VOLUME);

        // Get all the boardList where individualSellVolume not equals to UPDATED_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldBeFound("individualSellVolume.notEquals=" + UPDATED_INDIVIDUAL_SELL_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualSellVolumeIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualSellVolume in DEFAULT_INDIVIDUAL_SELL_VOLUME or UPDATED_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldBeFound("individualSellVolume.in=" + DEFAULT_INDIVIDUAL_SELL_VOLUME + "," + UPDATED_INDIVIDUAL_SELL_VOLUME);

        // Get all the boardList where individualSellVolume equals to UPDATED_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("individualSellVolume.in=" + UPDATED_INDIVIDUAL_SELL_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualSellVolumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualSellVolume is not null
        defaultBoardShouldBeFound("individualSellVolume.specified=true");

        // Get all the boardList where individualSellVolume is null
        defaultBoardShouldNotBeFound("individualSellVolume.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualSellVolumeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualSellVolume is greater than or equal to DEFAULT_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldBeFound("individualSellVolume.greaterThanOrEqual=" + DEFAULT_INDIVIDUAL_SELL_VOLUME);

        // Get all the boardList where individualSellVolume is greater than or equal to UPDATED_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("individualSellVolume.greaterThanOrEqual=" + UPDATED_INDIVIDUAL_SELL_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualSellVolumeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualSellVolume is less than or equal to DEFAULT_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldBeFound("individualSellVolume.lessThanOrEqual=" + DEFAULT_INDIVIDUAL_SELL_VOLUME);

        // Get all the boardList where individualSellVolume is less than or equal to SMALLER_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("individualSellVolume.lessThanOrEqual=" + SMALLER_INDIVIDUAL_SELL_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualSellVolumeIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualSellVolume is less than DEFAULT_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("individualSellVolume.lessThan=" + DEFAULT_INDIVIDUAL_SELL_VOLUME);

        // Get all the boardList where individualSellVolume is less than UPDATED_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldBeFound("individualSellVolume.lessThan=" + UPDATED_INDIVIDUAL_SELL_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByIndividualSellVolumeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where individualSellVolume is greater than DEFAULT_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("individualSellVolume.greaterThan=" + DEFAULT_INDIVIDUAL_SELL_VOLUME);

        // Get all the boardList where individualSellVolume is greater than SMALLER_INDIVIDUAL_SELL_VOLUME
        defaultBoardShouldBeFound("individualSellVolume.greaterThan=" + SMALLER_INDIVIDUAL_SELL_VOLUME);
    }


    @Test
    @Transactional
    public void getAllBoardsByLegalBuyVolumeIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalBuyVolume equals to DEFAULT_LEGAL_BUY_VOLUME
        defaultBoardShouldBeFound("legalBuyVolume.equals=" + DEFAULT_LEGAL_BUY_VOLUME);

        // Get all the boardList where legalBuyVolume equals to UPDATED_LEGAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("legalBuyVolume.equals=" + UPDATED_LEGAL_BUY_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalBuyVolumeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalBuyVolume not equals to DEFAULT_LEGAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("legalBuyVolume.notEquals=" + DEFAULT_LEGAL_BUY_VOLUME);

        // Get all the boardList where legalBuyVolume not equals to UPDATED_LEGAL_BUY_VOLUME
        defaultBoardShouldBeFound("legalBuyVolume.notEquals=" + UPDATED_LEGAL_BUY_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalBuyVolumeIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalBuyVolume in DEFAULT_LEGAL_BUY_VOLUME or UPDATED_LEGAL_BUY_VOLUME
        defaultBoardShouldBeFound("legalBuyVolume.in=" + DEFAULT_LEGAL_BUY_VOLUME + "," + UPDATED_LEGAL_BUY_VOLUME);

        // Get all the boardList where legalBuyVolume equals to UPDATED_LEGAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("legalBuyVolume.in=" + UPDATED_LEGAL_BUY_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalBuyVolumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalBuyVolume is not null
        defaultBoardShouldBeFound("legalBuyVolume.specified=true");

        // Get all the boardList where legalBuyVolume is null
        defaultBoardShouldNotBeFound("legalBuyVolume.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalBuyVolumeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalBuyVolume is greater than or equal to DEFAULT_LEGAL_BUY_VOLUME
        defaultBoardShouldBeFound("legalBuyVolume.greaterThanOrEqual=" + DEFAULT_LEGAL_BUY_VOLUME);

        // Get all the boardList where legalBuyVolume is greater than or equal to UPDATED_LEGAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("legalBuyVolume.greaterThanOrEqual=" + UPDATED_LEGAL_BUY_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalBuyVolumeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalBuyVolume is less than or equal to DEFAULT_LEGAL_BUY_VOLUME
        defaultBoardShouldBeFound("legalBuyVolume.lessThanOrEqual=" + DEFAULT_LEGAL_BUY_VOLUME);

        // Get all the boardList where legalBuyVolume is less than or equal to SMALLER_LEGAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("legalBuyVolume.lessThanOrEqual=" + SMALLER_LEGAL_BUY_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalBuyVolumeIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalBuyVolume is less than DEFAULT_LEGAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("legalBuyVolume.lessThan=" + DEFAULT_LEGAL_BUY_VOLUME);

        // Get all the boardList where legalBuyVolume is less than UPDATED_LEGAL_BUY_VOLUME
        defaultBoardShouldBeFound("legalBuyVolume.lessThan=" + UPDATED_LEGAL_BUY_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalBuyVolumeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalBuyVolume is greater than DEFAULT_LEGAL_BUY_VOLUME
        defaultBoardShouldNotBeFound("legalBuyVolume.greaterThan=" + DEFAULT_LEGAL_BUY_VOLUME);

        // Get all the boardList where legalBuyVolume is greater than SMALLER_LEGAL_BUY_VOLUME
        defaultBoardShouldBeFound("legalBuyVolume.greaterThan=" + SMALLER_LEGAL_BUY_VOLUME);
    }


    @Test
    @Transactional
    public void getAllBoardsByLegalSellVolumeIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalSellVolume equals to DEFAULT_LEGAL_SELL_VOLUME
        defaultBoardShouldBeFound("legalSellVolume.equals=" + DEFAULT_LEGAL_SELL_VOLUME);

        // Get all the boardList where legalSellVolume equals to UPDATED_LEGAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("legalSellVolume.equals=" + UPDATED_LEGAL_SELL_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalSellVolumeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalSellVolume not equals to DEFAULT_LEGAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("legalSellVolume.notEquals=" + DEFAULT_LEGAL_SELL_VOLUME);

        // Get all the boardList where legalSellVolume not equals to UPDATED_LEGAL_SELL_VOLUME
        defaultBoardShouldBeFound("legalSellVolume.notEquals=" + UPDATED_LEGAL_SELL_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalSellVolumeIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalSellVolume in DEFAULT_LEGAL_SELL_VOLUME or UPDATED_LEGAL_SELL_VOLUME
        defaultBoardShouldBeFound("legalSellVolume.in=" + DEFAULT_LEGAL_SELL_VOLUME + "," + UPDATED_LEGAL_SELL_VOLUME);

        // Get all the boardList where legalSellVolume equals to UPDATED_LEGAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("legalSellVolume.in=" + UPDATED_LEGAL_SELL_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalSellVolumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalSellVolume is not null
        defaultBoardShouldBeFound("legalSellVolume.specified=true");

        // Get all the boardList where legalSellVolume is null
        defaultBoardShouldNotBeFound("legalSellVolume.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalSellVolumeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalSellVolume is greater than or equal to DEFAULT_LEGAL_SELL_VOLUME
        defaultBoardShouldBeFound("legalSellVolume.greaterThanOrEqual=" + DEFAULT_LEGAL_SELL_VOLUME);

        // Get all the boardList where legalSellVolume is greater than or equal to UPDATED_LEGAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("legalSellVolume.greaterThanOrEqual=" + UPDATED_LEGAL_SELL_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalSellVolumeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalSellVolume is less than or equal to DEFAULT_LEGAL_SELL_VOLUME
        defaultBoardShouldBeFound("legalSellVolume.lessThanOrEqual=" + DEFAULT_LEGAL_SELL_VOLUME);

        // Get all the boardList where legalSellVolume is less than or equal to SMALLER_LEGAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("legalSellVolume.lessThanOrEqual=" + SMALLER_LEGAL_SELL_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalSellVolumeIsLessThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalSellVolume is less than DEFAULT_LEGAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("legalSellVolume.lessThan=" + DEFAULT_LEGAL_SELL_VOLUME);

        // Get all the boardList where legalSellVolume is less than UPDATED_LEGAL_SELL_VOLUME
        defaultBoardShouldBeFound("legalSellVolume.lessThan=" + UPDATED_LEGAL_SELL_VOLUME);
    }

    @Test
    @Transactional
    public void getAllBoardsByLegalSellVolumeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList where legalSellVolume is greater than DEFAULT_LEGAL_SELL_VOLUME
        defaultBoardShouldNotBeFound("legalSellVolume.greaterThan=" + DEFAULT_LEGAL_SELL_VOLUME);

        // Get all the boardList where legalSellVolume is greater than SMALLER_LEGAL_SELL_VOLUME
        defaultBoardShouldBeFound("legalSellVolume.greaterThan=" + SMALLER_LEGAL_SELL_VOLUME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBoardShouldBeFound(String filter) throws Exception {
        restBoardMockMvc.perform(get("/api/boards?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].isin").value(hasItem(DEFAULT_ISIN)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].last").value(hasItem(DEFAULT_LAST)))
            .andExpect(jsonPath("$.[*].close").value(hasItem(DEFAULT_CLOSE)))
            .andExpect(jsonPath("$.[*].first").value(hasItem(DEFAULT_FIRST)))
            .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW)))
            .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH)))
            .andExpect(jsonPath("$.[*].tradeCount").value(hasItem(DEFAULT_TRADE_COUNT)))
            .andExpect(jsonPath("$.[*].tradeVolume").value(hasItem(DEFAULT_TRADE_VOLUME.intValue())))
            .andExpect(jsonPath("$.[*].tradeValue").value(hasItem(DEFAULT_TRADE_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].askPrice").value(hasItem(DEFAULT_ASK_PRICE)))
            .andExpect(jsonPath("$.[*].askVolume").value(hasItem(DEFAULT_ASK_VOLUME)))
            .andExpect(jsonPath("$.[*].bidPrice").value(hasItem(DEFAULT_BID_PRICE)))
            .andExpect(jsonPath("$.[*].bidVolume").value(hasItem(DEFAULT_BID_VOLUME)))
            .andExpect(jsonPath("$.[*].individualBuyVolume").value(hasItem(DEFAULT_INDIVIDUAL_BUY_VOLUME)))
            .andExpect(jsonPath("$.[*].individualSellVolume").value(hasItem(DEFAULT_INDIVIDUAL_SELL_VOLUME)))
            .andExpect(jsonPath("$.[*].legalBuyVolume").value(hasItem(DEFAULT_LEGAL_BUY_VOLUME)))
            .andExpect(jsonPath("$.[*].legalSellVolume").value(hasItem(DEFAULT_LEGAL_SELL_VOLUME)));

        // Check, that the count call also returns 1
        restBoardMockMvc.perform(get("/api/boards/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBoardShouldNotBeFound(String filter) throws Exception {
        restBoardMockMvc.perform(get("/api/boards?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBoardMockMvc.perform(get("/api/boards/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBoard() throws Exception {
        // Get the board
        restBoardMockMvc.perform(get("/api/boards/{id}", "Long.MAX_VALUE"))
            .andExpect(status().isNotFound());
    }

}
