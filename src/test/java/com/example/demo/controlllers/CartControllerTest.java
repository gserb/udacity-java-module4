package com.example.demo.controlllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
    }

    @Test
    public void add_to_cart_happy_path() throws Exception {
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
        when(itemRepository.findById(0L)).thenReturn(Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("luca");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart c = response.getBody();
        assertNotNull(c);
        assertEquals(cart.getItems(), c.getItems());
    }

    @Test
    public void add_to_cart_invalid_item() throws Exception {
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
        when(itemRepository.findById(1L)).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("luca");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void add_to_cart_invalid_username() throws Exception {
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

        testUser.setCart(cart);
        when(userRepository.findByUsername("luca")).thenReturn(testUser);
        when(itemRepository.findById(1L)).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("luca");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_happy_path() throws Exception {
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

        testUser.setCart(cart);
        when(userRepository.findByUsername("luca")).thenReturn(testUser);
        when(itemRepository.findById(0L)).thenReturn(Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("luca");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart c = response.getBody();
        assertNotNull(c);
        assertEquals(cart.getItems(), c.getItems());
    }

    @Test
    public void remove_from_cart_invalid_item() throws Exception {
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

        testUser.setCart(cart);
        when(userRepository.findByUsername("luca")).thenReturn(testUser);
        when(itemRepository.findById(1L)).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("luca");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_invalid_username() throws Exception {
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

        testUser.setCart(cart);
        when(userRepository.findByUsername("luca")).thenReturn(testUser);
        when(itemRepository.findById(1L)).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("luca");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
