package com.raj.user.service.controllers;

import com.raj.user.service.dto.LoginRequest;
import com.raj.user.service.dto.LoginResponse;
import com.raj.user.service.entities.User;
import com.raj.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }


    int retry=0;
    @GetMapping("/{userId}")
    @RateLimiter(name = "ratingHotelRateLimiter", fallbackMethod = "ratingHotelFallback")
    @Retry(name="ratingHotelService")
    @CircuitBreaker(name="ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getUserById(@PathVariable String userId){
        retry++;
        logger.info("retrycount:{}",retry);
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }
    // creting fallback method for rating and hotel service
    // circuitbreaker flow : Service healthy → CB CLOSED → all calls pass
    //Service starts failing → failures counted
    //Failures exceed threshold → CB OPEN → fallback only
    //After wait time → CB HALF-OPEN → test limited calls
    //If success → CB CLOSED → normal again
    //If fail → CB OPEN → repeat cycle
    // i added circuit breaker at userservice for ratingservice and hotelservice and at api gateway

    public ResponseEntity<User> ratingHotelFallback(String userId,Exception ex){
        logger.info("Fallback executed because some services are down",ex.getMessage());
        User user = User.builder().userEmail("dummy@gmail.com").userName("dummy").userId("1233445").build();
        return new ResponseEntity<User>(user,HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUser(){
        List<User> allUser = userService.getAllUser();
        return ResponseEntity.ok(allUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId,
                                           @RequestBody User user) {
        user.setUserId(userId); // ensure correct user is updated
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable String userId) {
        User deletedUser = userService.deleteUser(userId);
        return ResponseEntity.ok(deletedUser);
    }

    @PutMapping("/users/{userId}/promote")// Only admin can call
    public ResponseEntity<User> promoteToAdmin(@PathVariable String userId) {
        User updatedUser = userService.promoteUserToAdmin(userId); // call interface method
        return ResponseEntity.ok(updatedUser);
    }



        @GetMapping("/me")
        public Map<String, Object> getProfile(@AuthenticationPrincipal Jwt jwt) {
            Map<String, Object> profile = new HashMap<>();
            profile.put("user_id", jwt.getSubject());
            profile.put("email", jwt.getClaimAsString("email"));
            profile.put("roles", jwt.getClaimAsStringList("https://hotel.com/roles"));
            profile.put("permissions", jwt.getClaimAsStringList("permissions"));
            return profile;
        }



}
