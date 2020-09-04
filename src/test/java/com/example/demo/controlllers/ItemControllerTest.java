package com.example.demo.controlllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
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

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_item_by_id_happy_path() throws Exception {
        Item item = new Item();
        item.setId(0L);
        item.setName("egg");
        item.setDescription("eggs");
        item.setPrice(BigDecimal.valueOf(2L));

        when(itemRepository.findById(0L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item i = response.getBody();
        assertNotNull(i);
        assertEquals(item.getName(), i.getName());
    }

    @Test
    public void get_item_by_name_happy_path() throws Exception {
        Item item = new Item();
        item.setId(0L);
        item.setName("egg");
        item.setDescription("eggs");
        item.setPrice(BigDecimal.valueOf(2L));

        when(itemRepository.findByName("egg")).thenReturn(Collections.singletonList(item));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("egg");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> i = response.getBody();
        assertNotNull(i);
        assertEquals("egg", i.get(0).getName());
    }

    @Test
    public void get_item_by_name_not_ok() throws Exception {
        when(itemRepository.findByName("eggx")).thenReturn(null);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("eggx");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_all_items_happy_path() throws Exception {
        Item item = new Item();
        item.setId(0L);
        item.setName("egg");
        item.setDescription("eggs");
        item.setPrice(BigDecimal.valueOf(2L));

        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> i = response.getBody();
        assertNotNull(i);
        assertEquals("egg", i.get(0).getName());
    }
}
