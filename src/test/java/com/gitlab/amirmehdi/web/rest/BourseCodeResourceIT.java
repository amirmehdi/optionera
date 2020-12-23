package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.ETradeApp;
import com.gitlab.amirmehdi.domain.BourseCode;
import com.gitlab.amirmehdi.repository.BourseCodeRepository;
import com.gitlab.amirmehdi.service.BourseCodeService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gitlab.amirmehdi.domain.enumeration.Broker;
/**
 * Integration tests for the {@link BourseCodeResource} REST controller.
 */
@SpringBootTest(classes = ETradeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class BourseCodeResourceIT {

    private static final Broker DEFAULT_BROKER = Broker.REFAH;
    private static final Broker UPDATED_BROKER = Broker.FIROOZE_ASIA;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Long DEFAULT_BUYING_POWER = 1L;
    private static final Long UPDATED_BUYING_POWER = 2L;

    private static final Long DEFAULT_BLOCKED = 1L;
    private static final Long UPDATED_BLOCKED = 2L;

    private static final Long DEFAULT_REMAIN = 1L;
    private static final Long UPDATED_REMAIN = 2L;

    private static final Long DEFAULT_CREDIT = 1L;
    private static final Long UPDATED_CREDIT = 2L;

    @Autowired
    private BourseCodeRepository bourseCodeRepository;

    @Autowired
    private BourseCodeService bourseCodeService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBourseCodeMockMvc;

    private BourseCode bourseCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BourseCode createEntity(EntityManager em) {
        BourseCode bourseCode = new BourseCode()
            .broker(DEFAULT_BROKER)
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .buyingPower(DEFAULT_BUYING_POWER)
            .blocked(DEFAULT_BLOCKED)
            .remain(DEFAULT_REMAIN)
            .credit(DEFAULT_CREDIT);
        return bourseCode;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BourseCode createUpdatedEntity(EntityManager em) {
        BourseCode bourseCode = new BourseCode()
            .broker(UPDATED_BROKER)
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .buyingPower(UPDATED_BUYING_POWER)
            .blocked(UPDATED_BLOCKED)
            .remain(UPDATED_REMAIN)
            .credit(UPDATED_CREDIT);
        return bourseCode;
    }

    @BeforeEach
    public void initTest() {
        bourseCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createBourseCode() throws Exception {
        int databaseSizeBeforeCreate = bourseCodeRepository.findAll().size();

        // Create the BourseCode
        restBourseCodeMockMvc.perform(post("/api/bourse-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bourseCode)))
            .andExpect(status().isCreated());

        // Validate the BourseCode in the database
        List<BourseCode> bourseCodeList = bourseCodeRepository.findAll();
        assertThat(bourseCodeList).hasSize(databaseSizeBeforeCreate + 1);
        BourseCode testBourseCode = bourseCodeList.get(bourseCodeList.size() - 1);
        assertThat(testBourseCode.getBroker()).isEqualTo(DEFAULT_BROKER);
        assertThat(testBourseCode.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBourseCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBourseCode.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testBourseCode.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testBourseCode.getBuyingPower()).isEqualTo(DEFAULT_BUYING_POWER);
        assertThat(testBourseCode.getBlocked()).isEqualTo(DEFAULT_BLOCKED);
        assertThat(testBourseCode.getRemain()).isEqualTo(DEFAULT_REMAIN);
        assertThat(testBourseCode.getCredit()).isEqualTo(DEFAULT_CREDIT);
    }

    @Test
    @Transactional
    public void createBourseCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bourseCodeRepository.findAll().size();

        // Create the BourseCode with an existing ID
        bourseCode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBourseCodeMockMvc.perform(post("/api/bourse-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bourseCode)))
            .andExpect(status().isBadRequest());

        // Validate the BourseCode in the database
        List<BourseCode> bourseCodeList = bourseCodeRepository.findAll();
        assertThat(bourseCodeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBourseCodes() throws Exception {
        // Initialize the database
        bourseCodeRepository.saveAndFlush(bourseCode);

        // Get all the bourseCodeList
        restBourseCodeMockMvc.perform(get("/api/bourse-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bourseCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].broker").value(hasItem(DEFAULT_BROKER.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].buyingPower").value(hasItem(DEFAULT_BUYING_POWER.intValue())))
            .andExpect(jsonPath("$.[*].blocked").value(hasItem(DEFAULT_BLOCKED.intValue())))
            .andExpect(jsonPath("$.[*].remain").value(hasItem(DEFAULT_REMAIN.intValue())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.intValue())));
    }
    
    @Test
    @Transactional
    public void getBourseCode() throws Exception {
        // Initialize the database
        bourseCodeRepository.saveAndFlush(bourseCode);

        // Get the bourseCode
        restBourseCodeMockMvc.perform(get("/api/bourse-codes/{id}", bourseCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bourseCode.getId().intValue()))
            .andExpect(jsonPath("$.broker").value(DEFAULT_BROKER.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.buyingPower").value(DEFAULT_BUYING_POWER.intValue()))
            .andExpect(jsonPath("$.blocked").value(DEFAULT_BLOCKED.intValue()))
            .andExpect(jsonPath("$.remain").value(DEFAULT_REMAIN.intValue()))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBourseCode() throws Exception {
        // Get the bourseCode
        restBourseCodeMockMvc.perform(get("/api/bourse-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBourseCode() throws Exception {
        // Initialize the database
        bourseCodeService.save(bourseCode);

        int databaseSizeBeforeUpdate = bourseCodeRepository.findAll().size();

        // Update the bourseCode
        BourseCode updatedBourseCode = bourseCodeRepository.findById(bourseCode.getId()).get();
        // Disconnect from session so that the updates on updatedBourseCode are not directly saved in db
        em.detach(updatedBourseCode);
        updatedBourseCode
            .broker(UPDATED_BROKER)
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .buyingPower(UPDATED_BUYING_POWER)
            .blocked(UPDATED_BLOCKED)
            .remain(UPDATED_REMAIN)
            .credit(UPDATED_CREDIT);

        restBourseCodeMockMvc.perform(put("/api/bourse-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBourseCode)))
            .andExpect(status().isOk());

        // Validate the BourseCode in the database
        List<BourseCode> bourseCodeList = bourseCodeRepository.findAll();
        assertThat(bourseCodeList).hasSize(databaseSizeBeforeUpdate);
        BourseCode testBourseCode = bourseCodeList.get(bourseCodeList.size() - 1);
        assertThat(testBourseCode.getBroker()).isEqualTo(UPDATED_BROKER);
        assertThat(testBourseCode.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBourseCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBourseCode.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testBourseCode.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testBourseCode.getBuyingPower()).isEqualTo(UPDATED_BUYING_POWER);
        assertThat(testBourseCode.getBlocked()).isEqualTo(UPDATED_BLOCKED);
        assertThat(testBourseCode.getRemain()).isEqualTo(UPDATED_REMAIN);
        assertThat(testBourseCode.getCredit()).isEqualTo(UPDATED_CREDIT);
    }

    @Test
    @Transactional
    public void updateNonExistingBourseCode() throws Exception {
        int databaseSizeBeforeUpdate = bourseCodeRepository.findAll().size();

        // Create the BourseCode

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBourseCodeMockMvc.perform(put("/api/bourse-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bourseCode)))
            .andExpect(status().isBadRequest());

        // Validate the BourseCode in the database
        List<BourseCode> bourseCodeList = bourseCodeRepository.findAll();
        assertThat(bourseCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBourseCode() throws Exception {
        // Initialize the database
        bourseCodeService.save(bourseCode);

        int databaseSizeBeforeDelete = bourseCodeRepository.findAll().size();

        // Delete the bourseCode
        restBourseCodeMockMvc.perform(delete("/api/bourse-codes/{id}", bourseCode.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BourseCode> bourseCodeList = bourseCodeRepository.findAll();
        assertThat(bourseCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
