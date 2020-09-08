package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.ETradeApp;
import com.gitlab.amirmehdi.domain.Option;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.repository.OptionRepository;
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

    private static final Integer DEFAULT_STRIKE_PRICE = 1;
    private static final Integer UPDATED_STRIKE_PRICE = 2;

    private static final Integer DEFAULT_CONTRACT_SIZE = 1;
    private static final Integer UPDATED_CONTRACT_SIZE = 2;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private OptionService optionService;

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
            .contractSize(DEFAULT_CONTRACT_SIZE);
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
            .contractSize(UPDATED_CONTRACT_SIZE);
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
            .andExpect(jsonPath("$.[*].contractSize").value(hasItem(DEFAULT_CONTRACT_SIZE)));
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
            .andExpect(jsonPath("$.contractSize").value(DEFAULT_CONTRACT_SIZE));
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
            .contractSize(UPDATED_CONTRACT_SIZE);

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
