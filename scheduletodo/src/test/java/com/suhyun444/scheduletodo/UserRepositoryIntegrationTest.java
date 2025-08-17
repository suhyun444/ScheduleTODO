package com.suhyun444.scheduletodo;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.suhyun444.scheduletodo.user.User;
import com.suhyun444.scheduletodo.user.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Testcontainers
public class UserRepositoryIntegrationTest 
{ 
    @Container 
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0").withDatabaseName("testdb").withUsername("test").withPassword("pass"); 
    @DynamicPropertySource 
    static void overrideProps(DynamicPropertyRegistry registry) 
    { 
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
    } 
    @Autowired
    private UserRepository userRepository; 
    @Test
    public void testSaveAndFind() 
    {         
        User user = new User("test@test.com"); 
        userRepository.save(user); 
        Optional<User> found = userRepository.findByEmail("test@test.com"); 
        assertThat(found.isPresent()).isTrue(); 
        assertThat(found.get().getEmail()).isEqualTo("test@test.com"); 
    } 
}