package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.mock;

public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void addToCart() {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("test");
        cartRequest.setItemId(1);
        cartRequest.setQuantity(1);

        User user = new User();
        user.setId(1);
        user.setUsername("test");

        Cart newCart = new Cart();
        user.setCart(newCart);

        Item item = new Item();
        item.setId(1l);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setPrice(BigDecimal.ONE);

        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<Cart> response = cartController.addTocart(cartRequest);
        Cart cart = response.getBody();

        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals("testItem", cart.getItems().get(0).getName());
        Assert.assertEquals("testDescription", cart.getItems().get(0).getDescription());
    }
}
