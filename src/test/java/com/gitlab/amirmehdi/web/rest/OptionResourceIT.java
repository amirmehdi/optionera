package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.ETradeApp;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.repository.OptionRepository;
import com.gitlab.amirmehdi.service.OptionQueryService;
import com.gitlab.amirmehdi.service.OptionService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OptionResource} REST controller.
 */
@SpringBootTest(classes = ETradeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class OptionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CALL_ISIN = "AAAAAAAAAA";
    private static final String UPDATED_CALL_ISIN = "BBBBBBBBBB";

    private static final String DEFAULT_PUT_ISIN = "AAAAAAAAAA";
    private static final String UPDATED_PUT_ISIN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXP_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXP_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXP_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_STRIKE_PRICE = 1;
    private static final Integer UPDATED_STRIKE_PRICE = 2;
    private static final Integer SMALLER_STRIKE_PRICE = 1 - 1;

    private static final Integer DEFAULT_CONTRACT_SIZE = 1;
    private static final Integer UPDATED_CONTRACT_SIZE = 2;
    private static final Integer SMALLER_CONTRACT_SIZE = 1 - 1;

    private static final Boolean DEFAULT_CALL_IN_THE_MONEY = false;
    private static final Boolean UPDATED_CALL_IN_THE_MONEY = true;

    private static final Float DEFAULT_CALL_BREAK_EVEN = 1F;
    private static final Float UPDATED_CALL_BREAK_EVEN = 2F;
    private static final Float SMALLER_CALL_BREAK_EVEN = 1F - 1F;

    private static final Float DEFAULT_PUT_BREAK_EVEN = 1F;
    private static final Float UPDATED_PUT_BREAK_EVEN = 2F;
    private static final Float SMALLER_PUT_BREAK_EVEN = 1F - 1F;

    private static final Float DEFAULT_CALL_ASK_TO_BS = 1F;
    private static final Float UPDATED_CALL_ASK_TO_BS = 2F;
    private static final Float SMALLER_CALL_ASK_TO_BS = 1F - 1F;

    private static final Float DEFAULT_PUT_ASK_TO_BS = 1F;
    private static final Float UPDATED_PUT_ASK_TO_BS = 2F;
    private static final Float SMALLER_PUT_ASK_TO_BS = 1F - 1F;

    private static final Float DEFAULT_CALL_LEVERAGE = 1F;
    private static final Float UPDATED_CALL_LEVERAGE = 2F;
    private static final Float SMALLER_CALL_LEVERAGE = 1F - 1F;

    private static final Float DEFAULT_PUT_LEVERAGE = 1F;
    private static final Float UPDATED_PUT_LEVERAGE = 2F;
    private static final Float SMALLER_PUT_LEVERAGE = 1F - 1F;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private OptionService optionService;

    @Autowired
    private OptionQueryService optionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOptionMockMvc;

    private Option option;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createEntity(EntityManager em) {
        Option option = new Option()
            .name(DEFAULT_NAME)
            .callIsin(DEFAULT_CALL_ISIN)
            .putIsin(DEFAULT_PUT_ISIN)
            .expDate(DEFAULT_EXP_DATE)
            .strikePrice(DEFAULT_STRIKE_PRICE)
            .contractSize(DEFAULT_CONTRACT_SIZE)
            .callInTheMoney(DEFAULT_CALL_IN_THE_MONEY)
            .callBreakEven(DEFAULT_CALL_BREAK_EVEN)
            .putBreakEven(DEFAULT_PUT_BREAK_EVEN)
            .callAskToBS(DEFAULT_CALL_ASK_TO_BS)
            .putAskToBS(DEFAULT_PUT_ASK_TO_BS)
            .callLeverage(DEFAULT_CALL_LEVERAGE)
            .putLeverage(DEFAULT_PUT_LEVERAGE);
        // Add required entity
        Instrument instrument;
        if (TestUtil.findAll(em, Instrument.class).isEmpty()) {
            instrument = InstrumentResourceIT.createEntity(em);
            em.persist(instrument);
            em.flush();
        } else {
            instrument = TestUtil.findAll(em, Instrument.class).get(0);
        }
        option.setInstrument(instrument);
        return option;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createUpdatedEntity(EntityManager em) {
        Option option = new Option()
            .name(UPDATED_NAME)
            .callIsin(UPDATED_CALL_ISIN)
            .putIsin(UPDATED_PUT_ISIN)
            .expDate(UPDATED_EXP_DATE)
            .strikePrice(UPDATED_STRIKE_PRICE)
            .contractSize(UPDATED_CONTRACT_SIZE)
            .callInTheMoney(UPDATED_CALL_IN_THE_MONEY)
            .callBreakEven(UPDATED_CALL_BREAK_EVEN)
            .putBreakEven(UPDATED_PUT_BREAK_EVEN)
            .callAskToBS(UPDATED_CALL_ASK_TO_BS)
            .putAskToBS(UPDATED_PUT_ASK_TO_BS)
            .callLeverage(UPDATED_CALL_LEVERAGE)
            .putLeverage(UPDATED_PUT_LEVERAGE);
        // Add required entity
        Instrument instrument;
        if (TestUtil.findAll(em, Instrument.class).isEmpty()) {
            instrument = InstrumentResourceIT.createUpdatedEntity(em);
            em.persist(instrument);
            em.flush();
        } else {
            instrument = TestUtil.findAll(em, Instrument.class).get(0);
        }
        option.setInstrument(instrument);
        return option;
    }

    @BeforeEach
    public void initTest() {
        option = createEntity(em);
    }

    @Test
    @Transactional
    public void createOption() throws Exception {
        int databaseSizeBeforeCreate = optionRepository.findAll().size();

        // Create the Option
        restOptionMockMvc.perform(post("/api/options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isCreated());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeCreate + 1);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOption.getCallIsin()).isEqualTo(DEFAULT_CALL_ISIN);
        assertThat(testOption.getPutIsin()).isEqualTo(DEFAULT_PUT_ISIN);
        assertThat(testOption.getExpDate()).isEqualTo(DEFAULT_EXP_DATE);
        assertThat(testOption.getStrikePrice()).isEqualTo(DEFAULT_STRIKE_PRICE);
        assertThat(testOption.getContractSize()).isEqualTo(DEFAULT_CONTRACT_SIZE);
        assertThat(testOption.isCallInTheMoney()).isEqualTo(DEFAULT_CALL_IN_THE_MONEY);
        assertThat(testOption.getCallBreakEven()).isEqualTo(DEFAULT_CALL_BREAK_EVEN);
        assertThat(testOption.getPutBreakEven()).isEqualTo(DEFAULT_PUT_BREAK_EVEN);
        assertThat(testOption.getCallAskToBS()).isEqualTo(DEFAULT_CALL_ASK_TO_BS);
        assertThat(testOption.getPutAskToBS()).isEqualTo(DEFAULT_PUT_ASK_TO_BS);
        assertThat(testOption.getCallLeverage()).isEqualTo(DEFAULT_CALL_LEVERAGE);
        assertThat(testOption.getPutLeverage()).isEqualTo(DEFAULT_PUT_LEVERAGE);
    }

    @Test
    @Transactional
    public void createOptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = optionRepository.findAll().size();

        // Create the Option with an existing ID
        option.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionMockMvc.perform(post("/api/options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionRepository.findAll().size();
        // set the field null
        option.setName(null);

        // Create the Option, which fails.

        restOptionMockMvc.perform(post("/api/options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isBadRequest());

        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCallIsinIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionRepository.findAll().size();
        // set the field null
        option.setCallIsin(null);

        // Create the Option, which fails.

        restOptionMockMvc.perform(post("/api/options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isBadRequest());

        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPutIsinIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionRepository.findAll().size();
        // set the field null
        option.setPutIsin(null);

        // Create the Option, which fails.

        restOptionMockMvc.perform(post("/api/options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isBadRequest());

        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionRepository.findAll().size();
        // set the field null
        option.setExpDate(null);

        // Create the Option, which fails.

        restOptionMockMvc.perform(post("/api/options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isBadRequest());

        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStrikePriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionRepository.findAll().size();
        // set the field null
        option.setStrikePrice(null);

        // Create the Option, which fails.

        restOptionMockMvc.perform(post("/api/options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isBadRequest());

        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeTest);
    }


    @Test
    @Transactional
    public void getAllOptions() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList
        restOptionMockMvc.perform(get("/api/options?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(option.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].callIsin").value(hasItem(DEFAULT_CALL_ISIN)))
            .andExpect(jsonPath("$.[*].putIsin").value(hasItem(DEFAULT_PUT_ISIN)))
            .andExpect(jsonPath("$.[*].expDate").value(hasItem(DEFAULT_EXP_DATE.toString())))
            .andExpect(jsonPath("$.[*].strikePrice").value(hasItem(DEFAULT_STRIKE_PRICE)))
            .andExpect(jsonPath("$.[*].contractSize").value(hasItem(DEFAULT_CONTRACT_SIZE)))
            .andExpect(jsonPath("$.[*].callInTheMoney").value(hasItem(DEFAULT_CALL_IN_THE_MONEY.booleanValue())))
            .andExpect(jsonPath("$.[*].callBreakEven").value(hasItem(DEFAULT_CALL_BREAK_EVEN.doubleValue())))
            .andExpect(jsonPath("$.[*].putBreakEven").value(hasItem(DEFAULT_PUT_BREAK_EVEN.doubleValue())))
            .andExpect(jsonPath("$.[*].callAskToBS").value(hasItem(DEFAULT_CALL_ASK_TO_BS.doubleValue())))
            .andExpect(jsonPath("$.[*].putAskToBS").value(hasItem(DEFAULT_PUT_ASK_TO_BS.doubleValue())))
            .andExpect(jsonPath("$.[*].callLeverage").value(hasItem(DEFAULT_CALL_LEVERAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].putLeverage").value(hasItem(DEFAULT_PUT_LEVERAGE.doubleValue())));
    }

    @Test
    @Transactional
    public void getOption() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get the option
        restOptionMockMvc.perform(get("/api/options/{id}", option.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(option.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.callIsin").value(DEFAULT_CALL_ISIN))
            .andExpect(jsonPath("$.putIsin").value(DEFAULT_PUT_ISIN))
            .andExpect(jsonPath("$.expDate").value(DEFAULT_EXP_DATE.toString()))
            .andExpect(jsonPath("$.strikePrice").value(DEFAULT_STRIKE_PRICE))
            .andExpect(jsonPath("$.contractSize").value(DEFAULT_CONTRACT_SIZE))
            .andExpect(jsonPath("$.callInTheMoney").value(DEFAULT_CALL_IN_THE_MONEY.booleanValue()))
            .andExpect(jsonPath("$.callBreakEven").value(DEFAULT_CALL_BREAK_EVEN.doubleValue()))
            .andExpect(jsonPath("$.putBreakEven").value(DEFAULT_PUT_BREAK_EVEN.doubleValue()))
            .andExpect(jsonPath("$.callAskToBS").value(DEFAULT_CALL_ASK_TO_BS.doubleValue()))
            .andExpect(jsonPath("$.putAskToBS").value(DEFAULT_PUT_ASK_TO_BS.doubleValue()))
            .andExpect(jsonPath("$.callLeverage").value(DEFAULT_CALL_LEVERAGE.doubleValue()))
            .andExpect(jsonPath("$.putLeverage").value(DEFAULT_PUT_LEVERAGE.doubleValue()));
    }


    @Test
    @Transactional
    public void getOptionsByIdFiltering() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        Long id = option.getId();

        defaultOptionShouldBeFound("id.equals=" + id);
        defaultOptionShouldNotBeFound("id.notEquals=" + id);

        defaultOptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOptionShouldNotBeFound("id.greaterThan=" + id);

        defaultOptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOptionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOptionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where name equals to DEFAULT_NAME
        defaultOptionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the optionList where name equals to UPDATED_NAME
        defaultOptionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOptionsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where name not equals to DEFAULT_NAME
        defaultOptionShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the optionList where name not equals to UPDATED_NAME
        defaultOptionShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOptionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOptionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the optionList where name equals to UPDATED_NAME
        defaultOptionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOptionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where name is not null
        defaultOptionShouldBeFound("name.specified=true");

        // Get all the optionList where name is null
        defaultOptionShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllOptionsByNameContainsSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where name contains DEFAULT_NAME
        defaultOptionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the optionList where name contains UPDATED_NAME
        defaultOptionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOptionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where name does not contain DEFAULT_NAME
        defaultOptionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the optionList where name does not contain UPDATED_NAME
        defaultOptionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllOptionsByCallIsinIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callIsin equals to DEFAULT_CALL_ISIN
        defaultOptionShouldBeFound("callIsin.equals=" + DEFAULT_CALL_ISIN);

        // Get all the optionList where callIsin equals to UPDATED_CALL_ISIN
        defaultOptionShouldNotBeFound("callIsin.equals=" + UPDATED_CALL_ISIN);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallIsinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callIsin not equals to DEFAULT_CALL_ISIN
        defaultOptionShouldNotBeFound("callIsin.notEquals=" + DEFAULT_CALL_ISIN);

        // Get all the optionList where callIsin not equals to UPDATED_CALL_ISIN
        defaultOptionShouldBeFound("callIsin.notEquals=" + UPDATED_CALL_ISIN);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallIsinIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callIsin in DEFAULT_CALL_ISIN or UPDATED_CALL_ISIN
        defaultOptionShouldBeFound("callIsin.in=" + DEFAULT_CALL_ISIN + "," + UPDATED_CALL_ISIN);

        // Get all the optionList where callIsin equals to UPDATED_CALL_ISIN
        defaultOptionShouldNotBeFound("callIsin.in=" + UPDATED_CALL_ISIN);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallIsinIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callIsin is not null
        defaultOptionShouldBeFound("callIsin.specified=true");

        // Get all the optionList where callIsin is null
        defaultOptionShouldNotBeFound("callIsin.specified=false");
    }
                @Test
    @Transactional
    public void getAllOptionsByCallIsinContainsSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callIsin contains DEFAULT_CALL_ISIN
        defaultOptionShouldBeFound("callIsin.contains=" + DEFAULT_CALL_ISIN);

        // Get all the optionList where callIsin contains UPDATED_CALL_ISIN
        defaultOptionShouldNotBeFound("callIsin.contains=" + UPDATED_CALL_ISIN);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallIsinNotContainsSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callIsin does not contain DEFAULT_CALL_ISIN
        defaultOptionShouldNotBeFound("callIsin.doesNotContain=" + DEFAULT_CALL_ISIN);

        // Get all the optionList where callIsin does not contain UPDATED_CALL_ISIN
        defaultOptionShouldBeFound("callIsin.doesNotContain=" + UPDATED_CALL_ISIN);
    }


    @Test
    @Transactional
    public void getAllOptionsByPutIsinIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putIsin equals to DEFAULT_PUT_ISIN
        defaultOptionShouldBeFound("putIsin.equals=" + DEFAULT_PUT_ISIN);

        // Get all the optionList where putIsin equals to UPDATED_PUT_ISIN
        defaultOptionShouldNotBeFound("putIsin.equals=" + UPDATED_PUT_ISIN);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutIsinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putIsin not equals to DEFAULT_PUT_ISIN
        defaultOptionShouldNotBeFound("putIsin.notEquals=" + DEFAULT_PUT_ISIN);

        // Get all the optionList where putIsin not equals to UPDATED_PUT_ISIN
        defaultOptionShouldBeFound("putIsin.notEquals=" + UPDATED_PUT_ISIN);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutIsinIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putIsin in DEFAULT_PUT_ISIN or UPDATED_PUT_ISIN
        defaultOptionShouldBeFound("putIsin.in=" + DEFAULT_PUT_ISIN + "," + UPDATED_PUT_ISIN);

        // Get all the optionList where putIsin equals to UPDATED_PUT_ISIN
        defaultOptionShouldNotBeFound("putIsin.in=" + UPDATED_PUT_ISIN);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutIsinIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putIsin is not null
        defaultOptionShouldBeFound("putIsin.specified=true");

        // Get all the optionList where putIsin is null
        defaultOptionShouldNotBeFound("putIsin.specified=false");
    }
                @Test
    @Transactional
    public void getAllOptionsByPutIsinContainsSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putIsin contains DEFAULT_PUT_ISIN
        defaultOptionShouldBeFound("putIsin.contains=" + DEFAULT_PUT_ISIN);

        // Get all the optionList where putIsin contains UPDATED_PUT_ISIN
        defaultOptionShouldNotBeFound("putIsin.contains=" + UPDATED_PUT_ISIN);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutIsinNotContainsSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putIsin does not contain DEFAULT_PUT_ISIN
        defaultOptionShouldNotBeFound("putIsin.doesNotContain=" + DEFAULT_PUT_ISIN);

        // Get all the optionList where putIsin does not contain UPDATED_PUT_ISIN
        defaultOptionShouldBeFound("putIsin.doesNotContain=" + UPDATED_PUT_ISIN);
    }


    @Test
    @Transactional
    public void getAllOptionsByExpDateIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where expDate equals to DEFAULT_EXP_DATE
        defaultOptionShouldBeFound("expDate.equals=" + DEFAULT_EXP_DATE);

        // Get all the optionList where expDate equals to UPDATED_EXP_DATE
        defaultOptionShouldNotBeFound("expDate.equals=" + UPDATED_EXP_DATE);
    }

    @Test
    @Transactional
    public void getAllOptionsByExpDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where expDate not equals to DEFAULT_EXP_DATE
        defaultOptionShouldNotBeFound("expDate.notEquals=" + DEFAULT_EXP_DATE);

        // Get all the optionList where expDate not equals to UPDATED_EXP_DATE
        defaultOptionShouldBeFound("expDate.notEquals=" + UPDATED_EXP_DATE);
    }

    @Test
    @Transactional
    public void getAllOptionsByExpDateIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where expDate in DEFAULT_EXP_DATE or UPDATED_EXP_DATE
        defaultOptionShouldBeFound("expDate.in=" + DEFAULT_EXP_DATE + "," + UPDATED_EXP_DATE);

        // Get all the optionList where expDate equals to UPDATED_EXP_DATE
        defaultOptionShouldNotBeFound("expDate.in=" + UPDATED_EXP_DATE);
    }

    @Test
    @Transactional
    public void getAllOptionsByExpDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where expDate is not null
        defaultOptionShouldBeFound("expDate.specified=true");

        // Get all the optionList where expDate is null
        defaultOptionShouldNotBeFound("expDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionsByExpDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where expDate is greater than or equal to DEFAULT_EXP_DATE
        defaultOptionShouldBeFound("expDate.greaterThanOrEqual=" + DEFAULT_EXP_DATE);

        // Get all the optionList where expDate is greater than or equal to UPDATED_EXP_DATE
        defaultOptionShouldNotBeFound("expDate.greaterThanOrEqual=" + UPDATED_EXP_DATE);
    }

    @Test
    @Transactional
    public void getAllOptionsByExpDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where expDate is less than or equal to DEFAULT_EXP_DATE
        defaultOptionShouldBeFound("expDate.lessThanOrEqual=" + DEFAULT_EXP_DATE);

        // Get all the optionList where expDate is less than or equal to SMALLER_EXP_DATE
        defaultOptionShouldNotBeFound("expDate.lessThanOrEqual=" + SMALLER_EXP_DATE);
    }

    @Test
    @Transactional
    public void getAllOptionsByExpDateIsLessThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where expDate is less than DEFAULT_EXP_DATE
        defaultOptionShouldNotBeFound("expDate.lessThan=" + DEFAULT_EXP_DATE);

        // Get all the optionList where expDate is less than UPDATED_EXP_DATE
        defaultOptionShouldBeFound("expDate.lessThan=" + UPDATED_EXP_DATE);
    }

    @Test
    @Transactional
    public void getAllOptionsByExpDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where expDate is greater than DEFAULT_EXP_DATE
        defaultOptionShouldNotBeFound("expDate.greaterThan=" + DEFAULT_EXP_DATE);

        // Get all the optionList where expDate is greater than SMALLER_EXP_DATE
        defaultOptionShouldBeFound("expDate.greaterThan=" + SMALLER_EXP_DATE);
    }


    @Test
    @Transactional
    public void getAllOptionsByStrikePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where strikePrice equals to DEFAULT_STRIKE_PRICE
        defaultOptionShouldBeFound("strikePrice.equals=" + DEFAULT_STRIKE_PRICE);

        // Get all the optionList where strikePrice equals to UPDATED_STRIKE_PRICE
        defaultOptionShouldNotBeFound("strikePrice.equals=" + UPDATED_STRIKE_PRICE);
    }

    @Test
    @Transactional
    public void getAllOptionsByStrikePriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where strikePrice not equals to DEFAULT_STRIKE_PRICE
        defaultOptionShouldNotBeFound("strikePrice.notEquals=" + DEFAULT_STRIKE_PRICE);

        // Get all the optionList where strikePrice not equals to UPDATED_STRIKE_PRICE
        defaultOptionShouldBeFound("strikePrice.notEquals=" + UPDATED_STRIKE_PRICE);
    }

    @Test
    @Transactional
    public void getAllOptionsByStrikePriceIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where strikePrice in DEFAULT_STRIKE_PRICE or UPDATED_STRIKE_PRICE
        defaultOptionShouldBeFound("strikePrice.in=" + DEFAULT_STRIKE_PRICE + "," + UPDATED_STRIKE_PRICE);

        // Get all the optionList where strikePrice equals to UPDATED_STRIKE_PRICE
        defaultOptionShouldNotBeFound("strikePrice.in=" + UPDATED_STRIKE_PRICE);
    }

    @Test
    @Transactional
    public void getAllOptionsByStrikePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where strikePrice is not null
        defaultOptionShouldBeFound("strikePrice.specified=true");

        // Get all the optionList where strikePrice is null
        defaultOptionShouldNotBeFound("strikePrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionsByStrikePriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where strikePrice is greater than or equal to DEFAULT_STRIKE_PRICE
        defaultOptionShouldBeFound("strikePrice.greaterThanOrEqual=" + DEFAULT_STRIKE_PRICE);

        // Get all the optionList where strikePrice is greater than or equal to UPDATED_STRIKE_PRICE
        defaultOptionShouldNotBeFound("strikePrice.greaterThanOrEqual=" + UPDATED_STRIKE_PRICE);
    }

    @Test
    @Transactional
    public void getAllOptionsByStrikePriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where strikePrice is less than or equal to DEFAULT_STRIKE_PRICE
        defaultOptionShouldBeFound("strikePrice.lessThanOrEqual=" + DEFAULT_STRIKE_PRICE);

        // Get all the optionList where strikePrice is less than or equal to SMALLER_STRIKE_PRICE
        defaultOptionShouldNotBeFound("strikePrice.lessThanOrEqual=" + SMALLER_STRIKE_PRICE);
    }

    @Test
    @Transactional
    public void getAllOptionsByStrikePriceIsLessThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where strikePrice is less than DEFAULT_STRIKE_PRICE
        defaultOptionShouldNotBeFound("strikePrice.lessThan=" + DEFAULT_STRIKE_PRICE);

        // Get all the optionList where strikePrice is less than UPDATED_STRIKE_PRICE
        defaultOptionShouldBeFound("strikePrice.lessThan=" + UPDATED_STRIKE_PRICE);
    }

    @Test
    @Transactional
    public void getAllOptionsByStrikePriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where strikePrice is greater than DEFAULT_STRIKE_PRICE
        defaultOptionShouldNotBeFound("strikePrice.greaterThan=" + DEFAULT_STRIKE_PRICE);

        // Get all the optionList where strikePrice is greater than SMALLER_STRIKE_PRICE
        defaultOptionShouldBeFound("strikePrice.greaterThan=" + SMALLER_STRIKE_PRICE);
    }


    @Test
    @Transactional
    public void getAllOptionsByContractSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where contractSize equals to DEFAULT_CONTRACT_SIZE
        defaultOptionShouldBeFound("contractSize.equals=" + DEFAULT_CONTRACT_SIZE);

        // Get all the optionList where contractSize equals to UPDATED_CONTRACT_SIZE
        defaultOptionShouldNotBeFound("contractSize.equals=" + UPDATED_CONTRACT_SIZE);
    }

    @Test
    @Transactional
    public void getAllOptionsByContractSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where contractSize not equals to DEFAULT_CONTRACT_SIZE
        defaultOptionShouldNotBeFound("contractSize.notEquals=" + DEFAULT_CONTRACT_SIZE);

        // Get all the optionList where contractSize not equals to UPDATED_CONTRACT_SIZE
        defaultOptionShouldBeFound("contractSize.notEquals=" + UPDATED_CONTRACT_SIZE);
    }

    @Test
    @Transactional
    public void getAllOptionsByContractSizeIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where contractSize in DEFAULT_CONTRACT_SIZE or UPDATED_CONTRACT_SIZE
        defaultOptionShouldBeFound("contractSize.in=" + DEFAULT_CONTRACT_SIZE + "," + UPDATED_CONTRACT_SIZE);

        // Get all the optionList where contractSize equals to UPDATED_CONTRACT_SIZE
        defaultOptionShouldNotBeFound("contractSize.in=" + UPDATED_CONTRACT_SIZE);
    }

    @Test
    @Transactional
    public void getAllOptionsByContractSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where contractSize is not null
        defaultOptionShouldBeFound("contractSize.specified=true");

        // Get all the optionList where contractSize is null
        defaultOptionShouldNotBeFound("contractSize.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionsByContractSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where contractSize is greater than or equal to DEFAULT_CONTRACT_SIZE
        defaultOptionShouldBeFound("contractSize.greaterThanOrEqual=" + DEFAULT_CONTRACT_SIZE);

        // Get all the optionList where contractSize is greater than or equal to UPDATED_CONTRACT_SIZE
        defaultOptionShouldNotBeFound("contractSize.greaterThanOrEqual=" + UPDATED_CONTRACT_SIZE);
    }

    @Test
    @Transactional
    public void getAllOptionsByContractSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where contractSize is less than or equal to DEFAULT_CONTRACT_SIZE
        defaultOptionShouldBeFound("contractSize.lessThanOrEqual=" + DEFAULT_CONTRACT_SIZE);

        // Get all the optionList where contractSize is less than or equal to SMALLER_CONTRACT_SIZE
        defaultOptionShouldNotBeFound("contractSize.lessThanOrEqual=" + SMALLER_CONTRACT_SIZE);
    }

    @Test
    @Transactional
    public void getAllOptionsByContractSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where contractSize is less than DEFAULT_CONTRACT_SIZE
        defaultOptionShouldNotBeFound("contractSize.lessThan=" + DEFAULT_CONTRACT_SIZE);

        // Get all the optionList where contractSize is less than UPDATED_CONTRACT_SIZE
        defaultOptionShouldBeFound("contractSize.lessThan=" + UPDATED_CONTRACT_SIZE);
    }

    @Test
    @Transactional
    public void getAllOptionsByContractSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where contractSize is greater than DEFAULT_CONTRACT_SIZE
        defaultOptionShouldNotBeFound("contractSize.greaterThan=" + DEFAULT_CONTRACT_SIZE);

        // Get all the optionList where contractSize is greater than SMALLER_CONTRACT_SIZE
        defaultOptionShouldBeFound("contractSize.greaterThan=" + SMALLER_CONTRACT_SIZE);
    }


    @Test
    @Transactional
    public void getAllOptionsByCallInTheMoneyIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callInTheMoney equals to DEFAULT_CALL_IN_THE_MONEY
        defaultOptionShouldBeFound("callInTheMoney.equals=" + DEFAULT_CALL_IN_THE_MONEY);

        // Get all the optionList where callInTheMoney equals to UPDATED_CALL_IN_THE_MONEY
        defaultOptionShouldNotBeFound("callInTheMoney.equals=" + UPDATED_CALL_IN_THE_MONEY);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallInTheMoneyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callInTheMoney not equals to DEFAULT_CALL_IN_THE_MONEY
        defaultOptionShouldNotBeFound("callInTheMoney.notEquals=" + DEFAULT_CALL_IN_THE_MONEY);

        // Get all the optionList where callInTheMoney not equals to UPDATED_CALL_IN_THE_MONEY
        defaultOptionShouldBeFound("callInTheMoney.notEquals=" + UPDATED_CALL_IN_THE_MONEY);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallInTheMoneyIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callInTheMoney in DEFAULT_CALL_IN_THE_MONEY or UPDATED_CALL_IN_THE_MONEY
        defaultOptionShouldBeFound("callInTheMoney.in=" + DEFAULT_CALL_IN_THE_MONEY + "," + UPDATED_CALL_IN_THE_MONEY);

        // Get all the optionList where callInTheMoney equals to UPDATED_CALL_IN_THE_MONEY
        defaultOptionShouldNotBeFound("callInTheMoney.in=" + UPDATED_CALL_IN_THE_MONEY);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallInTheMoneyIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callInTheMoney is not null
        defaultOptionShouldBeFound("callInTheMoney.specified=true");

        // Get all the optionList where callInTheMoney is null
        defaultOptionShouldNotBeFound("callInTheMoney.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionsByCallBreakEvenIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callBreakEven equals to DEFAULT_CALL_BREAK_EVEN
        defaultOptionShouldBeFound("callBreakEven.equals=" + DEFAULT_CALL_BREAK_EVEN);

        // Get all the optionList where callBreakEven equals to UPDATED_CALL_BREAK_EVEN
        defaultOptionShouldNotBeFound("callBreakEven.equals=" + UPDATED_CALL_BREAK_EVEN);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallBreakEvenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callBreakEven not equals to DEFAULT_CALL_BREAK_EVEN
        defaultOptionShouldNotBeFound("callBreakEven.notEquals=" + DEFAULT_CALL_BREAK_EVEN);

        // Get all the optionList where callBreakEven not equals to UPDATED_CALL_BREAK_EVEN
        defaultOptionShouldBeFound("callBreakEven.notEquals=" + UPDATED_CALL_BREAK_EVEN);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallBreakEvenIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callBreakEven in DEFAULT_CALL_BREAK_EVEN or UPDATED_CALL_BREAK_EVEN
        defaultOptionShouldBeFound("callBreakEven.in=" + DEFAULT_CALL_BREAK_EVEN + "," + UPDATED_CALL_BREAK_EVEN);

        // Get all the optionList where callBreakEven equals to UPDATED_CALL_BREAK_EVEN
        defaultOptionShouldNotBeFound("callBreakEven.in=" + UPDATED_CALL_BREAK_EVEN);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallBreakEvenIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callBreakEven is not null
        defaultOptionShouldBeFound("callBreakEven.specified=true");

        // Get all the optionList where callBreakEven is null
        defaultOptionShouldNotBeFound("callBreakEven.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionsByCallBreakEvenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callBreakEven is greater than or equal to DEFAULT_CALL_BREAK_EVEN
        defaultOptionShouldBeFound("callBreakEven.greaterThanOrEqual=" + DEFAULT_CALL_BREAK_EVEN);

        // Get all the optionList where callBreakEven is greater than or equal to UPDATED_CALL_BREAK_EVEN
        defaultOptionShouldNotBeFound("callBreakEven.greaterThanOrEqual=" + UPDATED_CALL_BREAK_EVEN);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallBreakEvenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callBreakEven is less than or equal to DEFAULT_CALL_BREAK_EVEN
        defaultOptionShouldBeFound("callBreakEven.lessThanOrEqual=" + DEFAULT_CALL_BREAK_EVEN);

        // Get all the optionList where callBreakEven is less than or equal to SMALLER_CALL_BREAK_EVEN
        defaultOptionShouldNotBeFound("callBreakEven.lessThanOrEqual=" + SMALLER_CALL_BREAK_EVEN);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallBreakEvenIsLessThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callBreakEven is less than DEFAULT_CALL_BREAK_EVEN
        defaultOptionShouldNotBeFound("callBreakEven.lessThan=" + DEFAULT_CALL_BREAK_EVEN);

        // Get all the optionList where callBreakEven is less than UPDATED_CALL_BREAK_EVEN
        defaultOptionShouldBeFound("callBreakEven.lessThan=" + UPDATED_CALL_BREAK_EVEN);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallBreakEvenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callBreakEven is greater than DEFAULT_CALL_BREAK_EVEN
        defaultOptionShouldNotBeFound("callBreakEven.greaterThan=" + DEFAULT_CALL_BREAK_EVEN);

        // Get all the optionList where callBreakEven is greater than SMALLER_CALL_BREAK_EVEN
        defaultOptionShouldBeFound("callBreakEven.greaterThan=" + SMALLER_CALL_BREAK_EVEN);
    }


    @Test
    @Transactional
    public void getAllOptionsByPutBreakEvenIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putBreakEven equals to DEFAULT_PUT_BREAK_EVEN
        defaultOptionShouldBeFound("putBreakEven.equals=" + DEFAULT_PUT_BREAK_EVEN);

        // Get all the optionList where putBreakEven equals to UPDATED_PUT_BREAK_EVEN
        defaultOptionShouldNotBeFound("putBreakEven.equals=" + UPDATED_PUT_BREAK_EVEN);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutBreakEvenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putBreakEven not equals to DEFAULT_PUT_BREAK_EVEN
        defaultOptionShouldNotBeFound("putBreakEven.notEquals=" + DEFAULT_PUT_BREAK_EVEN);

        // Get all the optionList where putBreakEven not equals to UPDATED_PUT_BREAK_EVEN
        defaultOptionShouldBeFound("putBreakEven.notEquals=" + UPDATED_PUT_BREAK_EVEN);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutBreakEvenIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putBreakEven in DEFAULT_PUT_BREAK_EVEN or UPDATED_PUT_BREAK_EVEN
        defaultOptionShouldBeFound("putBreakEven.in=" + DEFAULT_PUT_BREAK_EVEN + "," + UPDATED_PUT_BREAK_EVEN);

        // Get all the optionList where putBreakEven equals to UPDATED_PUT_BREAK_EVEN
        defaultOptionShouldNotBeFound("putBreakEven.in=" + UPDATED_PUT_BREAK_EVEN);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutBreakEvenIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putBreakEven is not null
        defaultOptionShouldBeFound("putBreakEven.specified=true");

        // Get all the optionList where putBreakEven is null
        defaultOptionShouldNotBeFound("putBreakEven.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionsByPutBreakEvenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putBreakEven is greater than or equal to DEFAULT_PUT_BREAK_EVEN
        defaultOptionShouldBeFound("putBreakEven.greaterThanOrEqual=" + DEFAULT_PUT_BREAK_EVEN);

        // Get all the optionList where putBreakEven is greater than or equal to UPDATED_PUT_BREAK_EVEN
        defaultOptionShouldNotBeFound("putBreakEven.greaterThanOrEqual=" + UPDATED_PUT_BREAK_EVEN);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutBreakEvenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putBreakEven is less than or equal to DEFAULT_PUT_BREAK_EVEN
        defaultOptionShouldBeFound("putBreakEven.lessThanOrEqual=" + DEFAULT_PUT_BREAK_EVEN);

        // Get all the optionList where putBreakEven is less than or equal to SMALLER_PUT_BREAK_EVEN
        defaultOptionShouldNotBeFound("putBreakEven.lessThanOrEqual=" + SMALLER_PUT_BREAK_EVEN);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutBreakEvenIsLessThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putBreakEven is less than DEFAULT_PUT_BREAK_EVEN
        defaultOptionShouldNotBeFound("putBreakEven.lessThan=" + DEFAULT_PUT_BREAK_EVEN);

        // Get all the optionList where putBreakEven is less than UPDATED_PUT_BREAK_EVEN
        defaultOptionShouldBeFound("putBreakEven.lessThan=" + UPDATED_PUT_BREAK_EVEN);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutBreakEvenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putBreakEven is greater than DEFAULT_PUT_BREAK_EVEN
        defaultOptionShouldNotBeFound("putBreakEven.greaterThan=" + DEFAULT_PUT_BREAK_EVEN);

        // Get all the optionList where putBreakEven is greater than SMALLER_PUT_BREAK_EVEN
        defaultOptionShouldBeFound("putBreakEven.greaterThan=" + SMALLER_PUT_BREAK_EVEN);
    }


    @Test
    @Transactional
    public void getAllOptionsByCallAskToBSIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callAskToBS equals to DEFAULT_CALL_ASK_TO_BS
        defaultOptionShouldBeFound("callAskToBS.equals=" + DEFAULT_CALL_ASK_TO_BS);

        // Get all the optionList where callAskToBS equals to UPDATED_CALL_ASK_TO_BS
        defaultOptionShouldNotBeFound("callAskToBS.equals=" + UPDATED_CALL_ASK_TO_BS);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallAskToBSIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callAskToBS not equals to DEFAULT_CALL_ASK_TO_BS
        defaultOptionShouldNotBeFound("callAskToBS.notEquals=" + DEFAULT_CALL_ASK_TO_BS);

        // Get all the optionList where callAskToBS not equals to UPDATED_CALL_ASK_TO_BS
        defaultOptionShouldBeFound("callAskToBS.notEquals=" + UPDATED_CALL_ASK_TO_BS);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallAskToBSIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callAskToBS in DEFAULT_CALL_ASK_TO_BS or UPDATED_CALL_ASK_TO_BS
        defaultOptionShouldBeFound("callAskToBS.in=" + DEFAULT_CALL_ASK_TO_BS + "," + UPDATED_CALL_ASK_TO_BS);

        // Get all the optionList where callAskToBS equals to UPDATED_CALL_ASK_TO_BS
        defaultOptionShouldNotBeFound("callAskToBS.in=" + UPDATED_CALL_ASK_TO_BS);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallAskToBSIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callAskToBS is not null
        defaultOptionShouldBeFound("callAskToBS.specified=true");

        // Get all the optionList where callAskToBS is null
        defaultOptionShouldNotBeFound("callAskToBS.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionsByCallAskToBSIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callAskToBS is greater than or equal to DEFAULT_CALL_ASK_TO_BS
        defaultOptionShouldBeFound("callAskToBS.greaterThanOrEqual=" + DEFAULT_CALL_ASK_TO_BS);

        // Get all the optionList where callAskToBS is greater than or equal to UPDATED_CALL_ASK_TO_BS
        defaultOptionShouldNotBeFound("callAskToBS.greaterThanOrEqual=" + UPDATED_CALL_ASK_TO_BS);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallAskToBSIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callAskToBS is less than or equal to DEFAULT_CALL_ASK_TO_BS
        defaultOptionShouldBeFound("callAskToBS.lessThanOrEqual=" + DEFAULT_CALL_ASK_TO_BS);

        // Get all the optionList where callAskToBS is less than or equal to SMALLER_CALL_ASK_TO_BS
        defaultOptionShouldNotBeFound("callAskToBS.lessThanOrEqual=" + SMALLER_CALL_ASK_TO_BS);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallAskToBSIsLessThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callAskToBS is less than DEFAULT_CALL_ASK_TO_BS
        defaultOptionShouldNotBeFound("callAskToBS.lessThan=" + DEFAULT_CALL_ASK_TO_BS);

        // Get all the optionList where callAskToBS is less than UPDATED_CALL_ASK_TO_BS
        defaultOptionShouldBeFound("callAskToBS.lessThan=" + UPDATED_CALL_ASK_TO_BS);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallAskToBSIsGreaterThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callAskToBS is greater than DEFAULT_CALL_ASK_TO_BS
        defaultOptionShouldNotBeFound("callAskToBS.greaterThan=" + DEFAULT_CALL_ASK_TO_BS);

        // Get all the optionList where callAskToBS is greater than SMALLER_CALL_ASK_TO_BS
        defaultOptionShouldBeFound("callAskToBS.greaterThan=" + SMALLER_CALL_ASK_TO_BS);
    }


    @Test
    @Transactional
    public void getAllOptionsByPutAskToBSIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putAskToBS equals to DEFAULT_PUT_ASK_TO_BS
        defaultOptionShouldBeFound("putAskToBS.equals=" + DEFAULT_PUT_ASK_TO_BS);

        // Get all the optionList where putAskToBS equals to UPDATED_PUT_ASK_TO_BS
        defaultOptionShouldNotBeFound("putAskToBS.equals=" + UPDATED_PUT_ASK_TO_BS);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutAskToBSIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putAskToBS not equals to DEFAULT_PUT_ASK_TO_BS
        defaultOptionShouldNotBeFound("putAskToBS.notEquals=" + DEFAULT_PUT_ASK_TO_BS);

        // Get all the optionList where putAskToBS not equals to UPDATED_PUT_ASK_TO_BS
        defaultOptionShouldBeFound("putAskToBS.notEquals=" + UPDATED_PUT_ASK_TO_BS);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutAskToBSIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putAskToBS in DEFAULT_PUT_ASK_TO_BS or UPDATED_PUT_ASK_TO_BS
        defaultOptionShouldBeFound("putAskToBS.in=" + DEFAULT_PUT_ASK_TO_BS + "," + UPDATED_PUT_ASK_TO_BS);

        // Get all the optionList where putAskToBS equals to UPDATED_PUT_ASK_TO_BS
        defaultOptionShouldNotBeFound("putAskToBS.in=" + UPDATED_PUT_ASK_TO_BS);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutAskToBSIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putAskToBS is not null
        defaultOptionShouldBeFound("putAskToBS.specified=true");

        // Get all the optionList where putAskToBS is null
        defaultOptionShouldNotBeFound("putAskToBS.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionsByPutAskToBSIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putAskToBS is greater than or equal to DEFAULT_PUT_ASK_TO_BS
        defaultOptionShouldBeFound("putAskToBS.greaterThanOrEqual=" + DEFAULT_PUT_ASK_TO_BS);

        // Get all the optionList where putAskToBS is greater than or equal to UPDATED_PUT_ASK_TO_BS
        defaultOptionShouldNotBeFound("putAskToBS.greaterThanOrEqual=" + UPDATED_PUT_ASK_TO_BS);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutAskToBSIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putAskToBS is less than or equal to DEFAULT_PUT_ASK_TO_BS
        defaultOptionShouldBeFound("putAskToBS.lessThanOrEqual=" + DEFAULT_PUT_ASK_TO_BS);

        // Get all the optionList where putAskToBS is less than or equal to SMALLER_PUT_ASK_TO_BS
        defaultOptionShouldNotBeFound("putAskToBS.lessThanOrEqual=" + SMALLER_PUT_ASK_TO_BS);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutAskToBSIsLessThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putAskToBS is less than DEFAULT_PUT_ASK_TO_BS
        defaultOptionShouldNotBeFound("putAskToBS.lessThan=" + DEFAULT_PUT_ASK_TO_BS);

        // Get all the optionList where putAskToBS is less than UPDATED_PUT_ASK_TO_BS
        defaultOptionShouldBeFound("putAskToBS.lessThan=" + UPDATED_PUT_ASK_TO_BS);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutAskToBSIsGreaterThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putAskToBS is greater than DEFAULT_PUT_ASK_TO_BS
        defaultOptionShouldNotBeFound("putAskToBS.greaterThan=" + DEFAULT_PUT_ASK_TO_BS);

        // Get all the optionList where putAskToBS is greater than SMALLER_PUT_ASK_TO_BS
        defaultOptionShouldBeFound("putAskToBS.greaterThan=" + SMALLER_PUT_ASK_TO_BS);
    }


    @Test
    @Transactional
    public void getAllOptionsByCallLeverageIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callLeverage equals to DEFAULT_CALL_LEVERAGE
        defaultOptionShouldBeFound("callLeverage.equals=" + DEFAULT_CALL_LEVERAGE);

        // Get all the optionList where callLeverage equals to UPDATED_CALL_LEVERAGE
        defaultOptionShouldNotBeFound("callLeverage.equals=" + UPDATED_CALL_LEVERAGE);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallLeverageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callLeverage not equals to DEFAULT_CALL_LEVERAGE
        defaultOptionShouldNotBeFound("callLeverage.notEquals=" + DEFAULT_CALL_LEVERAGE);

        // Get all the optionList where callLeverage not equals to UPDATED_CALL_LEVERAGE
        defaultOptionShouldBeFound("callLeverage.notEquals=" + UPDATED_CALL_LEVERAGE);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallLeverageIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callLeverage in DEFAULT_CALL_LEVERAGE or UPDATED_CALL_LEVERAGE
        defaultOptionShouldBeFound("callLeverage.in=" + DEFAULT_CALL_LEVERAGE + "," + UPDATED_CALL_LEVERAGE);

        // Get all the optionList where callLeverage equals to UPDATED_CALL_LEVERAGE
        defaultOptionShouldNotBeFound("callLeverage.in=" + UPDATED_CALL_LEVERAGE);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallLeverageIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callLeverage is not null
        defaultOptionShouldBeFound("callLeverage.specified=true");

        // Get all the optionList where callLeverage is null
        defaultOptionShouldNotBeFound("callLeverage.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionsByCallLeverageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callLeverage is greater than or equal to DEFAULT_CALL_LEVERAGE
        defaultOptionShouldBeFound("callLeverage.greaterThanOrEqual=" + DEFAULT_CALL_LEVERAGE);

        // Get all the optionList where callLeverage is greater than or equal to UPDATED_CALL_LEVERAGE
        defaultOptionShouldNotBeFound("callLeverage.greaterThanOrEqual=" + UPDATED_CALL_LEVERAGE);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallLeverageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callLeverage is less than or equal to DEFAULT_CALL_LEVERAGE
        defaultOptionShouldBeFound("callLeverage.lessThanOrEqual=" + DEFAULT_CALL_LEVERAGE);

        // Get all the optionList where callLeverage is less than or equal to SMALLER_CALL_LEVERAGE
        defaultOptionShouldNotBeFound("callLeverage.lessThanOrEqual=" + SMALLER_CALL_LEVERAGE);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallLeverageIsLessThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callLeverage is less than DEFAULT_CALL_LEVERAGE
        defaultOptionShouldNotBeFound("callLeverage.lessThan=" + DEFAULT_CALL_LEVERAGE);

        // Get all the optionList where callLeverage is less than UPDATED_CALL_LEVERAGE
        defaultOptionShouldBeFound("callLeverage.lessThan=" + UPDATED_CALL_LEVERAGE);
    }

    @Test
    @Transactional
    public void getAllOptionsByCallLeverageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where callLeverage is greater than DEFAULT_CALL_LEVERAGE
        defaultOptionShouldNotBeFound("callLeverage.greaterThan=" + DEFAULT_CALL_LEVERAGE);

        // Get all the optionList where callLeverage is greater than SMALLER_CALL_LEVERAGE
        defaultOptionShouldBeFound("callLeverage.greaterThan=" + SMALLER_CALL_LEVERAGE);
    }


    @Test
    @Transactional
    public void getAllOptionsByPutLeverageIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putLeverage equals to DEFAULT_PUT_LEVERAGE
        defaultOptionShouldBeFound("putLeverage.equals=" + DEFAULT_PUT_LEVERAGE);

        // Get all the optionList where putLeverage equals to UPDATED_PUT_LEVERAGE
        defaultOptionShouldNotBeFound("putLeverage.equals=" + UPDATED_PUT_LEVERAGE);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutLeverageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putLeverage not equals to DEFAULT_PUT_LEVERAGE
        defaultOptionShouldNotBeFound("putLeverage.notEquals=" + DEFAULT_PUT_LEVERAGE);

        // Get all the optionList where putLeverage not equals to UPDATED_PUT_LEVERAGE
        defaultOptionShouldBeFound("putLeverage.notEquals=" + UPDATED_PUT_LEVERAGE);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutLeverageIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putLeverage in DEFAULT_PUT_LEVERAGE or UPDATED_PUT_LEVERAGE
        defaultOptionShouldBeFound("putLeverage.in=" + DEFAULT_PUT_LEVERAGE + "," + UPDATED_PUT_LEVERAGE);

        // Get all the optionList where putLeverage equals to UPDATED_PUT_LEVERAGE
        defaultOptionShouldNotBeFound("putLeverage.in=" + UPDATED_PUT_LEVERAGE);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutLeverageIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putLeverage is not null
        defaultOptionShouldBeFound("putLeverage.specified=true");

        // Get all the optionList where putLeverage is null
        defaultOptionShouldNotBeFound("putLeverage.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionsByPutLeverageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putLeverage is greater than or equal to DEFAULT_PUT_LEVERAGE
        defaultOptionShouldBeFound("putLeverage.greaterThanOrEqual=" + DEFAULT_PUT_LEVERAGE);

        // Get all the optionList where putLeverage is greater than or equal to UPDATED_PUT_LEVERAGE
        defaultOptionShouldNotBeFound("putLeverage.greaterThanOrEqual=" + UPDATED_PUT_LEVERAGE);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutLeverageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putLeverage is less than or equal to DEFAULT_PUT_LEVERAGE
        defaultOptionShouldBeFound("putLeverage.lessThanOrEqual=" + DEFAULT_PUT_LEVERAGE);

        // Get all the optionList where putLeverage is less than or equal to SMALLER_PUT_LEVERAGE
        defaultOptionShouldNotBeFound("putLeverage.lessThanOrEqual=" + SMALLER_PUT_LEVERAGE);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutLeverageIsLessThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putLeverage is less than DEFAULT_PUT_LEVERAGE
        defaultOptionShouldNotBeFound("putLeverage.lessThan=" + DEFAULT_PUT_LEVERAGE);

        // Get all the optionList where putLeverage is less than UPDATED_PUT_LEVERAGE
        defaultOptionShouldBeFound("putLeverage.lessThan=" + UPDATED_PUT_LEVERAGE);
    }

    @Test
    @Transactional
    public void getAllOptionsByPutLeverageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where putLeverage is greater than DEFAULT_PUT_LEVERAGE
        defaultOptionShouldNotBeFound("putLeverage.greaterThan=" + DEFAULT_PUT_LEVERAGE);

        // Get all the optionList where putLeverage is greater than SMALLER_PUT_LEVERAGE
        defaultOptionShouldBeFound("putLeverage.greaterThan=" + SMALLER_PUT_LEVERAGE);
    }


    @Test
    @Transactional
    public void getAllOptionsByInstrumentIsEqualToSomething() throws Exception {
        // Get already existing entity
        Instrument instrument = option.getInstrument();
        optionRepository.saveAndFlush(option);
        String instrumentId = instrument.getIsin();

        // Get all the optionList where instrument equals to instrumentId
        defaultOptionShouldBeFound("instrumentId.equals=" + instrumentId);

        // Get all the optionList where instrument equals to instrumentId + 1
        defaultOptionShouldNotBeFound("instrumentId.equals=" + (instrumentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOptionShouldBeFound(String filter) throws Exception {
        restOptionMockMvc.perform(get("/api/options?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(option.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].callIsin").value(hasItem(DEFAULT_CALL_ISIN)))
            .andExpect(jsonPath("$.[*].putIsin").value(hasItem(DEFAULT_PUT_ISIN)))
            .andExpect(jsonPath("$.[*].expDate").value(hasItem(DEFAULT_EXP_DATE.toString())))
            .andExpect(jsonPath("$.[*].strikePrice").value(hasItem(DEFAULT_STRIKE_PRICE)))
            .andExpect(jsonPath("$.[*].contractSize").value(hasItem(DEFAULT_CONTRACT_SIZE)))
            .andExpect(jsonPath("$.[*].callInTheMoney").value(hasItem(DEFAULT_CALL_IN_THE_MONEY.booleanValue())))
            .andExpect(jsonPath("$.[*].callBreakEven").value(hasItem(DEFAULT_CALL_BREAK_EVEN.doubleValue())))
            .andExpect(jsonPath("$.[*].putBreakEven").value(hasItem(DEFAULT_PUT_BREAK_EVEN.doubleValue())))
            .andExpect(jsonPath("$.[*].callAskToBS").value(hasItem(DEFAULT_CALL_ASK_TO_BS.doubleValue())))
            .andExpect(jsonPath("$.[*].putAskToBS").value(hasItem(DEFAULT_PUT_ASK_TO_BS.doubleValue())))
            .andExpect(jsonPath("$.[*].callLeverage").value(hasItem(DEFAULT_CALL_LEVERAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].putLeverage").value(hasItem(DEFAULT_PUT_LEVERAGE.doubleValue())));

        // Check, that the count call also returns 1
        restOptionMockMvc.perform(get("/api/options/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOptionShouldNotBeFound(String filter) throws Exception {
        restOptionMockMvc.perform(get("/api/options?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOptionMockMvc.perform(get("/api/options/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOption() throws Exception {
        // Get the option
        restOptionMockMvc.perform(get("/api/options/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOption() throws Exception {
        // Initialize the database
        optionService.save(option);

        int databaseSizeBeforeUpdate = optionRepository.findAll().size();

        // Update the option
        Option updatedOption = optionRepository.findById(option.getId()).get();
        // Disconnect from session so that the updates on updatedOption are not directly saved in db
        em.detach(updatedOption);
        updatedOption
            .name(UPDATED_NAME)
            .callIsin(UPDATED_CALL_ISIN)
            .putIsin(UPDATED_PUT_ISIN)
            .expDate(UPDATED_EXP_DATE)
            .strikePrice(UPDATED_STRIKE_PRICE)
            .contractSize(UPDATED_CONTRACT_SIZE)
            .callInTheMoney(UPDATED_CALL_IN_THE_MONEY)
            .callBreakEven(UPDATED_CALL_BREAK_EVEN)
            .putBreakEven(UPDATED_PUT_BREAK_EVEN)
            .callAskToBS(UPDATED_CALL_ASK_TO_BS)
            .putAskToBS(UPDATED_PUT_ASK_TO_BS)
            .callLeverage(UPDATED_CALL_LEVERAGE)
            .putLeverage(UPDATED_PUT_LEVERAGE);

        restOptionMockMvc.perform(put("/api/options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOption)))
            .andExpect(status().isOk());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOption.getCallIsin()).isEqualTo(UPDATED_CALL_ISIN);
        assertThat(testOption.getPutIsin()).isEqualTo(UPDATED_PUT_ISIN);
        assertThat(testOption.getExpDate()).isEqualTo(UPDATED_EXP_DATE);
        assertThat(testOption.getStrikePrice()).isEqualTo(UPDATED_STRIKE_PRICE);
        assertThat(testOption.getContractSize()).isEqualTo(UPDATED_CONTRACT_SIZE);
        assertThat(testOption.isCallInTheMoney()).isEqualTo(UPDATED_CALL_IN_THE_MONEY);
        assertThat(testOption.getCallBreakEven()).isEqualTo(UPDATED_CALL_BREAK_EVEN);
        assertThat(testOption.getPutBreakEven()).isEqualTo(UPDATED_PUT_BREAK_EVEN);
        assertThat(testOption.getCallAskToBS()).isEqualTo(UPDATED_CALL_ASK_TO_BS);
        assertThat(testOption.getPutAskToBS()).isEqualTo(UPDATED_PUT_ASK_TO_BS);
        assertThat(testOption.getCallLeverage()).isEqualTo(UPDATED_CALL_LEVERAGE);
        assertThat(testOption.getPutLeverage()).isEqualTo(UPDATED_PUT_LEVERAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();

        // Create the Option

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionMockMvc.perform(put("/api/options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOption() throws Exception {
        // Initialize the database
        optionService.save(option);

        int databaseSizeBeforeDelete = optionRepository.findAll().size();

        // Delete the option
        restOptionMockMvc.perform(delete("/api/options/{id}", option.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
