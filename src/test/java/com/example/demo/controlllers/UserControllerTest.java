package com.example.demo.controlllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void create_user_notok() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPass");
        request.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void get_by_name_happy_path() throws Exception {
        User testUser = new User();
        testUser.setUsername("luca");
        testUser.setPassword("testPassword");

        when(userRepository.findByUsername("luca")).thenReturn(testUser);
        final ResponseEntity<User> response = userController.findByUserName("luca");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User responseUser = response.getBody();

        assertEquals("luca", responseUser.getUsername());
        assertEquals("testPassword", responseUser.getPassword());
        assertEquals(0, responseUser.getId());
    }

    @Test
    public void get_by_name_not_ok() throws Exception {
        User testUser = new User();
        testUser.setUsername("luca");
        testUser.setPassword("testPassword");

        when(userRepository.findByUsername("luca1")).thenReturn(null);
        final ResponseEntity<User> response = userController.findByUserName("luca1");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_by_id_happy_path() throws Exception {
        User testUser = new User();
        testUser.setUsername("luca");
        testUser.setPassword("testPassword");

        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(testUser));
        final ResponseEntity<User> response = userController.findById(0L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User responseUser = response.getBody();
        assertNotNull(responseUser);
        assertEquals(0, responseUser.getId());
        assertEquals("luca", responseUser.getUsername());
        assertEquals("testPassword", responseUser.getPassword());
    }

    @Test
    public void get_by_id_not_ok() throws Exception {
//        User testUser = new User();
//        testUser.setUsername("luca");
//        testUser.setPassword("testPassword");

        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        final ResponseEntity<User> response = userController.findById(2L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
