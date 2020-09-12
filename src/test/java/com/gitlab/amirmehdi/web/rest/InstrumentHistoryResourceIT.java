package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.ETradeApp;
import com.gitlab.amirmehdi.domain.InstrumentHistory;
import com.gitlab.amirmehdi.repository.InstrumentHistoryRepository;
import com.gitlab.amirmehdi.service.InstrumentHistoryService;
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
import java.time.LocalDate;
import java.time.ZoneId;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InstrumentHistoryResource} REST controller.
 */
@SpringBootTest(classes = ETradeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class InstrumentHistoryResourceIT {

    private static final String DEFAULT_ISIN = "AAAAAAAAAA";
    private static final String UPDATED_ISIN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_LAST = 1;
    private static final Integer UPDATED_LAST = 2;

    private static final Integer DEFAULT_CLOSE = 1;
    private static final Integer UPDATED_CLOSE = 2;

    private static final Integer DEFAULT_FIRST = 1;
    private static final Integer UPDATED_FIRST = 2;

    private static final Integer DEFAULT_REFERENCE_PRICE = 1;
    private static final Integer UPDATED_REFERENCE_PRICE = 2;

    private static final Integer DEFAULT_LOW = 1;
    private static final Integer UPDATED_LOW = 2;

    private static final Integer DEFAULT_HIGH = 1;
    private static final Integer UPDATED_HIGH = 2;

    private static final Long DEFAULT_TRADE_VOLUME = 1L;
    private static final Long UPDATED_TRADE_VOLUME = 2L;

    private static final Integer DEFAULT_TRADE_COUNT = 1;
    private static final Integer UPDATED_TRADE_COUNT = 2;

    private static final Long DEFAULT_TRADE_VALUE = 1L;
    private static final Long UPDATED_TRADE_VALUE = 2L;

    @Autowired
    private InstrumentHistoryRepository instrumentHistoryRepository;

    @Autowired
    private InstrumentHistoryService instrumentHistoryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInstrumentHistoryMockMvc;

    private InstrumentHistory instrumentHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstrumentHistory createEntity(EntityManager em) {
        InstrumentHistory instrumentHistory = new InstrumentHistory()
            .isin(DEFAULT_ISIN)
            .date(DEFAULT_DATE)
            .last(DEFAULT_LAST)
            .close(DEFAULT_CLOSE)
            .first(DEFAULT_FIRST)
            .referencePrice(DEFAULT_REFERENCE_PRICE)
            .low(DEFAULT_LOW)
            .high(DEFAULT_HIGH)
            .tradeVolume(DEFAULT_TRADE_VOLUME)
            .tradeCount(DEFAULT_TRADE_COUNT)
            .tradeValue(DEFAULT_TRADE_VALUE);
        return instrumentHistory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstrumentHistory createUpdatedEntity(EntityManager em) {
        InstrumentHistory instrumentHistory = new InstrumentHistory()
            .isin(UPDATED_ISIN)
            .date(UPDATED_DATE)
            .last(UPDATED_LAST)
            .close(UPDATED_CLOSE)
            .first(UPDATED_FIRST)
            .referencePrice(UPDATED_REFERENCE_PRICE)
            .low(UPDATED_LOW)
            .high(UPDATED_HIGH)
            .tradeVolume(UPDATED_TRADE_VOLUME)
            .tradeCount(UPDATED_TRADE_COUNT)
            .tradeValue(UPDATED_TRADE_VALUE);
        return instrumentHistory;
    }

    @BeforeEach
    public void initTest() {
        instrumentHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllInstrumentHistories() throws Exception {
        // Initialize the database
        instrumentHistoryRepository.saveAndFlush(instrumentHistory);

        // Get all the instrumentHistoryList
        restInstrumentHistoryMockMvc.perform(get("/api/instrument-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instrumentHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].isin").value(hasItem(DEFAULT_ISIN)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].last").value(hasItem(DEFAULT_LAST)))
            .andExpect(jsonPath("$.[*].close").value(hasItem(DEFAULT_CLOSE)))
            .andExpect(jsonPath("$.[*].first").value(hasItem(DEFAULT_FIRST)))
            .andExpect(jsonPath("$.[*].referencePrice").value(hasItem(DEFAULT_REFERENCE_PRICE)))
            .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW)))
            .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH)))
            .andExpect(jsonPath("$.[*].tradeVolume").value(hasItem(DEFAULT_TRADE_VOLUME.intValue())))
            .andExpect(jsonPath("$.[*].tradeCount").value(hasItem(DEFAULT_TRADE_COUNT)))
            .andExpect(jsonPath("$.[*].tradeValue").value(hasItem(DEFAULT_TRADE_VALUE.intValue())));
    }

    @Test
    @Transactional
    public void getInstrumentHistory() throws Exception {
        // Initialize the database
        instrumentHistoryRepository.saveAndFlush(instrumentHistory);

        // Get the instrumentHistory
        restInstrumentHistoryMockMvc.perform(get("/api/instrument-histories/{id}", instrumentHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(instrumentHistory.getId().intValue()))
            .andExpect(jsonPath("$.isin").value(DEFAULT_ISIN))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.last").value(DEFAULT_LAST))
            .andExpect(jsonPath("$.close").value(DEFAULT_CLOSE))
            .andExpect(jsonPath("$.first").value(DEFAULT_FIRST))
            .andExpect(jsonPath("$.referencePrice").value(DEFAULT_REFERENCE_PRICE))
            .andExpect(jsonPath("$.low").value(DEFAULT_LOW))
            .andExpect(jsonPath("$.high").value(DEFAULT_HIGH))
            .andExpect(jsonPath("$.tradeVolume").value(DEFAULT_TRADE_VOLUME.intValue()))
            .andExpect(jsonPath("$.tradeCount").value(DEFAULT_TRADE_COUNT))
            .andExpect(jsonPath("$.tradeValue").value(DEFAULT_TRADE_VALUE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInstrumentHistory() throws Exception {
        // Get the instrumentHistory
        restInstrumentHistoryMockMvc.perform(get("/api/instrument-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
