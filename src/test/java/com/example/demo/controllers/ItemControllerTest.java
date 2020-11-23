package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void getItemByName() throws Exception {
        Item item = new Item();
        item.setId(1l);
        item.setName("test");
        item.setDescription("testDescription");
        item.setPrice(BigDecimal.ONE);

        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepo.findByName(item.getName())).thenReturn(items);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("test");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> i = response.getBody();
        assertNotNull(i);
        assertEquals("test", i.get(0).getName());
        assertEquals("testDescription", i.get(0).getDescription());
        assertEquals(BigDecimal.ONE, i.get(0).getPrice());
    }

    @Test
    public void getItems() throws Exception {
        Item item1 = new Item();
        item1.setId(1l);
        item1.setName("test");
        item1.setDescription("testDescription");
        item1.setPrice(BigDecimal.ONE);

        Item item2 = new Item();
        item1.setId(2l);
        item1.setName("test2");
        item1.setDescription("testDescription2");
        item1.setPrice(BigDecimal.ONE);

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        when(itemRepo.findAll()).thenReturn(items);

        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> i = response.getBody();
        assertNotNull(i);
    }

    @Test
    public void getItemById() throws Exception {
        Item item = new Item();
        item.setId(1l);
        item.setName("test");
        item.setDescription("testDescription");
        item.setPrice(BigDecimal.ONE);

        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        final ResponseEntity<Item> response = itemController.getItemById(1l);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item i = response.getBody();
        assertNotNull(i);
        assertEquals("test", i.getName());
        assertEquals("testDescription", i.getDescription());
        assertEquals(BigDecimal.ONE, i.getPrice());
    }
}
