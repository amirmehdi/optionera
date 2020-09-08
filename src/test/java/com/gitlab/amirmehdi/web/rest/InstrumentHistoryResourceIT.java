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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private static final Integer DEFAULT_CLOSING = 1;
    private static final Integer UPDATED_CLOSING = 2;

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

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
            .closing(DEFAULT_CLOSING)
            .updatedAt(DEFAULT_UPDATED_AT);
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
            .closing(UPDATED_CLOSING)
            .updatedAt(UPDATED_UPDATED_AT);
        return instrumentHistory;
    }

    @BeforeEach
    public void initTest() {
        instrumentHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstrumentHistory() throws Exception {
        int databaseSizeBeforeCreate = instrumentHistoryRepository.findAll().size();

        // Create the InstrumentHistory
        restInstrumentHistoryMockMvc.perform(post("/api/instrument-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(instrumentHistory)))
            .andExpect(status().isCreated());

        // Validate the InstrumentHistory in the database
        List<InstrumentHistory> instrumentHistoryList = instrumentHistoryRepository.findAll();
        assertThat(instrumentHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        InstrumentHistory testInstrumentHistory = instrumentHistoryList.get(instrumentHistoryList.size() - 1);
        assertThat(testInstrumentHistory.getIsin()).isEqualTo(DEFAULT_ISIN);
        assertThat(testInstrumentHistory.getClosing()).isEqualTo(DEFAULT_CLOSING);
        assertThat(testInstrumentHistory.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createInstrumentHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = instrumentHistoryRepository.findAll().size();

        // Create the InstrumentHistory with an existing ID
        instrumentHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstrumentHistoryMockMvc.perform(post("/api/instrument-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(instrumentHistory)))
            .andExpect(status().isBadRequest());

        // Validate the InstrumentHistory in the database
        List<InstrumentHistory> instrumentHistoryList = instrumentHistoryRepository.findAll();
        assertThat(instrumentHistoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkIsinIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentHistoryRepository.findAll().size();
        // set the field null
        instrumentHistory.setIsin(null);

        // Create the InstrumentHistory, which fails.

        restInstrumentHistoryMockMvc.perform(post("/api/instrument-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(instrumentHistory)))
            .andExpect(status().isBadRequest());

        List<InstrumentHistory> instrumentHistoryList = instrumentHistoryRepository.findAll();
        assertThat(instrumentHistoryList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].closing").value(hasItem(DEFAULT_CLOSING)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
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
            .andExpect(jsonPath("$.closing").value(DEFAULT_CLOSING))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstrumentHistory() throws Exception {
        // Get the instrumentHistory
        restInstrumentHistoryMockMvc.perform(get("/api/instrument-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstrumentHistory() throws Exception {
        // Initialize the database
        instrumentHistoryService.save(instrumentHistory);

        int databaseSizeBeforeUpdate = instrumentHistoryRepository.findAll().size();

        // Update the instrumentHistory
        InstrumentHistory updatedInstrumentHistory = instrumentHistoryRepository.findById(instrumentHistory.getId()).get();
        // Disconnect from session so that the updates on updatedInstrumentHistory are not directly saved in db
        em.detach(updatedInstrumentHistory);
        updatedInstrumentHistory
            .isin(UPDATED_ISIN)
            .closing(UPDATED_CLOSING)
            .updatedAt(UPDATED_UPDATED_AT);

        restInstrumentHistoryMockMvc.perform(put("/api/instrument-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInstrumentHistory)))
            .andExpect(status().isOk());

        // Validate the InstrumentHistory in the database
        List<InstrumentHistory> instrumentHistoryList = instrumentHistoryRepository.findAll();
        assertThat(instrumentHistoryList).hasSize(databaseSizeBeforeUpdate);
        InstrumentHistory testInstrumentHistory = instrumentHistoryList.get(instrumentHistoryList.size() - 1);
        assertThat(testInstrumentHistory.getIsin()).isEqualTo(UPDATED_ISIN);
        assertThat(testInstrumentHistory.getClosing()).isEqualTo(UPDATED_CLOSING);
        assertThat(testInstrumentHistory.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingInstrumentHistory() throws Exception {
        int databaseSizeBeforeUpdate = instrumentHistoryRepository.findAll().size();

        // Create the InstrumentHistory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstrumentHistoryMockMvc.perform(put("/api/instrument-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(instrumentHistory)))
            .andExpect(status().isBadRequest());

        // Validate the InstrumentHistory in the database
        List<InstrumentHistory> instrumentHistoryList = instrumentHistoryRepository.findAll();
        assertThat(instrumentHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInstrumentHistory() throws Exception {
        // Initialize the database
        instrumentHistoryService.save(instrumentHistory);

        int databaseSizeBeforeDelete = instrumentHistoryRepository.findAll().size();

        // Delete the instrumentHistory
        restInstrumentHistoryMockMvc.perform(delete("/api/instrument-histories/{id}", instrumentHistory.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InstrumentHistory> instrumentHistoryList = instrumentHistoryRepository.findAll();
        assertThat(instrumentHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
