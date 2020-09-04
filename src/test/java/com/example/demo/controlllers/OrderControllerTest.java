package com.example.demo.controlllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void submit_order_happy_path() throws Exception {
        User testUser = new User();
        testUser.setId(0L);
        testUser.setUsername("luca");
        testUser.setPassword("testPassword");

        Item item = new Item();
        item.setId(0L);
        item.setName("egg");
        item.setDescription("eggs");
        item.setPrice(BigDecimal.valueOf(2L));

        Cart cart = new Cart();
        cart.setId(0L);
        cart.addItem(item);
        cart.setUser(testUser);

        testUser.setCart(cart);
        when(userRepository.findByUsername("luca")).thenReturn(testUser);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("luca");

        ResponseEntity<UserOrder> response = orderController.submit("luca");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder u = response.getBody();
        assertNotNull(u);
        assertEquals(cart.getItems(), u.getItems());
        assertEquals(cart.getUser(), u.getUser());
    }

    @Test
    public void submit_order_not_ok() throws Exception {
        when(userRepository.findByUsername("luca")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("luca");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_orders_history_for_username_happy_path() throws Exception {
        User testUser = new User();
        testUser.setId(0L);
        testUser.setUsername("luca");
        testUser.setPassword("testPassword");

        Item item = new Item();
        item.setId(0L);
        item.setName("egg");
        item.setDescription("eggs");
        item.setPrice(BigDecimal.valueOf(2L));

        UserOrder testOrder = new UserOrder();
        testOrder.setId(0L);
        testOrder.setItems(Collections.singletonList(item));
        testOrder.setUser(testUser);

        when(userRepository.findByUsername("luca")).thenReturn(testUser);
        when(orderRepository.findByUser(testUser)).thenReturn(Collections.singletonList(testOrder));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("luca");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> u = response.getBody();
        assertNotNull(u);
        assertEquals(testOrder, u.get(0));
    }

    @Test
    public void get_orders_history_for_username_not_ok() throws Exception {
        User testUser = new User();
        testUser.setId(0L);
        testUser.setUsername("luca");
        testUser.setPassword("testPassword");

        Item item = new Item();
        item.setId(0L);
        item.setName("egg");
        item.setDescription("eggs");
        item.setPrice(BigDecimal.valueOf(2L));

        UserOrder testOrder = new UserOrder();
        testOrder.setId(0L);
        testOrder.setItems(Collections.singletonList(item));
        testOrder.setUser(testUser);

        when(userRepository.findByUsername("luca")).thenReturn(null);
        when(orderRepository.findByUser(testUser)).thenReturn(Collections.singletonList(testOrder));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("luca");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
