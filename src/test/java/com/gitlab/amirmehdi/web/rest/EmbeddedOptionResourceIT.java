package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.ETradeApp;
import com.gitlab.amirmehdi.domain.EmbeddedOption;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.repository.EmbeddedOptionRepository;
import com.gitlab.amirmehdi.service.EmbeddedOptionQueryService;
import com.gitlab.amirmehdi.service.EmbeddedOptionService;
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
 * Integration tests for the {@link EmbeddedOptionResource} REST controller.
 */
@SpringBootTest(classes = ETradeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class EmbeddedOptionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ISIN = "AAAAAAAAAA";
    private static final String UPDATED_ISIN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXP_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXP_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXP_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_STRIKE_PRICE = 1;
    private static final Integer UPDATED_STRIKE_PRICE = 2;
    private static final Integer SMALLER_STRIKE_PRICE = 1 - 1;

    private static final String DEFAULT_TSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_TSE_ID = "BBBBBBBBBB";

    @Autowired
    private EmbeddedOptionRepository embeddedOptionRepository;

    @Autowired
    private EmbeddedOptionService embeddedOptionService;

    @Autowired
    private EmbeddedOptionQueryService embeddedOptionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmbeddedOptionMockMvc;

    private EmbeddedOption embeddedOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmbeddedOption createEntity(EntityManager em) {
        EmbeddedOption embeddedOption = new EmbeddedOption()
            .name(DEFAULT_NAME)
            .isin(DEFAULT_ISIN)
            .expDate(DEFAULT_EXP_DATE)
            .strikePrice(DEFAULT_STRIKE_PRICE)
            .tseId(DEFAULT_TSE_ID);
        // Add required entity
        Instrument instrument;
        if (TestUtil.findAll(em, Instrument.class).isEmpty()) {
            instrument = InstrumentResourceIT.createEntity(em);
            em.persist(instrument);
            em.flush();
        } else {
            instrument = TestUtil.findAll(em, Instrument.class).get(0);
        }
        embeddedOption.setUnderlyingInstrument(instrument);
        return embeddedOption;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmbeddedOption createUpdatedEntity(EntityManager em) {
        EmbeddedOption embeddedOption = new EmbeddedOption()
            .name(UPDATED_NAME)
            .isin(UPDATED_ISIN)
            .expDate(UPDATED_EXP_DATE)
            .strikePrice(UPDATED_STRIKE_PRICE)
            .tseId(UPDATED_TSE_ID);
        // Add required entity
        Instrument instrument;
        if (TestUtil.findAll(em, Instrument.class).isEmpty()) {
            instrument = InstrumentResourceIT.createUpdatedEntity(em);
            em.persist(instrument);
            em.flush();
        } else {
            instrument = TestUtil.findAll(em, Instrument.class).get(0);
        }
        embeddedOption.setUnderlyingInstrument(instrument);
        return embeddedOption;
    }

    @BeforeEach
    public void initTest() {
        embeddedOption = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmbeddedOption() throws Exception {
        int databaseSizeBeforeCreate = embeddedOptionRepository.findAll().size();

        // Create the EmbeddedOption
        restEmbeddedOptionMockMvc.perform(post("/api/embedded-options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(embeddedOption)))
            .andExpect(status().isCreated());

        // Validate the EmbeddedOption in the database
        List<EmbeddedOption> embeddedOptionList = embeddedOptionRepository.findAll();
        assertThat(embeddedOptionList).hasSize(databaseSizeBeforeCreate + 1);
        EmbeddedOption testEmbeddedOption = embeddedOptionList.get(embeddedOptionList.size() - 1);
        assertThat(testEmbeddedOption.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmbeddedOption.getIsin()).isEqualTo(DEFAULT_ISIN);
        assertThat(testEmbeddedOption.getExpDate()).isEqualTo(DEFAULT_EXP_DATE);
        assertThat(testEmbeddedOption.getStrikePrice()).isEqualTo(DEFAULT_STRIKE_PRICE);
        assertThat(testEmbeddedOption.getTseId()).isEqualTo(DEFAULT_TSE_ID);
    }

    @Test
    @Transactional
    public void createEmbeddedOptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = embeddedOptionRepository.findAll().size();

        // Create the EmbeddedOption with an existing ID
        embeddedOption.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmbeddedOptionMockMvc.perform(post("/api/embedded-options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(embeddedOption)))
            .andExpect(status().isBadRequest());

        // Validate the EmbeddedOption in the database
        List<EmbeddedOption> embeddedOptionList = embeddedOptionRepository.findAll();
        assertThat(embeddedOptionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = embeddedOptionRepository.findAll().size();
        // set the field null
        embeddedOption.setName(null);

        // Create the EmbeddedOption, which fails.

        restEmbeddedOptionMockMvc.perform(post("/api/embedded-options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(embeddedOption)))
            .andExpect(status().isBadRequest());

        List<EmbeddedOption> embeddedOptionList = embeddedOptionRepository.findAll();
        assertThat(embeddedOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsinIsRequired() throws Exception {
        int databaseSizeBeforeTest = embeddedOptionRepository.findAll().size();
        // set the field null
        embeddedOption.setIsin(null);

        // Create the EmbeddedOption, which fails.

        restEmbeddedOptionMockMvc.perform(post("/api/embedded-options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(embeddedOption)))
            .andExpect(status().isBadRequest());

        List<EmbeddedOption> embeddedOptionList = embeddedOptionRepository.findAll();
        assertThat(embeddedOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = embeddedOptionRepository.findAll().size();
        // set the field null
        embeddedOption.setExpDate(null);

        // Create the EmbeddedOption, which fails.

        restEmbeddedOptionMockMvc.perform(post("/api/embedded-options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(embeddedOption)))
            .andExpect(status().isBadRequest());

        List<EmbeddedOption> embeddedOptionList = embeddedOptionRepository.findAll();
        assertThat(embeddedOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStrikePriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = embeddedOptionRepository.findAll().size();
        // set the field null
        embeddedOption.setStrikePrice(null);

        // Create the EmbeddedOption, which fails.

        restEmbeddedOptionMockMvc.perform(post("/api/embedded-options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(embeddedOption)))
            .andExpect(status().isBadRequest());

        List<EmbeddedOption> embeddedOptionList = embeddedOptionRepository.findAll();
        assertThat(embeddedOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptions() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList
        restEmbeddedOptionMockMvc.perform(get("/api/embedded-options?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(embeddedOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isin").value(hasItem(DEFAULT_ISIN)))
            .andExpect(jsonPath("$.[*].expDate").value(hasItem(DEFAULT_EXP_DATE.toString())))
            .andExpect(jsonPath("$.[*].strikePrice").value(hasItem(DEFAULT_STRIKE_PRICE)))
            .andExpect(jsonPath("$.[*].tseId").value(hasItem(DEFAULT_TSE_ID)));
    }

    @Test
    @Transactional
    public void getEmbeddedOption() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get the embeddedOption
        restEmbeddedOptionMockMvc.perform(get("/api/embedded-options/{id}", embeddedOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(embeddedOption.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isin").value(DEFAULT_ISIN))
            .andExpect(jsonPath("$.expDate").value(DEFAULT_EXP_DATE.toString()))
            .andExpect(jsonPath("$.strikePrice").value(DEFAULT_STRIKE_PRICE))
            .andExpect(jsonPath("$.tseId").value(DEFAULT_TSE_ID));
    }


    @Test
    @Transactional
    public void getEmbeddedOptionsByIdFiltering() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        Long id = embeddedOption.getId();

        defaultEmbeddedOptionShouldBeFound("id.equals=" + id);
        defaultEmbeddedOptionShouldNotBeFound("id.notEquals=" + id);

        defaultEmbeddedOptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmbeddedOptionShouldNotBeFound("id.greaterThan=" + id);

        defaultEmbeddedOptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmbeddedOptionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEmbeddedOptionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where name equals to DEFAULT_NAME
        defaultEmbeddedOptionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the embeddedOptionList where name equals to UPDATED_NAME
        defaultEmbeddedOptionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where name not equals to DEFAULT_NAME
        defaultEmbeddedOptionShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the embeddedOptionList where name not equals to UPDATED_NAME
        defaultEmbeddedOptionShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEmbeddedOptionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the embeddedOptionList where name equals to UPDATED_NAME
        defaultEmbeddedOptionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where name is not null
        defaultEmbeddedOptionShouldBeFound("name.specified=true");

        // Get all the embeddedOptionList where name is null
        defaultEmbeddedOptionShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmbeddedOptionsByNameContainsSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where name contains DEFAULT_NAME
        defaultEmbeddedOptionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the embeddedOptionList where name contains UPDATED_NAME
        defaultEmbeddedOptionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where name does not contain DEFAULT_NAME
        defaultEmbeddedOptionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the embeddedOptionList where name does not contain UPDATED_NAME
        defaultEmbeddedOptionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllEmbeddedOptionsByIsinIsEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where isin equals to DEFAULT_ISIN
        defaultEmbeddedOptionShouldBeFound("isin.equals=" + DEFAULT_ISIN);

        // Get all the embeddedOptionList where isin equals to UPDATED_ISIN
        defaultEmbeddedOptionShouldNotBeFound("isin.equals=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByIsinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where isin not equals to DEFAULT_ISIN
        defaultEmbeddedOptionShouldNotBeFound("isin.notEquals=" + DEFAULT_ISIN);

        // Get all the embeddedOptionList where isin not equals to UPDATED_ISIN
        defaultEmbeddedOptionShouldBeFound("isin.notEquals=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByIsinIsInShouldWork() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where isin in DEFAULT_ISIN or UPDATED_ISIN
        defaultEmbeddedOptionShouldBeFound("isin.in=" + DEFAULT_ISIN + "," + UPDATED_ISIN);

        // Get all the embeddedOptionList where isin equals to UPDATED_ISIN
        defaultEmbeddedOptionShouldNotBeFound("isin.in=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByIsinIsNullOrNotNull() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where isin is not null
        defaultEmbeddedOptionShouldBeFound("isin.specified=true");

        // Get all the embeddedOptionList where isin is null
        defaultEmbeddedOptionShouldNotBeFound("isin.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmbeddedOptionsByIsinContainsSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where isin contains DEFAULT_ISIN
        defaultEmbeddedOptionShouldBeFound("isin.contains=" + DEFAULT_ISIN);

        // Get all the embeddedOptionList where isin contains UPDATED_ISIN
        defaultEmbeddedOptionShouldNotBeFound("isin.contains=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByIsinNotContainsSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where isin does not contain DEFAULT_ISIN
        defaultEmbeddedOptionShouldNotBeFound("isin.doesNotContain=" + DEFAULT_ISIN);

        // Get all the embeddedOptionList where isin does not contain UPDATED_ISIN
        defaultEmbeddedOptionShouldBeFound("isin.doesNotContain=" + UPDATED_ISIN);
    }


    @Test
    @Transactional
    public void getAllEmbeddedOptionsByExpDateIsEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where expDate equals to DEFAULT_EXP_DATE
        defaultEmbeddedOptionShouldBeFound("expDate.equals=" + DEFAULT_EXP_DATE);

        // Get all the embeddedOptionList where expDate equals to UPDATED_EXP_DATE
        defaultEmbeddedOptionShouldNotBeFound("expDate.equals=" + UPDATED_EXP_DATE);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByExpDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where expDate not equals to DEFAULT_EXP_DATE
        defaultEmbeddedOptionShouldNotBeFound("expDate.notEquals=" + DEFAULT_EXP_DATE);

        // Get all the embeddedOptionList where expDate not equals to UPDATED_EXP_DATE
        defaultEmbeddedOptionShouldBeFound("expDate.notEquals=" + UPDATED_EXP_DATE);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByExpDateIsInShouldWork() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where expDate in DEFAULT_EXP_DATE or UPDATED_EXP_DATE
        defaultEmbeddedOptionShouldBeFound("expDate.in=" + DEFAULT_EXP_DATE + "," + UPDATED_EXP_DATE);

        // Get all the embeddedOptionList where expDate equals to UPDATED_EXP_DATE
        defaultEmbeddedOptionShouldNotBeFound("expDate.in=" + UPDATED_EXP_DATE);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByExpDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where expDate is not null
        defaultEmbeddedOptionShouldBeFound("expDate.specified=true");

        // Get all the embeddedOptionList where expDate is null
        defaultEmbeddedOptionShouldNotBeFound("expDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByExpDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where expDate is greater than or equal to DEFAULT_EXP_DATE
        defaultEmbeddedOptionShouldBeFound("expDate.greaterThanOrEqual=" + DEFAULT_EXP_DATE);

        // Get all the embeddedOptionList where expDate is greater than or equal to UPDATED_EXP_DATE
        defaultEmbeddedOptionShouldNotBeFound("expDate.greaterThanOrEqual=" + UPDATED_EXP_DATE);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByExpDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where expDate is less than or equal to DEFAULT_EXP_DATE
        defaultEmbeddedOptionShouldBeFound("expDate.lessThanOrEqual=" + DEFAULT_EXP_DATE);

        // Get all the embeddedOptionList where expDate is less than or equal to SMALLER_EXP_DATE
        defaultEmbeddedOptionShouldNotBeFound("expDate.lessThanOrEqual=" + SMALLER_EXP_DATE);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByExpDateIsLessThanSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where expDate is less than DEFAULT_EXP_DATE
        defaultEmbeddedOptionShouldNotBeFound("expDate.lessThan=" + DEFAULT_EXP_DATE);

        // Get all the embeddedOptionList where expDate is less than UPDATED_EXP_DATE
        defaultEmbeddedOptionShouldBeFound("expDate.lessThan=" + UPDATED_EXP_DATE);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByExpDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where expDate is greater than DEFAULT_EXP_DATE
        defaultEmbeddedOptionShouldNotBeFound("expDate.greaterThan=" + DEFAULT_EXP_DATE);

        // Get all the embeddedOptionList where expDate is greater than SMALLER_EXP_DATE
        defaultEmbeddedOptionShouldBeFound("expDate.greaterThan=" + SMALLER_EXP_DATE);
    }


    @Test
    @Transactional
    public void getAllEmbeddedOptionsByStrikePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where strikePrice equals to DEFAULT_STRIKE_PRICE
        defaultEmbeddedOptionShouldBeFound("strikePrice.equals=" + DEFAULT_STRIKE_PRICE);

        // Get all the embeddedOptionList where strikePrice equals to UPDATED_STRIKE_PRICE
        defaultEmbeddedOptionShouldNotBeFound("strikePrice.equals=" + UPDATED_STRIKE_PRICE);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByStrikePriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where strikePrice not equals to DEFAULT_STRIKE_PRICE
        defaultEmbeddedOptionShouldNotBeFound("strikePrice.notEquals=" + DEFAULT_STRIKE_PRICE);

        // Get all the embeddedOptionList where strikePrice not equals to UPDATED_STRIKE_PRICE
        defaultEmbeddedOptionShouldBeFound("strikePrice.notEquals=" + UPDATED_STRIKE_PRICE);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByStrikePriceIsInShouldWork() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where strikePrice in DEFAULT_STRIKE_PRICE or UPDATED_STRIKE_PRICE
        defaultEmbeddedOptionShouldBeFound("strikePrice.in=" + DEFAULT_STRIKE_PRICE + "," + UPDATED_STRIKE_PRICE);

        // Get all the embeddedOptionList where strikePrice equals to UPDATED_STRIKE_PRICE
        defaultEmbeddedOptionShouldNotBeFound("strikePrice.in=" + UPDATED_STRIKE_PRICE);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByStrikePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where strikePrice is not null
        defaultEmbeddedOptionShouldBeFound("strikePrice.specified=true");

        // Get all the embeddedOptionList where strikePrice is null
        defaultEmbeddedOptionShouldNotBeFound("strikePrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByStrikePriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where strikePrice is greater than or equal to DEFAULT_STRIKE_PRICE
        defaultEmbeddedOptionShouldBeFound("strikePrice.greaterThanOrEqual=" + DEFAULT_STRIKE_PRICE);

        // Get all the embeddedOptionList where strikePrice is greater than or equal to UPDATED_STRIKE_PRICE
        defaultEmbeddedOptionShouldNotBeFound("strikePrice.greaterThanOrEqual=" + UPDATED_STRIKE_PRICE);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByStrikePriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where strikePrice is less than or equal to DEFAULT_STRIKE_PRICE
        defaultEmbeddedOptionShouldBeFound("strikePrice.lessThanOrEqual=" + DEFAULT_STRIKE_PRICE);

        // Get all the embeddedOptionList where strikePrice is less than or equal to SMALLER_STRIKE_PRICE
        defaultEmbeddedOptionShouldNotBeFound("strikePrice.lessThanOrEqual=" + SMALLER_STRIKE_PRICE);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByStrikePriceIsLessThanSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where strikePrice is less than DEFAULT_STRIKE_PRICE
        defaultEmbeddedOptionShouldNotBeFound("strikePrice.lessThan=" + DEFAULT_STRIKE_PRICE);

        // Get all the embeddedOptionList where strikePrice is less than UPDATED_STRIKE_PRICE
        defaultEmbeddedOptionShouldBeFound("strikePrice.lessThan=" + UPDATED_STRIKE_PRICE);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByStrikePriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where strikePrice is greater than DEFAULT_STRIKE_PRICE
        defaultEmbeddedOptionShouldNotBeFound("strikePrice.greaterThan=" + DEFAULT_STRIKE_PRICE);

        // Get all the embeddedOptionList where strikePrice is greater than SMALLER_STRIKE_PRICE
        defaultEmbeddedOptionShouldBeFound("strikePrice.greaterThan=" + SMALLER_STRIKE_PRICE);
    }


    @Test
    @Transactional
    public void getAllEmbeddedOptionsByTseIdIsEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where tseId equals to DEFAULT_TSE_ID
        defaultEmbeddedOptionShouldBeFound("tseId.equals=" + DEFAULT_TSE_ID);

        // Get all the embeddedOptionList where tseId equals to UPDATED_TSE_ID
        defaultEmbeddedOptionShouldNotBeFound("tseId.equals=" + UPDATED_TSE_ID);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByTseIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where tseId not equals to DEFAULT_TSE_ID
        defaultEmbeddedOptionShouldNotBeFound("tseId.notEquals=" + DEFAULT_TSE_ID);

        // Get all the embeddedOptionList where tseId not equals to UPDATED_TSE_ID
        defaultEmbeddedOptionShouldBeFound("tseId.notEquals=" + UPDATED_TSE_ID);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByTseIdIsInShouldWork() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where tseId in DEFAULT_TSE_ID or UPDATED_TSE_ID
        defaultEmbeddedOptionShouldBeFound("tseId.in=" + DEFAULT_TSE_ID + "," + UPDATED_TSE_ID);

        // Get all the embeddedOptionList where tseId equals to UPDATED_TSE_ID
        defaultEmbeddedOptionShouldNotBeFound("tseId.in=" + UPDATED_TSE_ID);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByTseIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where tseId is not null
        defaultEmbeddedOptionShouldBeFound("tseId.specified=true");

        // Get all the embeddedOptionList where tseId is null
        defaultEmbeddedOptionShouldNotBeFound("tseId.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmbeddedOptionsByTseIdContainsSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where tseId contains DEFAULT_TSE_ID
        defaultEmbeddedOptionShouldBeFound("tseId.contains=" + DEFAULT_TSE_ID);

        // Get all the embeddedOptionList where tseId contains UPDATED_TSE_ID
        defaultEmbeddedOptionShouldNotBeFound("tseId.contains=" + UPDATED_TSE_ID);
    }

    @Test
    @Transactional
    public void getAllEmbeddedOptionsByTseIdNotContainsSomething() throws Exception {
        // Initialize the database
        embeddedOptionRepository.saveAndFlush(embeddedOption);

        // Get all the embeddedOptionList where tseId does not contain DEFAULT_TSE_ID
        defaultEmbeddedOptionShouldNotBeFound("tseId.doesNotContain=" + DEFAULT_TSE_ID);

        // Get all the embeddedOptionList where tseId does not contain UPDATED_TSE_ID
        defaultEmbeddedOptionShouldBeFound("tseId.doesNotContain=" + UPDATED_TSE_ID);
    }


    @Test
    @Transactional
    public void getAllEmbeddedOptionsByUnderlyingInstrumentIsEqualToSomething() throws Exception {
        // Get already existing entity
        Instrument underlyingInstrument = embeddedOption.getUnderlyingInstrument();
        embeddedOptionRepository.saveAndFlush(embeddedOption);
        String underlyingInstrumentId = underlyingInstrument.getIsin();

        // Get all the embeddedOptionList where underlyingInstrument equals to underlyingInstrumentId
        defaultEmbeddedOptionShouldBeFound("underlyingInstrumentId.equals=" + underlyingInstrumentId);

        // Get all the embeddedOptionList where underlyingInstrument equals to underlyingInstrumentId + 1
        defaultEmbeddedOptionShouldNotBeFound("underlyingInstrumentId.equals=" + (underlyingInstrumentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmbeddedOptionShouldBeFound(String filter) throws Exception {
        restEmbeddedOptionMockMvc.perform(get("/api/embedded-options?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(embeddedOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isin").value(hasItem(DEFAULT_ISIN)))
            .andExpect(jsonPath("$.[*].expDate").value(hasItem(DEFAULT_EXP_DATE.toString())))
            .andExpect(jsonPath("$.[*].strikePrice").value(hasItem(DEFAULT_STRIKE_PRICE)))
            .andExpect(jsonPath("$.[*].tseId").value(hasItem(DEFAULT_TSE_ID)));

        // Check, that the count call also returns 1
        restEmbeddedOptionMockMvc.perform(get("/api/embedded-options/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmbeddedOptionShouldNotBeFound(String filter) throws Exception {
        restEmbeddedOptionMockMvc.perform(get("/api/embedded-options?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmbeddedOptionMockMvc.perform(get("/api/embedded-options/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEmbeddedOption() throws Exception {
        // Get the embeddedOption
        restEmbeddedOptionMockMvc.perform(get("/api/embedded-options/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmbeddedOption() throws Exception {
        // Initialize the database
        embeddedOptionService.save(embeddedOption);

        int databaseSizeBeforeUpdate = embeddedOptionRepository.findAll().size();

        // Update the embeddedOption
        EmbeddedOption updatedEmbeddedOption = embeddedOptionRepository.findById(embeddedOption.getId()).get();
        // Disconnect from session so that the updates on updatedEmbeddedOption are not directly saved in db
        em.detach(updatedEmbeddedOption);
        updatedEmbeddedOption
            .name(UPDATED_NAME)
            .isin(UPDATED_ISIN)
            .expDate(UPDATED_EXP_DATE)
            .strikePrice(UPDATED_STRIKE_PRICE)
            .tseId(UPDATED_TSE_ID);

        restEmbeddedOptionMockMvc.perform(put("/api/embedded-options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmbeddedOption)))
            .andExpect(status().isOk());

        // Validate the EmbeddedOption in the database
        List<EmbeddedOption> embeddedOptionList = embeddedOptionRepository.findAll();
        assertThat(embeddedOptionList).hasSize(databaseSizeBeforeUpdate);
        EmbeddedOption testEmbeddedOption = embeddedOptionList.get(embeddedOptionList.size() - 1);
        assertThat(testEmbeddedOption.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmbeddedOption.getIsin()).isEqualTo(UPDATED_ISIN);
        assertThat(testEmbeddedOption.getExpDate()).isEqualTo(UPDATED_EXP_DATE);
        assertThat(testEmbeddedOption.getStrikePrice()).isEqualTo(UPDATED_STRIKE_PRICE);
        assertThat(testEmbeddedOption.getTseId()).isEqualTo(UPDATED_TSE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingEmbeddedOption() throws Exception {
        int databaseSizeBeforeUpdate = embeddedOptionRepository.findAll().size();

        // Create the EmbeddedOption

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmbeddedOptionMockMvc.perform(put("/api/embedded-options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(embeddedOption)))
            .andExpect(status().isBadRequest());

        // Validate the EmbeddedOption in the database
        List<EmbeddedOption> embeddedOptionList = embeddedOptionRepository.findAll();
        assertThat(embeddedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmbeddedOption() throws Exception {
        // Initialize the database
        embeddedOptionService.save(embeddedOption);

        int databaseSizeBeforeDelete = embeddedOptionRepository.findAll().size();

        // Delete the embeddedOption
        restEmbeddedOptionMockMvc.perform(delete("/api/embedded-options/{id}", embeddedOption.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmbeddedOption> embeddedOptionList = embeddedOptionRepository.findAll();
        assertThat(embeddedOptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
