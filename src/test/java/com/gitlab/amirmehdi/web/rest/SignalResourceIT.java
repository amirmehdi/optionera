package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.ETradeApp;
import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.Signal;
import com.gitlab.amirmehdi.repository.SignalRepository;
import com.gitlab.amirmehdi.service.SignalQueryService;
import com.gitlab.amirmehdi.service.SignalService;
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
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SignalResource} REST controller.
 */
@SpringBootTest(classes = ETradeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SignalResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ISIN = "AAAAAAAAAA";
    private static final String UPDATED_ISIN = "BBBBBBBBBB";

    private static final Integer DEFAULT_LAST = 1;
    private static final Integer UPDATED_LAST = 2;
    private static final Integer SMALLER_LAST = 1 - 1;

    private static final Long DEFAULT_TRADE_VOLUME = 1L;
    private static final Long UPDATED_TRADE_VOLUME = 2L;
    private static final Long SMALLER_TRADE_VOLUME = 1L - 1L;

    private static final Long DEFAULT_BID_VOLUME = 1L;
    private static final Long UPDATED_BID_VOLUME = 2L;
    private static final Long SMALLER_BID_VOLUME = 1L - 1L;

    private static final Integer DEFAULT_BID_PRICE = 1;
    private static final Integer UPDATED_BID_PRICE = 2;
    private static final Integer SMALLER_BID_PRICE = 1 - 1;

    private static final Integer DEFAULT_ASK_PRICE = 1;
    private static final Integer UPDATED_ASK_PRICE = 2;
    private static final Integer SMALLER_ASK_PRICE = 1 - 1;

    private static final Long DEFAULT_ASK_VOLUME = 1L;
    private static final Long UPDATED_ASK_VOLUME = 2L;
    private static final Long SMALLER_ASK_VOLUME = 1L - 1L;

    private static final Integer DEFAULT_BASE_INSTRUMENT_LAST = 1;
    private static final Integer UPDATED_BASE_INSTRUMENT_LAST = 2;
    private static final Integer SMALLER_BASE_INSTRUMENT_LAST = 1 - 1;

    private static final Date DEFAULT_CREATED_AT = new Date();
    private static final Date UPDATED_CREATED_AT = new Date();

    @Autowired
    private SignalRepository signalRepository;

    @Autowired
    private SignalService signalService;

    @Autowired
    private SignalQueryService signalQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSignalMockMvc;

    private Signal signal;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Signal createEntity(EntityManager em) {
        Signal signal = new Signal()
            .type(DEFAULT_TYPE)
            .isin(DEFAULT_ISIN)
            .last(DEFAULT_LAST)
            .tradeVolume(DEFAULT_TRADE_VOLUME)
            .bidVolume(DEFAULT_BID_VOLUME)
            .bidPrice(DEFAULT_BID_PRICE)
            .askPrice(DEFAULT_ASK_PRICE)
            .askVolume(DEFAULT_ASK_VOLUME)
            .baseInstrumentLast(DEFAULT_BASE_INSTRUMENT_LAST)
            .createdAt(DEFAULT_CREATED_AT);
        return signal;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Signal createUpdatedEntity(EntityManager em) {
        Signal signal = new Signal()
            .type(UPDATED_TYPE)
            .isin(UPDATED_ISIN)
            .last(UPDATED_LAST)
            .tradeVolume(UPDATED_TRADE_VOLUME)
            .bidVolume(UPDATED_BID_VOLUME)
            .bidPrice(UPDATED_BID_PRICE)
            .askPrice(UPDATED_ASK_PRICE)
            .askVolume(UPDATED_ASK_VOLUME)
            .baseInstrumentLast(UPDATED_BASE_INSTRUMENT_LAST)
            .createdAt(UPDATED_CREATED_AT);
        return signal;
    }

    @BeforeEach
    public void initTest() {
        signal = createEntity(em);
    }

    @Test
    @Transactional
    public void createSignal() throws Exception {
        int databaseSizeBeforeCreate = signalRepository.findAll().size();

        // Create the Signal
        restSignalMockMvc.perform(post("/api/signals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signal)))
            .andExpect(status().isCreated());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeCreate + 1);
        Signal testSignal = signalList.get(signalList.size() - 1);
        assertThat(testSignal.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSignal.getIsin()).isEqualTo(DEFAULT_ISIN);
        assertThat(testSignal.getLast()).isEqualTo(DEFAULT_LAST);
        assertThat(testSignal.getTradeVolume()).isEqualTo(DEFAULT_TRADE_VOLUME);
        assertThat(testSignal.getBidVolume()).isEqualTo(DEFAULT_BID_VOLUME);
        assertThat(testSignal.getBidPrice()).isEqualTo(DEFAULT_BID_PRICE);
        assertThat(testSignal.getAskPrice()).isEqualTo(DEFAULT_ASK_PRICE);
        assertThat(testSignal.getAskVolume()).isEqualTo(DEFAULT_ASK_VOLUME);
        assertThat(testSignal.getBaseInstrumentLast()).isEqualTo(DEFAULT_BASE_INSTRUMENT_LAST);
        assertThat(testSignal.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createSignalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = signalRepository.findAll().size();

        // Create the Signal with an existing ID
        signal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignalMockMvc.perform(post("/api/signals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signal)))
            .andExpect(status().isBadRequest());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = signalRepository.findAll().size();
        // set the field null
        signal.setType(null);

        // Create the Signal, which fails.

        restSignalMockMvc.perform(post("/api/signals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signal)))
            .andExpect(status().isBadRequest());

        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsinIsRequired() throws Exception {
        int databaseSizeBeforeTest = signalRepository.findAll().size();
        // set the field null
        signal.setIsin(null);

        // Create the Signal, which fails.

        restSignalMockMvc.perform(post("/api/signals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signal)))
            .andExpect(status().isBadRequest());

        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSignals() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList
        restSignalMockMvc.perform(get("/api/signals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signal.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].isin").value(hasItem(DEFAULT_ISIN)))
            .andExpect(jsonPath("$.[*].last").value(hasItem(DEFAULT_LAST)))
            .andExpect(jsonPath("$.[*].tradeVolume").value(hasItem(DEFAULT_TRADE_VOLUME.intValue())))
            .andExpect(jsonPath("$.[*].bidVolume").value(hasItem(DEFAULT_BID_VOLUME.intValue())))
            .andExpect(jsonPath("$.[*].bidPrice").value(hasItem(DEFAULT_BID_PRICE)))
            .andExpect(jsonPath("$.[*].askPrice").value(hasItem(DEFAULT_ASK_PRICE)))
            .andExpect(jsonPath("$.[*].askVolume").value(hasItem(DEFAULT_ASK_VOLUME.intValue())))
            .andExpect(jsonPath("$.[*].baseInstrumentLast").value(hasItem(DEFAULT_BASE_INSTRUMENT_LAST)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    public void getSignal() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get the signal
        restSignalMockMvc.perform(get("/api/signals/{id}", signal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(signal.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.isin").value(DEFAULT_ISIN))
            .andExpect(jsonPath("$.last").value(DEFAULT_LAST))
            .andExpect(jsonPath("$.tradeVolume").value(DEFAULT_TRADE_VOLUME.intValue()))
            .andExpect(jsonPath("$.bidVolume").value(DEFAULT_BID_VOLUME.intValue()))
            .andExpect(jsonPath("$.bidPrice").value(DEFAULT_BID_PRICE))
            .andExpect(jsonPath("$.askPrice").value(DEFAULT_ASK_PRICE))
            .andExpect(jsonPath("$.askVolume").value(DEFAULT_ASK_VOLUME.intValue()))
            .andExpect(jsonPath("$.baseInstrumentLast").value(DEFAULT_BASE_INSTRUMENT_LAST))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getSignalsByIdFiltering() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        Long id = signal.getId();

        defaultSignalShouldBeFound("id.equals=" + id);
        defaultSignalShouldNotBeFound("id.notEquals=" + id);

        defaultSignalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSignalShouldNotBeFound("id.greaterThan=" + id);

        defaultSignalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSignalShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSignalsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where type equals to DEFAULT_TYPE
        defaultSignalShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the signalList where type equals to UPDATED_TYPE
        defaultSignalShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSignalsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where type not equals to DEFAULT_TYPE
        defaultSignalShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the signalList where type not equals to UPDATED_TYPE
        defaultSignalShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSignalsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultSignalShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the signalList where type equals to UPDATED_TYPE
        defaultSignalShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSignalsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where type is not null
        defaultSignalShouldBeFound("type.specified=true");

        // Get all the signalList where type is null
        defaultSignalShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignalsByTypeContainsSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where type contains DEFAULT_TYPE
        defaultSignalShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the signalList where type contains UPDATED_TYPE
        defaultSignalShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSignalsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where type does not contain DEFAULT_TYPE
        defaultSignalShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the signalList where type does not contain UPDATED_TYPE
        defaultSignalShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllSignalsByIsinIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where isin equals to DEFAULT_ISIN
        defaultSignalShouldBeFound("isin.equals=" + DEFAULT_ISIN);

        // Get all the signalList where isin equals to UPDATED_ISIN
        defaultSignalShouldNotBeFound("isin.equals=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllSignalsByIsinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where isin not equals to DEFAULT_ISIN
        defaultSignalShouldNotBeFound("isin.notEquals=" + DEFAULT_ISIN);

        // Get all the signalList where isin not equals to UPDATED_ISIN
        defaultSignalShouldBeFound("isin.notEquals=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllSignalsByIsinIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where isin in DEFAULT_ISIN or UPDATED_ISIN
        defaultSignalShouldBeFound("isin.in=" + DEFAULT_ISIN + "," + UPDATED_ISIN);

        // Get all the signalList where isin equals to UPDATED_ISIN
        defaultSignalShouldNotBeFound("isin.in=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllSignalsByIsinIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where isin is not null
        defaultSignalShouldBeFound("isin.specified=true");

        // Get all the signalList where isin is null
        defaultSignalShouldNotBeFound("isin.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignalsByIsinContainsSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where isin contains DEFAULT_ISIN
        defaultSignalShouldBeFound("isin.contains=" + DEFAULT_ISIN);

        // Get all the signalList where isin contains UPDATED_ISIN
        defaultSignalShouldNotBeFound("isin.contains=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllSignalsByIsinNotContainsSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where isin does not contain DEFAULT_ISIN
        defaultSignalShouldNotBeFound("isin.doesNotContain=" + DEFAULT_ISIN);

        // Get all the signalList where isin does not contain UPDATED_ISIN
        defaultSignalShouldBeFound("isin.doesNotContain=" + UPDATED_ISIN);
    }


    @Test
    @Transactional
    public void getAllSignalsByLastIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where last equals to DEFAULT_LAST
        defaultSignalShouldBeFound("last.equals=" + DEFAULT_LAST);

        // Get all the signalList where last equals to UPDATED_LAST
        defaultSignalShouldNotBeFound("last.equals=" + UPDATED_LAST);
    }

    @Test
    @Transactional
    public void getAllSignalsByLastIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where last not equals to DEFAULT_LAST
        defaultSignalShouldNotBeFound("last.notEquals=" + DEFAULT_LAST);

        // Get all the signalList where last not equals to UPDATED_LAST
        defaultSignalShouldBeFound("last.notEquals=" + UPDATED_LAST);
    }

    @Test
    @Transactional
    public void getAllSignalsByLastIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where last in DEFAULT_LAST or UPDATED_LAST
        defaultSignalShouldBeFound("last.in=" + DEFAULT_LAST + "," + UPDATED_LAST);

        // Get all the signalList where last equals to UPDATED_LAST
        defaultSignalShouldNotBeFound("last.in=" + UPDATED_LAST);
    }

    @Test
    @Transactional
    public void getAllSignalsByLastIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where last is not null
        defaultSignalShouldBeFound("last.specified=true");

        // Get all the signalList where last is null
        defaultSignalShouldNotBeFound("last.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignalsByLastIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where last is greater than or equal to DEFAULT_LAST
        defaultSignalShouldBeFound("last.greaterThanOrEqual=" + DEFAULT_LAST);

        // Get all the signalList where last is greater than or equal to UPDATED_LAST
        defaultSignalShouldNotBeFound("last.greaterThanOrEqual=" + UPDATED_LAST);
    }

    @Test
    @Transactional
    public void getAllSignalsByLastIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where last is less than or equal to DEFAULT_LAST
        defaultSignalShouldBeFound("last.lessThanOrEqual=" + DEFAULT_LAST);

        // Get all the signalList where last is less than or equal to SMALLER_LAST
        defaultSignalShouldNotBeFound("last.lessThanOrEqual=" + SMALLER_LAST);
    }

    @Test
    @Transactional
    public void getAllSignalsByLastIsLessThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where last is less than DEFAULT_LAST
        defaultSignalShouldNotBeFound("last.lessThan=" + DEFAULT_LAST);

        // Get all the signalList where last is less than UPDATED_LAST
        defaultSignalShouldBeFound("last.lessThan=" + UPDATED_LAST);
    }

    @Test
    @Transactional
    public void getAllSignalsByLastIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where last is greater than DEFAULT_LAST
        defaultSignalShouldNotBeFound("last.greaterThan=" + DEFAULT_LAST);

        // Get all the signalList where last is greater than SMALLER_LAST
        defaultSignalShouldBeFound("last.greaterThan=" + SMALLER_LAST);
    }


    @Test
    @Transactional
    public void getAllSignalsByTradeVolumeIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where tradeVolume equals to DEFAULT_TRADE_VOLUME
        defaultSignalShouldBeFound("tradeVolume.equals=" + DEFAULT_TRADE_VOLUME);

        // Get all the signalList where tradeVolume equals to UPDATED_TRADE_VOLUME
        defaultSignalShouldNotBeFound("tradeVolume.equals=" + UPDATED_TRADE_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByTradeVolumeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where tradeVolume not equals to DEFAULT_TRADE_VOLUME
        defaultSignalShouldNotBeFound("tradeVolume.notEquals=" + DEFAULT_TRADE_VOLUME);

        // Get all the signalList where tradeVolume not equals to UPDATED_TRADE_VOLUME
        defaultSignalShouldBeFound("tradeVolume.notEquals=" + UPDATED_TRADE_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByTradeVolumeIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where tradeVolume in DEFAULT_TRADE_VOLUME or UPDATED_TRADE_VOLUME
        defaultSignalShouldBeFound("tradeVolume.in=" + DEFAULT_TRADE_VOLUME + "," + UPDATED_TRADE_VOLUME);

        // Get all the signalList where tradeVolume equals to UPDATED_TRADE_VOLUME
        defaultSignalShouldNotBeFound("tradeVolume.in=" + UPDATED_TRADE_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByTradeVolumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where tradeVolume is not null
        defaultSignalShouldBeFound("tradeVolume.specified=true");

        // Get all the signalList where tradeVolume is null
        defaultSignalShouldNotBeFound("tradeVolume.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignalsByTradeVolumeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where tradeVolume is greater than or equal to DEFAULT_TRADE_VOLUME
        defaultSignalShouldBeFound("tradeVolume.greaterThanOrEqual=" + DEFAULT_TRADE_VOLUME);

        // Get all the signalList where tradeVolume is greater than or equal to UPDATED_TRADE_VOLUME
        defaultSignalShouldNotBeFound("tradeVolume.greaterThanOrEqual=" + UPDATED_TRADE_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByTradeVolumeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where tradeVolume is less than or equal to DEFAULT_TRADE_VOLUME
        defaultSignalShouldBeFound("tradeVolume.lessThanOrEqual=" + DEFAULT_TRADE_VOLUME);

        // Get all the signalList where tradeVolume is less than or equal to SMALLER_TRADE_VOLUME
        defaultSignalShouldNotBeFound("tradeVolume.lessThanOrEqual=" + SMALLER_TRADE_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByTradeVolumeIsLessThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where tradeVolume is less than DEFAULT_TRADE_VOLUME
        defaultSignalShouldNotBeFound("tradeVolume.lessThan=" + DEFAULT_TRADE_VOLUME);

        // Get all the signalList where tradeVolume is less than UPDATED_TRADE_VOLUME
        defaultSignalShouldBeFound("tradeVolume.lessThan=" + UPDATED_TRADE_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByTradeVolumeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where tradeVolume is greater than DEFAULT_TRADE_VOLUME
        defaultSignalShouldNotBeFound("tradeVolume.greaterThan=" + DEFAULT_TRADE_VOLUME);

        // Get all the signalList where tradeVolume is greater than SMALLER_TRADE_VOLUME
        defaultSignalShouldBeFound("tradeVolume.greaterThan=" + SMALLER_TRADE_VOLUME);
    }


    @Test
    @Transactional
    public void getAllSignalsByBidVolumeIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidVolume equals to DEFAULT_BID_VOLUME
        defaultSignalShouldBeFound("bidVolume.equals=" + DEFAULT_BID_VOLUME);

        // Get all the signalList where bidVolume equals to UPDATED_BID_VOLUME
        defaultSignalShouldNotBeFound("bidVolume.equals=" + UPDATED_BID_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByBidVolumeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidVolume not equals to DEFAULT_BID_VOLUME
        defaultSignalShouldNotBeFound("bidVolume.notEquals=" + DEFAULT_BID_VOLUME);

        // Get all the signalList where bidVolume not equals to UPDATED_BID_VOLUME
        defaultSignalShouldBeFound("bidVolume.notEquals=" + UPDATED_BID_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByBidVolumeIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidVolume in DEFAULT_BID_VOLUME or UPDATED_BID_VOLUME
        defaultSignalShouldBeFound("bidVolume.in=" + DEFAULT_BID_VOLUME + "," + UPDATED_BID_VOLUME);

        // Get all the signalList where bidVolume equals to UPDATED_BID_VOLUME
        defaultSignalShouldNotBeFound("bidVolume.in=" + UPDATED_BID_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByBidVolumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidVolume is not null
        defaultSignalShouldBeFound("bidVolume.specified=true");

        // Get all the signalList where bidVolume is null
        defaultSignalShouldNotBeFound("bidVolume.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignalsByBidVolumeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidVolume is greater than or equal to DEFAULT_BID_VOLUME
        defaultSignalShouldBeFound("bidVolume.greaterThanOrEqual=" + DEFAULT_BID_VOLUME);

        // Get all the signalList where bidVolume is greater than or equal to UPDATED_BID_VOLUME
        defaultSignalShouldNotBeFound("bidVolume.greaterThanOrEqual=" + UPDATED_BID_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByBidVolumeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidVolume is less than or equal to DEFAULT_BID_VOLUME
        defaultSignalShouldBeFound("bidVolume.lessThanOrEqual=" + DEFAULT_BID_VOLUME);

        // Get all the signalList where bidVolume is less than or equal to SMALLER_BID_VOLUME
        defaultSignalShouldNotBeFound("bidVolume.lessThanOrEqual=" + SMALLER_BID_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByBidVolumeIsLessThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidVolume is less than DEFAULT_BID_VOLUME
        defaultSignalShouldNotBeFound("bidVolume.lessThan=" + DEFAULT_BID_VOLUME);

        // Get all the signalList where bidVolume is less than UPDATED_BID_VOLUME
        defaultSignalShouldBeFound("bidVolume.lessThan=" + UPDATED_BID_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByBidVolumeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidVolume is greater than DEFAULT_BID_VOLUME
        defaultSignalShouldNotBeFound("bidVolume.greaterThan=" + DEFAULT_BID_VOLUME);

        // Get all the signalList where bidVolume is greater than SMALLER_BID_VOLUME
        defaultSignalShouldBeFound("bidVolume.greaterThan=" + SMALLER_BID_VOLUME);
    }


    @Test
    @Transactional
    public void getAllSignalsByBidPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidPrice equals to DEFAULT_BID_PRICE
        defaultSignalShouldBeFound("bidPrice.equals=" + DEFAULT_BID_PRICE);

        // Get all the signalList where bidPrice equals to UPDATED_BID_PRICE
        defaultSignalShouldNotBeFound("bidPrice.equals=" + UPDATED_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllSignalsByBidPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidPrice not equals to DEFAULT_BID_PRICE
        defaultSignalShouldNotBeFound("bidPrice.notEquals=" + DEFAULT_BID_PRICE);

        // Get all the signalList where bidPrice not equals to UPDATED_BID_PRICE
        defaultSignalShouldBeFound("bidPrice.notEquals=" + UPDATED_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllSignalsByBidPriceIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidPrice in DEFAULT_BID_PRICE or UPDATED_BID_PRICE
        defaultSignalShouldBeFound("bidPrice.in=" + DEFAULT_BID_PRICE + "," + UPDATED_BID_PRICE);

        // Get all the signalList where bidPrice equals to UPDATED_BID_PRICE
        defaultSignalShouldNotBeFound("bidPrice.in=" + UPDATED_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllSignalsByBidPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidPrice is not null
        defaultSignalShouldBeFound("bidPrice.specified=true");

        // Get all the signalList where bidPrice is null
        defaultSignalShouldNotBeFound("bidPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignalsByBidPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidPrice is greater than or equal to DEFAULT_BID_PRICE
        defaultSignalShouldBeFound("bidPrice.greaterThanOrEqual=" + DEFAULT_BID_PRICE);

        // Get all the signalList where bidPrice is greater than or equal to UPDATED_BID_PRICE
        defaultSignalShouldNotBeFound("bidPrice.greaterThanOrEqual=" + UPDATED_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllSignalsByBidPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidPrice is less than or equal to DEFAULT_BID_PRICE
        defaultSignalShouldBeFound("bidPrice.lessThanOrEqual=" + DEFAULT_BID_PRICE);

        // Get all the signalList where bidPrice is less than or equal to SMALLER_BID_PRICE
        defaultSignalShouldNotBeFound("bidPrice.lessThanOrEqual=" + SMALLER_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllSignalsByBidPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidPrice is less than DEFAULT_BID_PRICE
        defaultSignalShouldNotBeFound("bidPrice.lessThan=" + DEFAULT_BID_PRICE);

        // Get all the signalList where bidPrice is less than UPDATED_BID_PRICE
        defaultSignalShouldBeFound("bidPrice.lessThan=" + UPDATED_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllSignalsByBidPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where bidPrice is greater than DEFAULT_BID_PRICE
        defaultSignalShouldNotBeFound("bidPrice.greaterThan=" + DEFAULT_BID_PRICE);

        // Get all the signalList where bidPrice is greater than SMALLER_BID_PRICE
        defaultSignalShouldBeFound("bidPrice.greaterThan=" + SMALLER_BID_PRICE);
    }


    @Test
    @Transactional
    public void getAllSignalsByAskPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askPrice equals to DEFAULT_ASK_PRICE
        defaultSignalShouldBeFound("askPrice.equals=" + DEFAULT_ASK_PRICE);

        // Get all the signalList where askPrice equals to UPDATED_ASK_PRICE
        defaultSignalShouldNotBeFound("askPrice.equals=" + UPDATED_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllSignalsByAskPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askPrice not equals to DEFAULT_ASK_PRICE
        defaultSignalShouldNotBeFound("askPrice.notEquals=" + DEFAULT_ASK_PRICE);

        // Get all the signalList where askPrice not equals to UPDATED_ASK_PRICE
        defaultSignalShouldBeFound("askPrice.notEquals=" + UPDATED_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllSignalsByAskPriceIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askPrice in DEFAULT_ASK_PRICE or UPDATED_ASK_PRICE
        defaultSignalShouldBeFound("askPrice.in=" + DEFAULT_ASK_PRICE + "," + UPDATED_ASK_PRICE);

        // Get all the signalList where askPrice equals to UPDATED_ASK_PRICE
        defaultSignalShouldNotBeFound("askPrice.in=" + UPDATED_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllSignalsByAskPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askPrice is not null
        defaultSignalShouldBeFound("askPrice.specified=true");

        // Get all the signalList where askPrice is null
        defaultSignalShouldNotBeFound("askPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignalsByAskPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askPrice is greater than or equal to DEFAULT_ASK_PRICE
        defaultSignalShouldBeFound("askPrice.greaterThanOrEqual=" + DEFAULT_ASK_PRICE);

        // Get all the signalList where askPrice is greater than or equal to UPDATED_ASK_PRICE
        defaultSignalShouldNotBeFound("askPrice.greaterThanOrEqual=" + UPDATED_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllSignalsByAskPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askPrice is less than or equal to DEFAULT_ASK_PRICE
        defaultSignalShouldBeFound("askPrice.lessThanOrEqual=" + DEFAULT_ASK_PRICE);

        // Get all the signalList where askPrice is less than or equal to SMALLER_ASK_PRICE
        defaultSignalShouldNotBeFound("askPrice.lessThanOrEqual=" + SMALLER_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllSignalsByAskPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askPrice is less than DEFAULT_ASK_PRICE
        defaultSignalShouldNotBeFound("askPrice.lessThan=" + DEFAULT_ASK_PRICE);

        // Get all the signalList where askPrice is less than UPDATED_ASK_PRICE
        defaultSignalShouldBeFound("askPrice.lessThan=" + UPDATED_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllSignalsByAskPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askPrice is greater than DEFAULT_ASK_PRICE
        defaultSignalShouldNotBeFound("askPrice.greaterThan=" + DEFAULT_ASK_PRICE);

        // Get all the signalList where askPrice is greater than SMALLER_ASK_PRICE
        defaultSignalShouldBeFound("askPrice.greaterThan=" + SMALLER_ASK_PRICE);
    }


    @Test
    @Transactional
    public void getAllSignalsByAskVolumeIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askVolume equals to DEFAULT_ASK_VOLUME
        defaultSignalShouldBeFound("askVolume.equals=" + DEFAULT_ASK_VOLUME);

        // Get all the signalList where askVolume equals to UPDATED_ASK_VOLUME
        defaultSignalShouldNotBeFound("askVolume.equals=" + UPDATED_ASK_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByAskVolumeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askVolume not equals to DEFAULT_ASK_VOLUME
        defaultSignalShouldNotBeFound("askVolume.notEquals=" + DEFAULT_ASK_VOLUME);

        // Get all the signalList where askVolume not equals to UPDATED_ASK_VOLUME
        defaultSignalShouldBeFound("askVolume.notEquals=" + UPDATED_ASK_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByAskVolumeIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askVolume in DEFAULT_ASK_VOLUME or UPDATED_ASK_VOLUME
        defaultSignalShouldBeFound("askVolume.in=" + DEFAULT_ASK_VOLUME + "," + UPDATED_ASK_VOLUME);

        // Get all the signalList where askVolume equals to UPDATED_ASK_VOLUME
        defaultSignalShouldNotBeFound("askVolume.in=" + UPDATED_ASK_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByAskVolumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askVolume is not null
        defaultSignalShouldBeFound("askVolume.specified=true");

        // Get all the signalList where askVolume is null
        defaultSignalShouldNotBeFound("askVolume.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignalsByAskVolumeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askVolume is greater than or equal to DEFAULT_ASK_VOLUME
        defaultSignalShouldBeFound("askVolume.greaterThanOrEqual=" + DEFAULT_ASK_VOLUME);

        // Get all the signalList where askVolume is greater than or equal to UPDATED_ASK_VOLUME
        defaultSignalShouldNotBeFound("askVolume.greaterThanOrEqual=" + UPDATED_ASK_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByAskVolumeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askVolume is less than or equal to DEFAULT_ASK_VOLUME
        defaultSignalShouldBeFound("askVolume.lessThanOrEqual=" + DEFAULT_ASK_VOLUME);

        // Get all the signalList where askVolume is less than or equal to SMALLER_ASK_VOLUME
        defaultSignalShouldNotBeFound("askVolume.lessThanOrEqual=" + SMALLER_ASK_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByAskVolumeIsLessThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askVolume is less than DEFAULT_ASK_VOLUME
        defaultSignalShouldNotBeFound("askVolume.lessThan=" + DEFAULT_ASK_VOLUME);

        // Get all the signalList where askVolume is less than UPDATED_ASK_VOLUME
        defaultSignalShouldBeFound("askVolume.lessThan=" + UPDATED_ASK_VOLUME);
    }

    @Test
    @Transactional
    public void getAllSignalsByAskVolumeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where askVolume is greater than DEFAULT_ASK_VOLUME
        defaultSignalShouldNotBeFound("askVolume.greaterThan=" + DEFAULT_ASK_VOLUME);

        // Get all the signalList where askVolume is greater than SMALLER_ASK_VOLUME
        defaultSignalShouldBeFound("askVolume.greaterThan=" + SMALLER_ASK_VOLUME);
    }


    @Test
    @Transactional
    public void getAllSignalsByBaseInstrumentLastIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where baseInstrumentLast equals to DEFAULT_BASE_INSTRUMENT_LAST
        defaultSignalShouldBeFound("baseInstrumentLast.equals=" + DEFAULT_BASE_INSTRUMENT_LAST);

        // Get all the signalList where baseInstrumentLast equals to UPDATED_BASE_INSTRUMENT_LAST
        defaultSignalShouldNotBeFound("baseInstrumentLast.equals=" + UPDATED_BASE_INSTRUMENT_LAST);
    }

    @Test
    @Transactional
    public void getAllSignalsByBaseInstrumentLastIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where baseInstrumentLast not equals to DEFAULT_BASE_INSTRUMENT_LAST
        defaultSignalShouldNotBeFound("baseInstrumentLast.notEquals=" + DEFAULT_BASE_INSTRUMENT_LAST);

        // Get all the signalList where baseInstrumentLast not equals to UPDATED_BASE_INSTRUMENT_LAST
        defaultSignalShouldBeFound("baseInstrumentLast.notEquals=" + UPDATED_BASE_INSTRUMENT_LAST);
    }

    @Test
    @Transactional
    public void getAllSignalsByBaseInstrumentLastIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where baseInstrumentLast in DEFAULT_BASE_INSTRUMENT_LAST or UPDATED_BASE_INSTRUMENT_LAST
        defaultSignalShouldBeFound("baseInstrumentLast.in=" + DEFAULT_BASE_INSTRUMENT_LAST + "," + UPDATED_BASE_INSTRUMENT_LAST);

        // Get all the signalList where baseInstrumentLast equals to UPDATED_BASE_INSTRUMENT_LAST
        defaultSignalShouldNotBeFound("baseInstrumentLast.in=" + UPDATED_BASE_INSTRUMENT_LAST);
    }

    @Test
    @Transactional
    public void getAllSignalsByBaseInstrumentLastIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where baseInstrumentLast is not null
        defaultSignalShouldBeFound("baseInstrumentLast.specified=true");

        // Get all the signalList where baseInstrumentLast is null
        defaultSignalShouldNotBeFound("baseInstrumentLast.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignalsByBaseInstrumentLastIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where baseInstrumentLast is greater than or equal to DEFAULT_BASE_INSTRUMENT_LAST
        defaultSignalShouldBeFound("baseInstrumentLast.greaterThanOrEqual=" + DEFAULT_BASE_INSTRUMENT_LAST);

        // Get all the signalList where baseInstrumentLast is greater than or equal to UPDATED_BASE_INSTRUMENT_LAST
        defaultSignalShouldNotBeFound("baseInstrumentLast.greaterThanOrEqual=" + UPDATED_BASE_INSTRUMENT_LAST);
    }

    @Test
    @Transactional
    public void getAllSignalsByBaseInstrumentLastIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where baseInstrumentLast is less than or equal to DEFAULT_BASE_INSTRUMENT_LAST
        defaultSignalShouldBeFound("baseInstrumentLast.lessThanOrEqual=" + DEFAULT_BASE_INSTRUMENT_LAST);

        // Get all the signalList where baseInstrumentLast is less than or equal to SMALLER_BASE_INSTRUMENT_LAST
        defaultSignalShouldNotBeFound("baseInstrumentLast.lessThanOrEqual=" + SMALLER_BASE_INSTRUMENT_LAST);
    }

    @Test
    @Transactional
    public void getAllSignalsByBaseInstrumentLastIsLessThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where baseInstrumentLast is less than DEFAULT_BASE_INSTRUMENT_LAST
        defaultSignalShouldNotBeFound("baseInstrumentLast.lessThan=" + DEFAULT_BASE_INSTRUMENT_LAST);

        // Get all the signalList where baseInstrumentLast is less than UPDATED_BASE_INSTRUMENT_LAST
        defaultSignalShouldBeFound("baseInstrumentLast.lessThan=" + UPDATED_BASE_INSTRUMENT_LAST);
    }

    @Test
    @Transactional
    public void getAllSignalsByBaseInstrumentLastIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where baseInstrumentLast is greater than DEFAULT_BASE_INSTRUMENT_LAST
        defaultSignalShouldNotBeFound("baseInstrumentLast.greaterThan=" + DEFAULT_BASE_INSTRUMENT_LAST);

        // Get all the signalList where baseInstrumentLast is greater than SMALLER_BASE_INSTRUMENT_LAST
        defaultSignalShouldBeFound("baseInstrumentLast.greaterThan=" + SMALLER_BASE_INSTRUMENT_LAST);
    }


    @Test
    @Transactional
    public void getAllSignalsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where createdAt equals to DEFAULT_CREATED_AT
        defaultSignalShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the signalList where createdAt equals to UPDATED_CREATED_AT
        defaultSignalShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllSignalsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where createdAt not equals to DEFAULT_CREATED_AT
        defaultSignalShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the signalList where createdAt not equals to UPDATED_CREATED_AT
        defaultSignalShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllSignalsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultSignalShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the signalList where createdAt equals to UPDATED_CREATED_AT
        defaultSignalShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllSignalsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where createdAt is not null
        defaultSignalShouldBeFound("createdAt.specified=true");

        // Get all the signalList where createdAt is null
        defaultSignalShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignalsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);
        Order order = OrderResourceIT.createEntity(em);
        em.persist(order);
        em.flush();
        signal.addOrder(order);
        signalRepository.saveAndFlush(signal);
        Long orderId = order.getId();

        // Get all the signalList where order equals to orderId
        defaultSignalShouldBeFound("orderId.equals=" + orderId);

        // Get all the signalList where order equals to orderId + 1
        defaultSignalShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSignalShouldBeFound(String filter) throws Exception {
        restSignalMockMvc.perform(get("/api/signals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signal.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].isin").value(hasItem(DEFAULT_ISIN)))
            .andExpect(jsonPath("$.[*].last").value(hasItem(DEFAULT_LAST)))
            .andExpect(jsonPath("$.[*].tradeVolume").value(hasItem(DEFAULT_TRADE_VOLUME.intValue())))
            .andExpect(jsonPath("$.[*].bidVolume").value(hasItem(DEFAULT_BID_VOLUME.intValue())))
            .andExpect(jsonPath("$.[*].bidPrice").value(hasItem(DEFAULT_BID_PRICE)))
            .andExpect(jsonPath("$.[*].askPrice").value(hasItem(DEFAULT_ASK_PRICE)))
            .andExpect(jsonPath("$.[*].askVolume").value(hasItem(DEFAULT_ASK_VOLUME.intValue())))
            .andExpect(jsonPath("$.[*].baseInstrumentLast").value(hasItem(DEFAULT_BASE_INSTRUMENT_LAST)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restSignalMockMvc.perform(get("/api/signals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSignalShouldNotBeFound(String filter) throws Exception {
        restSignalMockMvc.perform(get("/api/signals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSignalMockMvc.perform(get("/api/signals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSignal() throws Exception {
        // Get the signal
        restSignalMockMvc.perform(get("/api/signals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignal() throws Exception {
        // Initialize the database
        signalService.save(signal);

        int databaseSizeBeforeUpdate = signalRepository.findAll().size();

        // Update the signal
        Signal updatedSignal = signalRepository.findById(signal.getId()).get();
        // Disconnect from session so that the updates on updatedSignal are not directly saved in db
        em.detach(updatedSignal);
        updatedSignal
            .type(UPDATED_TYPE)
            .isin(UPDATED_ISIN)
            .last(UPDATED_LAST)
            .tradeVolume(UPDATED_TRADE_VOLUME)
            .bidVolume(UPDATED_BID_VOLUME)
            .bidPrice(UPDATED_BID_PRICE)
            .askPrice(UPDATED_ASK_PRICE)
            .askVolume(UPDATED_ASK_VOLUME)
            .baseInstrumentLast(UPDATED_BASE_INSTRUMENT_LAST)
            .createdAt(UPDATED_CREATED_AT);

        restSignalMockMvc.perform(put("/api/signals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSignal)))
            .andExpect(status().isOk());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeUpdate);
        Signal testSignal = signalList.get(signalList.size() - 1);
        assertThat(testSignal.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSignal.getIsin()).isEqualTo(UPDATED_ISIN);
        assertThat(testSignal.getLast()).isEqualTo(UPDATED_LAST);
        assertThat(testSignal.getTradeVolume()).isEqualTo(UPDATED_TRADE_VOLUME);
        assertThat(testSignal.getBidVolume()).isEqualTo(UPDATED_BID_VOLUME);
        assertThat(testSignal.getBidPrice()).isEqualTo(UPDATED_BID_PRICE);
        assertThat(testSignal.getAskPrice()).isEqualTo(UPDATED_ASK_PRICE);
        assertThat(testSignal.getAskVolume()).isEqualTo(UPDATED_ASK_VOLUME);
        assertThat(testSignal.getBaseInstrumentLast()).isEqualTo(UPDATED_BASE_INSTRUMENT_LAST);
        assertThat(testSignal.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingSignal() throws Exception {
        int databaseSizeBeforeUpdate = signalRepository.findAll().size();

        // Create the Signal

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignalMockMvc.perform(put("/api/signals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(signal)))
            .andExpect(status().isBadRequest());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSignal() throws Exception {
        // Initialize the database
        signalService.save(signal);

        int databaseSizeBeforeDelete = signalRepository.findAll().size();

        // Delete the signal
        restSignalMockMvc.perform(delete("/api/signals/{id}", signal.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
