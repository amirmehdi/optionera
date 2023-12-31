package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.ETradeApp;
import com.gitlab.amirmehdi.domain.BourseCode;
import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.domain.Signal;
import com.gitlab.amirmehdi.domain.enumeration.OrderState;
import com.gitlab.amirmehdi.domain.enumeration.Side;
import com.gitlab.amirmehdi.domain.enumeration.Validity;
import com.gitlab.amirmehdi.repository.OrderRepository;
import com.gitlab.amirmehdi.service.OrderQueryService;
import com.gitlab.amirmehdi.service.OrderService;
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
/**
 * Integration tests for the {@link OrderResource} REST controller.
 */
@SpringBootTest(classes = ETradeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class OrderResourceIT {

    private static final String DEFAULT_ISIN = "AAAAAAAAAA";
    private static final String UPDATED_ISIN = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;
    private static final Integer SMALLER_PRICE = 1 - 1;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final Validity DEFAULT_VALIDITY = Validity.DAY;
    private static final Validity UPDATED_VALIDITY = Validity.FILL_AND_KILL;

    private static final Side DEFAULT_SIDE = Side.BUY;
    private static final Side UPDATED_SIDE = Side.SELL;

    private static final String DEFAULT_OMS_ID = "AAAAAAAAAA";
    private static final String UPDATED_OMS_ID = "BBBBBBBBBB";

    private static final OrderState DEFAULT_STATE = OrderState.NONE;
    private static final OrderState UPDATED_STATE = OrderState.ACTIVE;

    private static final Integer DEFAULT_EXECUTED = 1;
    private static final Integer UPDATED_EXECUTED = 2;
    private static final Integer SMALLER_EXECUTED = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .isin(DEFAULT_ISIN)
            .price(DEFAULT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .validity(DEFAULT_VALIDITY)
            .side(DEFAULT_SIDE)
            .omsId(DEFAULT_OMS_ID)
            .state(DEFAULT_STATE)
            .executed(DEFAULT_EXECUTED)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        BourseCode bourseCode;
        if (TestUtil.findAll(em, BourseCode.class).isEmpty()) {
            bourseCode = BourseCodeResourceIT.createEntity(em);
            em.persist(bourseCode);
            em.flush();
        } else {
            bourseCode = TestUtil.findAll(em, BourseCode.class).get(0);
        }
        order.setBourseCode(bourseCode);
        return order;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .isin(UPDATED_ISIN)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY)
            .validity(UPDATED_VALIDITY)
            .side(UPDATED_SIDE)
            .omsId(UPDATED_OMS_ID)
            .state(UPDATED_STATE)
            .executed(UPDATED_EXECUTED)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        BourseCode bourseCode;
        if (TestUtil.findAll(em, BourseCode.class).isEmpty()) {
            bourseCode = BourseCodeResourceIT.createUpdatedEntity(em);
            em.persist(bourseCode);
            em.flush();
        } else {
            bourseCode = TestUtil.findAll(em, BourseCode.class).get(0);
        }
        order.setBourseCode(bourseCode);
        return order;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getIsin()).isEqualTo(DEFAULT_ISIN);
        assertThat(testOrder.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrder.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrder.getValidity()).isEqualTo(DEFAULT_VALIDITY);
        assertThat(testOrder.getSide()).isEqualTo(DEFAULT_SIDE);
        assertThat(testOrder.getOmsId()).isEqualTo(DEFAULT_OMS_ID);
        assertThat(testOrder.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testOrder.getExecuted()).isEqualTo(DEFAULT_EXECUTED);
        assertThat(testOrder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order with an existing ID
        order.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkIsinIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setIsin(null);

        // Create the Order, which fails.

        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setPrice(null);

        // Create the Order, which fails.

        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setQuantity(null);

        // Create the Order, which fails.

        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValidityIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setValidity(null);

        // Create the Order, which fails.

        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSideIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setSide(null);

        // Create the Order, which fails.

        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].isin").value(hasItem(DEFAULT_ISIN)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].validity").value(hasItem(DEFAULT_VALIDITY.toString())))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].omsId").value(hasItem(DEFAULT_OMS_ID)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].executed").value(hasItem(DEFAULT_EXECUTED)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.isin").value(DEFAULT_ISIN))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.validity").value(DEFAULT_VALIDITY.toString()))
            .andExpect(jsonPath("$.side").value(DEFAULT_SIDE.toString()))
            .andExpect(jsonPath("$.omsId").value(DEFAULT_OMS_ID))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.executed").value(DEFAULT_EXECUTED))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getOrdersByIdFiltering() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        Long id = order.getId();

        defaultOrderShouldBeFound("id.equals=" + id);
        defaultOrderShouldNotBeFound("id.notEquals=" + id);

        defaultOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOrdersByIsinIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where isin equals to DEFAULT_ISIN
        defaultOrderShouldBeFound("isin.equals=" + DEFAULT_ISIN);

        // Get all the orderList where isin equals to UPDATED_ISIN
        defaultOrderShouldNotBeFound("isin.equals=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllOrdersByIsinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where isin not equals to DEFAULT_ISIN
        defaultOrderShouldNotBeFound("isin.notEquals=" + DEFAULT_ISIN);

        // Get all the orderList where isin not equals to UPDATED_ISIN
        defaultOrderShouldBeFound("isin.notEquals=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllOrdersByIsinIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where isin in DEFAULT_ISIN or UPDATED_ISIN
        defaultOrderShouldBeFound("isin.in=" + DEFAULT_ISIN + "," + UPDATED_ISIN);

        // Get all the orderList where isin equals to UPDATED_ISIN
        defaultOrderShouldNotBeFound("isin.in=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllOrdersByIsinIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where isin is not null
        defaultOrderShouldBeFound("isin.specified=true");

        // Get all the orderList where isin is null
        defaultOrderShouldNotBeFound("isin.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrdersByIsinContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where isin contains DEFAULT_ISIN
        defaultOrderShouldBeFound("isin.contains=" + DEFAULT_ISIN);

        // Get all the orderList where isin contains UPDATED_ISIN
        defaultOrderShouldNotBeFound("isin.contains=" + UPDATED_ISIN);
    }

    @Test
    @Transactional
    public void getAllOrdersByIsinNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where isin does not contain DEFAULT_ISIN
        defaultOrderShouldNotBeFound("isin.doesNotContain=" + DEFAULT_ISIN);

        // Get all the orderList where isin does not contain UPDATED_ISIN
        defaultOrderShouldBeFound("isin.doesNotContain=" + UPDATED_ISIN);
    }


    @Test
    @Transactional
    public void getAllOrdersByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where price equals to DEFAULT_PRICE
        defaultOrderShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the orderList where price equals to UPDATED_PRICE
        defaultOrderShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where price not equals to DEFAULT_PRICE
        defaultOrderShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the orderList where price not equals to UPDATED_PRICE
        defaultOrderShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultOrderShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the orderList where price equals to UPDATED_PRICE
        defaultOrderShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where price is not null
        defaultOrderShouldBeFound("price.specified=true");

        // Get all the orderList where price is null
        defaultOrderShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where price is greater than or equal to DEFAULT_PRICE
        defaultOrderShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the orderList where price is greater than or equal to UPDATED_PRICE
        defaultOrderShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where price is less than or equal to DEFAULT_PRICE
        defaultOrderShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the orderList where price is less than or equal to SMALLER_PRICE
        defaultOrderShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where price is less than DEFAULT_PRICE
        defaultOrderShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the orderList where price is less than UPDATED_PRICE
        defaultOrderShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where price is greater than DEFAULT_PRICE
        defaultOrderShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the orderList where price is greater than SMALLER_PRICE
        defaultOrderShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllOrdersByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity equals to DEFAULT_QUANTITY
        defaultOrderShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity equals to UPDATED_QUANTITY
        defaultOrderShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity not equals to DEFAULT_QUANTITY
        defaultOrderShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity not equals to UPDATED_QUANTITY
        defaultOrderShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultOrderShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the orderList where quantity equals to UPDATED_QUANTITY
        defaultOrderShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is not null
        defaultOrderShouldBeFound("quantity.specified=true");

        // Get all the orderList where quantity is null
        defaultOrderShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultOrderShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultOrderShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultOrderShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity is less than or equal to SMALLER_QUANTITY
        defaultOrderShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is less than DEFAULT_QUANTITY
        defaultOrderShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity is less than UPDATED_QUANTITY
        defaultOrderShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is greater than DEFAULT_QUANTITY
        defaultOrderShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity is greater than SMALLER_QUANTITY
        defaultOrderShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllOrdersByValidityIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where validity equals to DEFAULT_VALIDITY
        defaultOrderShouldBeFound("validity.equals=" + DEFAULT_VALIDITY);

        // Get all the orderList where validity equals to UPDATED_VALIDITY
        defaultOrderShouldNotBeFound("validity.equals=" + UPDATED_VALIDITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByValidityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where validity not equals to DEFAULT_VALIDITY
        defaultOrderShouldNotBeFound("validity.notEquals=" + DEFAULT_VALIDITY);

        // Get all the orderList where validity not equals to UPDATED_VALIDITY
        defaultOrderShouldBeFound("validity.notEquals=" + UPDATED_VALIDITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByValidityIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where validity in DEFAULT_VALIDITY or UPDATED_VALIDITY
        defaultOrderShouldBeFound("validity.in=" + DEFAULT_VALIDITY + "," + UPDATED_VALIDITY);

        // Get all the orderList where validity equals to UPDATED_VALIDITY
        defaultOrderShouldNotBeFound("validity.in=" + UPDATED_VALIDITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByValidityIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where validity is not null
        defaultOrderShouldBeFound("validity.specified=true");

        // Get all the orderList where validity is null
        defaultOrderShouldNotBeFound("validity.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersBySideIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where side equals to DEFAULT_SIDE
        defaultOrderShouldBeFound("side.equals=" + DEFAULT_SIDE);

        // Get all the orderList where side equals to UPDATED_SIDE
        defaultOrderShouldNotBeFound("side.equals=" + UPDATED_SIDE);
    }

    @Test
    @Transactional
    public void getAllOrdersBySideIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where side not equals to DEFAULT_SIDE
        defaultOrderShouldNotBeFound("side.notEquals=" + DEFAULT_SIDE);

        // Get all the orderList where side not equals to UPDATED_SIDE
        defaultOrderShouldBeFound("side.notEquals=" + UPDATED_SIDE);
    }

    @Test
    @Transactional
    public void getAllOrdersBySideIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where side in DEFAULT_SIDE or UPDATED_SIDE
        defaultOrderShouldBeFound("side.in=" + DEFAULT_SIDE + "," + UPDATED_SIDE);

        // Get all the orderList where side equals to UPDATED_SIDE
        defaultOrderShouldNotBeFound("side.in=" + UPDATED_SIDE);
    }

    @Test
    @Transactional
    public void getAllOrdersBySideIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where side is not null
        defaultOrderShouldBeFound("side.specified=true");

        // Get all the orderList where side is null
        defaultOrderShouldNotBeFound("side.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByBrokerIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where broker is not null
        defaultOrderShouldBeFound("broker.specified=true");

        // Get all the orderList where broker is null
        defaultOrderShouldNotBeFound("broker.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByOmsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where omsId equals to DEFAULT_OMS_ID
        defaultOrderShouldBeFound("omsId.equals=" + DEFAULT_OMS_ID);

        // Get all the orderList where omsId equals to UPDATED_OMS_ID
        defaultOrderShouldNotBeFound("omsId.equals=" + UPDATED_OMS_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByOmsIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where omsId not equals to DEFAULT_OMS_ID
        defaultOrderShouldNotBeFound("omsId.notEquals=" + DEFAULT_OMS_ID);

        // Get all the orderList where omsId not equals to UPDATED_OMS_ID
        defaultOrderShouldBeFound("omsId.notEquals=" + UPDATED_OMS_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByOmsIdIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where omsId in DEFAULT_OMS_ID or UPDATED_OMS_ID
        defaultOrderShouldBeFound("omsId.in=" + DEFAULT_OMS_ID + "," + UPDATED_OMS_ID);

        // Get all the orderList where omsId equals to UPDATED_OMS_ID
        defaultOrderShouldNotBeFound("omsId.in=" + UPDATED_OMS_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByOmsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where omsId is not null
        defaultOrderShouldBeFound("omsId.specified=true");

        // Get all the orderList where omsId is null
        defaultOrderShouldNotBeFound("omsId.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrdersByOmsIdContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where omsId contains DEFAULT_OMS_ID
        defaultOrderShouldBeFound("omsId.contains=" + DEFAULT_OMS_ID);

        // Get all the orderList where omsId contains UPDATED_OMS_ID
        defaultOrderShouldNotBeFound("omsId.contains=" + UPDATED_OMS_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByOmsIdNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where omsId does not contain DEFAULT_OMS_ID
        defaultOrderShouldNotBeFound("omsId.doesNotContain=" + DEFAULT_OMS_ID);

        // Get all the orderList where omsId does not contain UPDATED_OMS_ID
        defaultOrderShouldBeFound("omsId.doesNotContain=" + UPDATED_OMS_ID);
    }


    @Test
    @Transactional
    public void getAllOrdersByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where state equals to DEFAULT_STATE
        defaultOrderShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the orderList where state equals to UPDATED_STATE
        defaultOrderShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where state not equals to DEFAULT_STATE
        defaultOrderShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the orderList where state not equals to UPDATED_STATE
        defaultOrderShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByStateIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where state in DEFAULT_STATE or UPDATED_STATE
        defaultOrderShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the orderList where state equals to UPDATED_STATE
        defaultOrderShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where state is not null
        defaultOrderShouldBeFound("state.specified=true");

        // Get all the orderList where state is null
        defaultOrderShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByExecutedIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where executed equals to DEFAULT_EXECUTED
        defaultOrderShouldBeFound("executed.equals=" + DEFAULT_EXECUTED);

        // Get all the orderList where executed equals to UPDATED_EXECUTED
        defaultOrderShouldNotBeFound("executed.equals=" + UPDATED_EXECUTED);
    }

    @Test
    @Transactional
    public void getAllOrdersByExecutedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where executed not equals to DEFAULT_EXECUTED
        defaultOrderShouldNotBeFound("executed.notEquals=" + DEFAULT_EXECUTED);

        // Get all the orderList where executed not equals to UPDATED_EXECUTED
        defaultOrderShouldBeFound("executed.notEquals=" + UPDATED_EXECUTED);
    }

    @Test
    @Transactional
    public void getAllOrdersByExecutedIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where executed in DEFAULT_EXECUTED or UPDATED_EXECUTED
        defaultOrderShouldBeFound("executed.in=" + DEFAULT_EXECUTED + "," + UPDATED_EXECUTED);

        // Get all the orderList where executed equals to UPDATED_EXECUTED
        defaultOrderShouldNotBeFound("executed.in=" + UPDATED_EXECUTED);
    }

    @Test
    @Transactional
    public void getAllOrdersByExecutedIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where executed is not null
        defaultOrderShouldBeFound("executed.specified=true");

        // Get all the orderList where executed is null
        defaultOrderShouldNotBeFound("executed.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByExecutedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where executed is greater than or equal to DEFAULT_EXECUTED
        defaultOrderShouldBeFound("executed.greaterThanOrEqual=" + DEFAULT_EXECUTED);

        // Get all the orderList where executed is greater than or equal to UPDATED_EXECUTED
        defaultOrderShouldNotBeFound("executed.greaterThanOrEqual=" + UPDATED_EXECUTED);
    }

    @Test
    @Transactional
    public void getAllOrdersByExecutedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where executed is less than or equal to DEFAULT_EXECUTED
        defaultOrderShouldBeFound("executed.lessThanOrEqual=" + DEFAULT_EXECUTED);

        // Get all the orderList where executed is less than or equal to SMALLER_EXECUTED
        defaultOrderShouldNotBeFound("executed.lessThanOrEqual=" + SMALLER_EXECUTED);
    }

    @Test
    @Transactional
    public void getAllOrdersByExecutedIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where executed is less than DEFAULT_EXECUTED
        defaultOrderShouldNotBeFound("executed.lessThan=" + DEFAULT_EXECUTED);

        // Get all the orderList where executed is less than UPDATED_EXECUTED
        defaultOrderShouldBeFound("executed.lessThan=" + UPDATED_EXECUTED);
    }

    @Test
    @Transactional
    public void getAllOrdersByExecutedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where executed is greater than DEFAULT_EXECUTED
        defaultOrderShouldNotBeFound("executed.greaterThan=" + DEFAULT_EXECUTED);

        // Get all the orderList where executed is greater than SMALLER_EXECUTED
        defaultOrderShouldBeFound("executed.greaterThan=" + SMALLER_EXECUTED);
    }


    @Test
    @Transactional
    public void getAllOrdersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where description equals to DEFAULT_DESCRIPTION
        defaultOrderShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the orderList where description equals to UPDATED_DESCRIPTION
        defaultOrderShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrdersByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where description not equals to DEFAULT_DESCRIPTION
        defaultOrderShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the orderList where description not equals to UPDATED_DESCRIPTION
        defaultOrderShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrdersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOrderShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the orderList where description equals to UPDATED_DESCRIPTION
        defaultOrderShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrdersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where description is not null
        defaultOrderShouldBeFound("description.specified=true");

        // Get all the orderList where description is null
        defaultOrderShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrdersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where description contains DEFAULT_DESCRIPTION
        defaultOrderShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the orderList where description contains UPDATED_DESCRIPTION
        defaultOrderShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrdersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where description does not contain DEFAULT_DESCRIPTION
        defaultOrderShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the orderList where description does not contain UPDATED_DESCRIPTION
        defaultOrderShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllOrdersBySignalIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);
        Signal signal = SignalResourceIT.createEntity(em);
        em.persist(signal);
        em.flush();
        order.setSignal(signal);
        orderRepository.saveAndFlush(order);
        Long signalId = signal.getId();

        // Get all the orderList where signal equals to signalId
        defaultOrderShouldBeFound("signalId.equals=" + signalId);

        // Get all the orderList where signal equals to signalId + 1
        defaultOrderShouldNotBeFound("signalId.equals=" + (signalId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersByBourseCodeIsEqualToSomething() throws Exception {
        // Get already existing entity
        BourseCode bourseCode = order.getBourseCode();
        orderRepository.saveAndFlush(order);
        Long bourseCodeId = bourseCode.getId();

        // Get all the orderList where bourseCode equals to bourseCodeId
        defaultOrderShouldBeFound("bourseCodeId.equals=" + bourseCodeId);

        // Get all the orderList where bourseCode equals to bourseCodeId + 1
        defaultOrderShouldNotBeFound("bourseCodeId.equals=" + (bourseCodeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderShouldBeFound(String filter) throws Exception {
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].isin").value(hasItem(DEFAULT_ISIN)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].validity").value(hasItem(DEFAULT_VALIDITY.toString())))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].omsId").value(hasItem(DEFAULT_OMS_ID)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].executed").value(hasItem(DEFAULT_EXECUTED)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restOrderMockMvc.perform(get("/api/orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderShouldNotBeFound(String filter) throws Exception {
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderMockMvc.perform(get("/api/orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrder() throws Exception {
        // Initialize the database
        orderService.save(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .isin(UPDATED_ISIN)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY)
            .validity(UPDATED_VALIDITY)
            .side(UPDATED_SIDE)
            .omsId(UPDATED_OMS_ID)
            .state(UPDATED_STATE)
            .executed(UPDATED_EXECUTED)
            .description(UPDATED_DESCRIPTION);

        restOrderMockMvc.perform(put("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrder)))
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getIsin()).isEqualTo(UPDATED_ISIN);
        assertThat(testOrder.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrder.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrder.getValidity()).isEqualTo(UPDATED_VALIDITY);
        assertThat(testOrder.getSide()).isEqualTo(UPDATED_SIDE);
        assertThat(testOrder.getOmsId()).isEqualTo(UPDATED_OMS_ID);
        assertThat(testOrder.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testOrder.getExecuted()).isEqualTo(UPDATED_EXECUTED);
        assertThat(testOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Create the Order

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc.perform(put("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrder() throws Exception {
        // Initialize the database
        orderService.save(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc.perform(delete("/api/orders/{id}", order.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
