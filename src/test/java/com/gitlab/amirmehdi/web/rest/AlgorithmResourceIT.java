package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.ETradeApp;
import com.gitlab.amirmehdi.domain.Algorithm;
import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.enumeration.AlgorithmSide;
import com.gitlab.amirmehdi.domain.enumeration.AlgorithmState;
import com.gitlab.amirmehdi.repository.AlgorithmRepository;
import com.gitlab.amirmehdi.service.AlgorithmQueryService;
import com.gitlab.amirmehdi.service.AlgorithmService;
import com.gitlab.amirmehdi.service.dto.AlgorithmDTO;
import com.gitlab.amirmehdi.service.mapper.AlgorithmMapper;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Integration tests for the {@link AlgorithmResource} REST controller.
 */
@SpringBootTest(classes = ETradeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class AlgorithmResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final AlgorithmSide DEFAULT_SIDE = AlgorithmSide.BUY;
    private static final AlgorithmSide UPDATED_SIDE = AlgorithmSide.SELL;

    private static final AlgorithmState DEFAULT_STATE = AlgorithmState.ENABLE;
    private static final AlgorithmState UPDATED_STATE = AlgorithmState.DISABLE;

    private static final String DEFAULT_INPUT = "AAAAAAAAAA";
    private static final String UPDATED_INPUT = "BBBBBBBBBB";

    private static final Integer DEFAULT_TRADE_VOLUME_LIMIT = 1;
    private static final Integer UPDATED_TRADE_VOLUME_LIMIT = 2;
    private static final Integer SMALLER_TRADE_VOLUME_LIMIT = 1 - 1;

    private static final Long DEFAULT_TRADE_VALUE_LIMIT = 1L;
    private static final Long UPDATED_TRADE_VALUE_LIMIT = 2L;
    private static final Long SMALLER_TRADE_VALUE_LIMIT = 1L - 1L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ISINS = "AAAAAAAAAA";
    private static final String UPDATED_ISINS = "BBBBBBBBBB";

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Autowired
    private AlgorithmMapper algorithmMapper;

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private AlgorithmQueryService algorithmQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlgorithmMockMvc;

    private Algorithm algorithm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Algorithm createEntity(EntityManager em) {
        Algorithm algorithm = new Algorithm()
            .type(DEFAULT_TYPE)
            .side(DEFAULT_SIDE)
            .state(DEFAULT_STATE)
            .input(DEFAULT_INPUT)
            .tradeVolumeLimit(DEFAULT_TRADE_VOLUME_LIMIT)
            .tradeValueLimit(DEFAULT_TRADE_VALUE_LIMIT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .isins(DEFAULT_ISINS);
        return algorithm;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Algorithm createUpdatedEntity(EntityManager em) {
        Algorithm algorithm = new Algorithm()
            .type(UPDATED_TYPE)
            .side(UPDATED_SIDE)
            .state(UPDATED_STATE)
            .input(UPDATED_INPUT)
            .tradeVolumeLimit(UPDATED_TRADE_VOLUME_LIMIT)
            .tradeValueLimit(UPDATED_TRADE_VALUE_LIMIT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isins(UPDATED_ISINS);
        return algorithm;
    }

    @BeforeEach
    public void initTest() {
        algorithm = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlgorithm() throws Exception {
        int databaseSizeBeforeCreate = algorithmRepository.findAll().size();

        // Create the Algorithm
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);
        restAlgorithmMockMvc.perform(post("/api/algorithms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isCreated());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeCreate + 1);
        Algorithm testAlgorithm = algorithmList.get(algorithmList.size() - 1);
        assertThat(testAlgorithm.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAlgorithm.getSide()).isEqualTo(DEFAULT_SIDE);
        assertThat(testAlgorithm.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testAlgorithm.getInput()).isEqualTo(DEFAULT_INPUT);
        assertThat(testAlgorithm.getTradeVolumeLimit()).isEqualTo(DEFAULT_TRADE_VOLUME_LIMIT);
        assertThat(testAlgorithm.getTradeValueLimit()).isEqualTo(DEFAULT_TRADE_VALUE_LIMIT);
        assertThat(testAlgorithm.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAlgorithm.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testAlgorithm.getIsins()).isEqualTo(DEFAULT_ISINS);
    }

    @Test
    @Transactional
    public void createAlgorithmWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = algorithmRepository.findAll().size();

        // Create the Algorithm with an existing ID
        algorithm.setId(1L);
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlgorithmMockMvc.perform(post("/api/algorithms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = algorithmRepository.findAll().size();
        // set the field null
        algorithm.setType(null);

        // Create the Algorithm, which fails.
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        restAlgorithmMockMvc.perform(post("/api/algorithms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isBadRequest());

        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSideIsRequired() throws Exception {
        int databaseSizeBeforeTest = algorithmRepository.findAll().size();
        // set the field null
        algorithm.setSide(null);

        // Create the Algorithm, which fails.
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        restAlgorithmMockMvc.perform(post("/api/algorithms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isBadRequest());

        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = algorithmRepository.findAll().size();
        // set the field null
        algorithm.setState(null);

        // Create the Algorithm, which fails.
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        restAlgorithmMockMvc.perform(post("/api/algorithms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isBadRequest());

        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsinsIsRequired() throws Exception {
        int databaseSizeBeforeTest = algorithmRepository.findAll().size();
        // set the field null
        algorithm.setIsins(null);

        // Create the Algorithm, which fails.
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        restAlgorithmMockMvc.perform(post("/api/algorithms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isBadRequest());

        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAlgorithms() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList
        restAlgorithmMockMvc.perform(get("/api/algorithms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(algorithm.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].input").value(hasItem(DEFAULT_INPUT)))
            .andExpect(jsonPath("$.[*].tradeVolumeLimit").value(hasItem(DEFAULT_TRADE_VOLUME_LIMIT)))
            .andExpect(jsonPath("$.[*].tradeValueLimit").value(hasItem(DEFAULT_TRADE_VALUE_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isins").value(hasItem(DEFAULT_ISINS)));
    }

    @Test
    @Transactional
    public void getAlgorithm() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get the algorithm
        restAlgorithmMockMvc.perform(get("/api/algorithms/{id}", algorithm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(algorithm.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.side").value(DEFAULT_SIDE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.input").value(DEFAULT_INPUT))
            .andExpect(jsonPath("$.tradeVolumeLimit").value(DEFAULT_TRADE_VOLUME_LIMIT))
            .andExpect(jsonPath("$.tradeValueLimit").value(DEFAULT_TRADE_VALUE_LIMIT.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.isins").value(DEFAULT_ISINS));
    }


    @Test
    @Transactional
    public void getAlgorithmsByIdFiltering() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        Long id = algorithm.getId();

        defaultAlgorithmShouldBeFound("id.equals=" + id);
        defaultAlgorithmShouldNotBeFound("id.notEquals=" + id);

        defaultAlgorithmShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAlgorithmShouldNotBeFound("id.greaterThan=" + id);

        defaultAlgorithmShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAlgorithmShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAlgorithmsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where type equals to DEFAULT_TYPE
        defaultAlgorithmShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the algorithmList where type equals to UPDATED_TYPE
        defaultAlgorithmShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where type not equals to DEFAULT_TYPE
        defaultAlgorithmShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the algorithmList where type not equals to UPDATED_TYPE
        defaultAlgorithmShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAlgorithmShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the algorithmList where type equals to UPDATED_TYPE
        defaultAlgorithmShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where type is not null
        defaultAlgorithmShouldBeFound("type.specified=true");

        // Get all the algorithmList where type is null
        defaultAlgorithmShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlgorithmsByTypeContainsSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where type contains DEFAULT_TYPE
        defaultAlgorithmShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the algorithmList where type contains UPDATED_TYPE
        defaultAlgorithmShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where type does not contain DEFAULT_TYPE
        defaultAlgorithmShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the algorithmList where type does not contain UPDATED_TYPE
        defaultAlgorithmShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllAlgorithmsBySideIsEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where side equals to DEFAULT_SIDE
        defaultAlgorithmShouldBeFound("side.equals=" + DEFAULT_SIDE);

        // Get all the algorithmList where side equals to UPDATED_SIDE
        defaultAlgorithmShouldNotBeFound("side.equals=" + UPDATED_SIDE);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsBySideIsNotEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where side not equals to DEFAULT_SIDE
        defaultAlgorithmShouldNotBeFound("side.notEquals=" + DEFAULT_SIDE);

        // Get all the algorithmList where side not equals to UPDATED_SIDE
        defaultAlgorithmShouldBeFound("side.notEquals=" + UPDATED_SIDE);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsBySideIsInShouldWork() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where side in DEFAULT_SIDE or UPDATED_SIDE
        defaultAlgorithmShouldBeFound("side.in=" + DEFAULT_SIDE + "," + UPDATED_SIDE);

        // Get all the algorithmList where side equals to UPDATED_SIDE
        defaultAlgorithmShouldNotBeFound("side.in=" + UPDATED_SIDE);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsBySideIsNullOrNotNull() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where side is not null
        defaultAlgorithmShouldBeFound("side.specified=true");

        // Get all the algorithmList where side is null
        defaultAlgorithmShouldNotBeFound("side.specified=false");
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where state equals to DEFAULT_STATE
        defaultAlgorithmShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the algorithmList where state equals to UPDATED_STATE
        defaultAlgorithmShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where state not equals to DEFAULT_STATE
        defaultAlgorithmShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the algorithmList where state not equals to UPDATED_STATE
        defaultAlgorithmShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where state in DEFAULT_STATE or UPDATED_STATE
        defaultAlgorithmShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the algorithmList where state equals to UPDATED_STATE
        defaultAlgorithmShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where state is not null
        defaultAlgorithmShouldBeFound("state.specified=true");

        // Get all the algorithmList where state is null
        defaultAlgorithmShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByInputIsEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where input equals to DEFAULT_INPUT
        defaultAlgorithmShouldBeFound("input.equals=" + DEFAULT_INPUT);

        // Get all the algorithmList where input equals to UPDATED_INPUT
        defaultAlgorithmShouldNotBeFound("input.equals=" + UPDATED_INPUT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByInputIsNotEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where input not equals to DEFAULT_INPUT
        defaultAlgorithmShouldNotBeFound("input.notEquals=" + DEFAULT_INPUT);

        // Get all the algorithmList where input not equals to UPDATED_INPUT
        defaultAlgorithmShouldBeFound("input.notEquals=" + UPDATED_INPUT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByInputIsInShouldWork() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where input in DEFAULT_INPUT or UPDATED_INPUT
        defaultAlgorithmShouldBeFound("input.in=" + DEFAULT_INPUT + "," + UPDATED_INPUT);

        // Get all the algorithmList where input equals to UPDATED_INPUT
        defaultAlgorithmShouldNotBeFound("input.in=" + UPDATED_INPUT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByInputIsNullOrNotNull() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where input is not null
        defaultAlgorithmShouldBeFound("input.specified=true");

        // Get all the algorithmList where input is null
        defaultAlgorithmShouldNotBeFound("input.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlgorithmsByInputContainsSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where input contains DEFAULT_INPUT
        defaultAlgorithmShouldBeFound("input.contains=" + DEFAULT_INPUT);

        // Get all the algorithmList where input contains UPDATED_INPUT
        defaultAlgorithmShouldNotBeFound("input.contains=" + UPDATED_INPUT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByInputNotContainsSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where input does not contain DEFAULT_INPUT
        defaultAlgorithmShouldNotBeFound("input.doesNotContain=" + DEFAULT_INPUT);

        // Get all the algorithmList where input does not contain UPDATED_INPUT
        defaultAlgorithmShouldBeFound("input.doesNotContain=" + UPDATED_INPUT);
    }


    @Test
    @Transactional
    public void getAllAlgorithmsByTradeVolumeLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeVolumeLimit equals to DEFAULT_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldBeFound("tradeVolumeLimit.equals=" + DEFAULT_TRADE_VOLUME_LIMIT);

        // Get all the algorithmList where tradeVolumeLimit equals to UPDATED_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeVolumeLimit.equals=" + UPDATED_TRADE_VOLUME_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeVolumeLimitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeVolumeLimit not equals to DEFAULT_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeVolumeLimit.notEquals=" + DEFAULT_TRADE_VOLUME_LIMIT);

        // Get all the algorithmList where tradeVolumeLimit not equals to UPDATED_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldBeFound("tradeVolumeLimit.notEquals=" + UPDATED_TRADE_VOLUME_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeVolumeLimitIsInShouldWork() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeVolumeLimit in DEFAULT_TRADE_VOLUME_LIMIT or UPDATED_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldBeFound("tradeVolumeLimit.in=" + DEFAULT_TRADE_VOLUME_LIMIT + "," + UPDATED_TRADE_VOLUME_LIMIT);

        // Get all the algorithmList where tradeVolumeLimit equals to UPDATED_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeVolumeLimit.in=" + UPDATED_TRADE_VOLUME_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeVolumeLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeVolumeLimit is not null
        defaultAlgorithmShouldBeFound("tradeVolumeLimit.specified=true");

        // Get all the algorithmList where tradeVolumeLimit is null
        defaultAlgorithmShouldNotBeFound("tradeVolumeLimit.specified=false");
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeVolumeLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeVolumeLimit is greater than or equal to DEFAULT_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldBeFound("tradeVolumeLimit.greaterThanOrEqual=" + DEFAULT_TRADE_VOLUME_LIMIT);

        // Get all the algorithmList where tradeVolumeLimit is greater than or equal to UPDATED_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeVolumeLimit.greaterThanOrEqual=" + UPDATED_TRADE_VOLUME_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeVolumeLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeVolumeLimit is less than or equal to DEFAULT_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldBeFound("tradeVolumeLimit.lessThanOrEqual=" + DEFAULT_TRADE_VOLUME_LIMIT);

        // Get all the algorithmList where tradeVolumeLimit is less than or equal to SMALLER_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeVolumeLimit.lessThanOrEqual=" + SMALLER_TRADE_VOLUME_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeVolumeLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeVolumeLimit is less than DEFAULT_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeVolumeLimit.lessThan=" + DEFAULT_TRADE_VOLUME_LIMIT);

        // Get all the algorithmList where tradeVolumeLimit is less than UPDATED_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldBeFound("tradeVolumeLimit.lessThan=" + UPDATED_TRADE_VOLUME_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeVolumeLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeVolumeLimit is greater than DEFAULT_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeVolumeLimit.greaterThan=" + DEFAULT_TRADE_VOLUME_LIMIT);

        // Get all the algorithmList where tradeVolumeLimit is greater than SMALLER_TRADE_VOLUME_LIMIT
        defaultAlgorithmShouldBeFound("tradeVolumeLimit.greaterThan=" + SMALLER_TRADE_VOLUME_LIMIT);
    }


    @Test
    @Transactional
    public void getAllAlgorithmsByTradeValueLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeValueLimit equals to DEFAULT_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldBeFound("tradeValueLimit.equals=" + DEFAULT_TRADE_VALUE_LIMIT);

        // Get all the algorithmList where tradeValueLimit equals to UPDATED_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeValueLimit.equals=" + UPDATED_TRADE_VALUE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeValueLimitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeValueLimit not equals to DEFAULT_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeValueLimit.notEquals=" + DEFAULT_TRADE_VALUE_LIMIT);

        // Get all the algorithmList where tradeValueLimit not equals to UPDATED_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldBeFound("tradeValueLimit.notEquals=" + UPDATED_TRADE_VALUE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeValueLimitIsInShouldWork() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeValueLimit in DEFAULT_TRADE_VALUE_LIMIT or UPDATED_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldBeFound("tradeValueLimit.in=" + DEFAULT_TRADE_VALUE_LIMIT + "," + UPDATED_TRADE_VALUE_LIMIT);

        // Get all the algorithmList where tradeValueLimit equals to UPDATED_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeValueLimit.in=" + UPDATED_TRADE_VALUE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeValueLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeValueLimit is not null
        defaultAlgorithmShouldBeFound("tradeValueLimit.specified=true");

        // Get all the algorithmList where tradeValueLimit is null
        defaultAlgorithmShouldNotBeFound("tradeValueLimit.specified=false");
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeValueLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeValueLimit is greater than or equal to DEFAULT_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldBeFound("tradeValueLimit.greaterThanOrEqual=" + DEFAULT_TRADE_VALUE_LIMIT);

        // Get all the algorithmList where tradeValueLimit is greater than or equal to UPDATED_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeValueLimit.greaterThanOrEqual=" + UPDATED_TRADE_VALUE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeValueLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeValueLimit is less than or equal to DEFAULT_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldBeFound("tradeValueLimit.lessThanOrEqual=" + DEFAULT_TRADE_VALUE_LIMIT);

        // Get all the algorithmList where tradeValueLimit is less than or equal to SMALLER_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeValueLimit.lessThanOrEqual=" + SMALLER_TRADE_VALUE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeValueLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeValueLimit is less than DEFAULT_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeValueLimit.lessThan=" + DEFAULT_TRADE_VALUE_LIMIT);

        // Get all the algorithmList where tradeValueLimit is less than UPDATED_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldBeFound("tradeValueLimit.lessThan=" + UPDATED_TRADE_VALUE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByTradeValueLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where tradeValueLimit is greater than DEFAULT_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldNotBeFound("tradeValueLimit.greaterThan=" + DEFAULT_TRADE_VALUE_LIMIT);

        // Get all the algorithmList where tradeValueLimit is greater than SMALLER_TRADE_VALUE_LIMIT
        defaultAlgorithmShouldBeFound("tradeValueLimit.greaterThan=" + SMALLER_TRADE_VALUE_LIMIT);
    }


    @Test
    @Transactional
    public void getAllAlgorithmsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where createdAt equals to DEFAULT_CREATED_AT
        defaultAlgorithmShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the algorithmList where createdAt equals to UPDATED_CREATED_AT
        defaultAlgorithmShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where createdAt not equals to DEFAULT_CREATED_AT
        defaultAlgorithmShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the algorithmList where createdAt not equals to UPDATED_CREATED_AT
        defaultAlgorithmShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultAlgorithmShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the algorithmList where createdAt equals to UPDATED_CREATED_AT
        defaultAlgorithmShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where createdAt is not null
        defaultAlgorithmShouldBeFound("createdAt.specified=true");

        // Get all the algorithmList where createdAt is null
        defaultAlgorithmShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultAlgorithmShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the algorithmList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAlgorithmShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultAlgorithmShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the algorithmList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultAlgorithmShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultAlgorithmShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the algorithmList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAlgorithmShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where updatedAt is not null
        defaultAlgorithmShouldBeFound("updatedAt.specified=true");

        // Get all the algorithmList where updatedAt is null
        defaultAlgorithmShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByIsinsIsEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where isins equals to DEFAULT_ISINS
        defaultAlgorithmShouldBeFound("isins.equals=" + DEFAULT_ISINS);

        // Get all the algorithmList where isins equals to UPDATED_ISINS
        defaultAlgorithmShouldNotBeFound("isins.equals=" + UPDATED_ISINS);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByIsinsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where isins not equals to DEFAULT_ISINS
        defaultAlgorithmShouldNotBeFound("isins.notEquals=" + DEFAULT_ISINS);

        // Get all the algorithmList where isins not equals to UPDATED_ISINS
        defaultAlgorithmShouldBeFound("isins.notEquals=" + UPDATED_ISINS);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByIsinsIsInShouldWork() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where isins in DEFAULT_ISINS or UPDATED_ISINS
        defaultAlgorithmShouldBeFound("isins.in=" + DEFAULT_ISINS + "," + UPDATED_ISINS);

        // Get all the algorithmList where isins equals to UPDATED_ISINS
        defaultAlgorithmShouldNotBeFound("isins.in=" + UPDATED_ISINS);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByIsinsIsNullOrNotNull() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where isins is not null
        defaultAlgorithmShouldBeFound("isins.specified=true");

        // Get all the algorithmList where isins is null
        defaultAlgorithmShouldNotBeFound("isins.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlgorithmsByIsinsContainsSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where isins contains DEFAULT_ISINS
        defaultAlgorithmShouldBeFound("isins.contains=" + DEFAULT_ISINS);

        // Get all the algorithmList where isins contains UPDATED_ISINS
        defaultAlgorithmShouldNotBeFound("isins.contains=" + UPDATED_ISINS);
    }

    @Test
    @Transactional
    public void getAllAlgorithmsByIsinsNotContainsSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where isins does not contain DEFAULT_ISINS
        defaultAlgorithmShouldNotBeFound("isins.doesNotContain=" + DEFAULT_ISINS);

        // Get all the algorithmList where isins does not contain UPDATED_ISINS
        defaultAlgorithmShouldBeFound("isins.doesNotContain=" + UPDATED_ISINS);
    }


    @Test
    @Transactional
    public void getAllAlgorithmsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);
        Order order = OrderResourceIT.createEntity(em);
        em.persist(order);
        em.flush();
        algorithm.addOrder(order);
        algorithmRepository.saveAndFlush(algorithm);
        Long orderId = order.getId();

        // Get all the algorithmList where order equals to orderId
        defaultAlgorithmShouldBeFound("orderId.equals=" + orderId);

        // Get all the algorithmList where order equals to orderId + 1
        defaultAlgorithmShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlgorithmShouldBeFound(String filter) throws Exception {
        restAlgorithmMockMvc.perform(get("/api/algorithms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(algorithm.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].input").value(hasItem(DEFAULT_INPUT)))
            .andExpect(jsonPath("$.[*].tradeVolumeLimit").value(hasItem(DEFAULT_TRADE_VOLUME_LIMIT)))
            .andExpect(jsonPath("$.[*].tradeValueLimit").value(hasItem(DEFAULT_TRADE_VALUE_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isins").value(hasItem(DEFAULT_ISINS)));

        // Check, that the count call also returns 1
        restAlgorithmMockMvc.perform(get("/api/algorithms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlgorithmShouldNotBeFound(String filter) throws Exception {
        restAlgorithmMockMvc.perform(get("/api/algorithms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlgorithmMockMvc.perform(get("/api/algorithms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAlgorithm() throws Exception {
        // Get the algorithm
        restAlgorithmMockMvc.perform(get("/api/algorithms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlgorithm() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();

        // Update the algorithm
        Algorithm updatedAlgorithm = algorithmRepository.findById(algorithm.getId()).get();
        // Disconnect from session so that the updates on updatedAlgorithm are not directly saved in db
        em.detach(updatedAlgorithm);
        updatedAlgorithm
            .type(UPDATED_TYPE)
            .side(UPDATED_SIDE)
            .state(UPDATED_STATE)
            .input(UPDATED_INPUT)
            .tradeVolumeLimit(UPDATED_TRADE_VOLUME_LIMIT)
            .tradeValueLimit(UPDATED_TRADE_VALUE_LIMIT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isins(UPDATED_ISINS);
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(updatedAlgorithm);

        restAlgorithmMockMvc.perform(put("/api/algorithms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isOk());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);
        Algorithm testAlgorithm = algorithmList.get(algorithmList.size() - 1);
        assertThat(testAlgorithm.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAlgorithm.getSide()).isEqualTo(UPDATED_SIDE);
        assertThat(testAlgorithm.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAlgorithm.getInput()).isEqualTo(UPDATED_INPUT);
        assertThat(testAlgorithm.getTradeVolumeLimit()).isEqualTo(UPDATED_TRADE_VOLUME_LIMIT);
        assertThat(testAlgorithm.getTradeValueLimit()).isEqualTo(UPDATED_TRADE_VALUE_LIMIT);
        assertThat(testAlgorithm.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAlgorithm.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testAlgorithm.getIsins()).isEqualTo(UPDATED_ISINS);
    }

    @Test
    @Transactional
    public void updateNonExistingAlgorithm() throws Exception {
        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();

        // Create the Algorithm
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlgorithmMockMvc.perform(put("/api/algorithms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAlgorithm() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        int databaseSizeBeforeDelete = algorithmRepository.findAll().size();

        // Delete the algorithm
        restAlgorithmMockMvc.perform(delete("/api/algorithms/{id}", algorithm.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
