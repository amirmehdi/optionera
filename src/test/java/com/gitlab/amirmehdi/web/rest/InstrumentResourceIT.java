package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.ETradeApp;
import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.repository.InstrumentRepository;

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
 * Integration tests for the {@link InstrumentResource} REST controller.
 */
@SpringBootTest(classes = ETradeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class InstrumentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ISIN = "AAAAAAAAAA";
    private static final String UPDATED_ISIN = "BBBBBBBBBB";

    private static final String DEFAULT_TSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_TSE_ID = "BBBBBBBBBB";

    private static final Double DEFAULT_VOLATILITY_30 = 1D;
    private static final Double UPDATED_VOLATILITY_30 = 2D;

    private static final Double DEFAULT_VOLATILITY_60 = 1D;
    private static final Double UPDATED_VOLATILITY_60 = 2D;

    private static final Double DEFAULT_VOLATILITY_90 = 1D;
    private static final Double UPDATED_VOLATILITY_90 = 2D;

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInstrumentMockMvc;

    private Instrument instrument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instrument createEntity(EntityManager em) {
        Instrument instrument = new Instrument()
            .name(DEFAULT_NAME)
            .isin(DEFAULT_ISIN)
            .tseId(DEFAULT_TSE_ID)
            .volatility30(DEFAULT_VOLATILITY_30)
            .volatility60(DEFAULT_VOLATILITY_60)
            .volatility90(DEFAULT_VOLATILITY_90)
            .updatedAt(DEFAULT_UPDATED_AT);
        return instrument;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instrument createUpdatedEntity(EntityManager em) {
        Instrument instrument = new Instrument()
            .name(UPDATED_NAME)
            .isin(UPDATED_ISIN)
            .tseId(UPDATED_TSE_ID)
            .volatility30(UPDATED_VOLATILITY_30)
            .volatility60(UPDATED_VOLATILITY_60)
            .volatility90(UPDATED_VOLATILITY_90)
            .updatedAt(UPDATED_UPDATED_AT);
        return instrument;
    }

    @BeforeEach
    public void initTest() {
        instrument = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstrument() throws Exception {
        int databaseSizeBeforeCreate = instrumentRepository.findAll().size();

        // Create the Instrument
        restInstrumentMockMvc.perform(post("/api/instruments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(instrument)))
            .andExpect(status().isCreated());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeCreate + 1);
        Instrument testInstrument = instrumentList.get(instrumentList.size() - 1);
        assertThat(testInstrument.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstrument.getIsin()).isEqualTo(DEFAULT_ISIN);
        assertThat(testInstrument.getTseId()).isEqualTo(DEFAULT_TSE_ID);
        assertThat(testInstrument.getVolatility30()).isEqualTo(DEFAULT_VOLATILITY_30);
        assertThat(testInstrument.getVolatility60()).isEqualTo(DEFAULT_VOLATILITY_60);
        assertThat(testInstrument.getVolatility90()).isEqualTo(DEFAULT_VOLATILITY_90);
        assertThat(testInstrument.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createInstrumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = instrumentRepository.findAll().size();

        // Create the Instrument with an existing ID
        instrument.setIsin(DEFAULT_ISIN);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstrumentMockMvc.perform(post("/api/instruments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(instrument)))
            .andExpect(status().isBadRequest());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().size();
        // set the field null
        instrument.setName(null);

        // Create the Instrument, which fails.

        restInstrumentMockMvc.perform(post("/api/instruments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(instrument)))
            .andExpect(status().isBadRequest());

        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsinIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().size();
        // set the field null
        instrument.setIsin(null);

        // Create the Instrument, which fails.

        restInstrumentMockMvc.perform(post("/api/instruments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(instrument)))
            .andExpect(status().isBadRequest());

        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstruments() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        // Get all the instrumentList
        restInstrumentMockMvc.perform(get("/api/instruments?sort=isin,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instrument.getIsin())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isin").value(hasItem(DEFAULT_ISIN)))
            .andExpect(jsonPath("$.[*].tseId").value(hasItem(DEFAULT_TSE_ID)))
            .andExpect(jsonPath("$.[*].volatility30").value(hasItem(DEFAULT_VOLATILITY_30.doubleValue())))
            .andExpect(jsonPath("$.[*].volatility60").value(hasItem(DEFAULT_VOLATILITY_60.doubleValue())))
            .andExpect(jsonPath("$.[*].volatility90").value(hasItem(DEFAULT_VOLATILITY_90.doubleValue())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    public void getInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        // Get the instrument
        restInstrumentMockMvc.perform(get("/api/instruments/{id}", instrument.getIsin()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.isin").value(instrument.getIsin()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.tseId").value(DEFAULT_TSE_ID))
            .andExpect(jsonPath("$.volatility30").value(DEFAULT_VOLATILITY_30.doubleValue()))
            .andExpect(jsonPath("$.volatility60").value(DEFAULT_VOLATILITY_60.doubleValue()))
            .andExpect(jsonPath("$.volatility90").value(DEFAULT_VOLATILITY_90.doubleValue()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstrument() throws Exception {
        // Get the instrument
        restInstrumentMockMvc.perform(get("/api/instruments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();

        // Update the instrument
        Instrument updatedInstrument = instrumentRepository.findById(instrument.getIsin()).get();
        // Disconnect from session so that the updates on updatedInstrument are not directly saved in db
        em.detach(updatedInstrument);
        updatedInstrument
            .name(UPDATED_NAME)
            .isin(UPDATED_ISIN)
            .tseId(UPDATED_TSE_ID)
            .volatility30(UPDATED_VOLATILITY_30)
            .volatility60(UPDATED_VOLATILITY_60)
            .volatility90(UPDATED_VOLATILITY_90)
            .updatedAt(UPDATED_UPDATED_AT);

        restInstrumentMockMvc.perform(put("/api/instruments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInstrument)))
            .andExpect(status().isOk());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
        Instrument testInstrument = instrumentList.get(instrumentList.size() - 1);
        assertThat(testInstrument.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstrument.getIsin()).isEqualTo(UPDATED_ISIN);
        assertThat(testInstrument.getTseId()).isEqualTo(UPDATED_TSE_ID);
        assertThat(testInstrument.getVolatility30()).isEqualTo(UPDATED_VOLATILITY_30);
        assertThat(testInstrument.getVolatility60()).isEqualTo(UPDATED_VOLATILITY_60);
        assertThat(testInstrument.getVolatility90()).isEqualTo(UPDATED_VOLATILITY_90);
        assertThat(testInstrument.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();

        // Create the Instrument

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstrumentMockMvc.perform(put("/api/instruments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(instrument)))
            .andExpect(status().isBadRequest());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        int databaseSizeBeforeDelete = instrumentRepository.findAll().size();

        // Delete the instrument
        restInstrumentMockMvc.perform(delete("/api/instruments/{id}", instrument.getIsin())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
