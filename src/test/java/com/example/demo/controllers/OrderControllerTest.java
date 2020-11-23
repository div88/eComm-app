package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submitOrder() throws Exception {
        User user = new User();
        user.setId(1l);
        user.setUsername("test");

        Cart cart = new Cart();

        Item item = new Item();
        item.setId(1l);
        item.setName("item1");
        item.setDescription("testItemDescription");
        item.setPrice(BigDecimal.ONE);

        List<Item> items = new ArrayList<>();
        items.add(item);

        cart.setItems(items);
        user.setCart(cart);
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);


        final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals("item1", order.getItems().get(0).getName());
        assertEquals("testItemDescription", order.getItems().get(0).getDescription());
        assertEquals(BigDecimal.ONE, order.getItems().get(0).getPrice());
    }

    @Test
    public void submitOrderFailure() throws Exception {
        User user = new User();
        user.setId(1l);
        user.setUsername("test");
        Cart cart = new Cart();

        Item item = new Item();
        item.setId(1l);
        item.setName("item1");
        item.setDescription("testItemDescription");
        item.setPrice(BigDecimal.ONE);

        List<Item> items = new ArrayList<>();
        items.add(item);

        cart.setItems(items);
        user.setCart(cart);
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit("test2");
        Assert.assertEquals(404, response.getStatusCodeValue());
    }
}
