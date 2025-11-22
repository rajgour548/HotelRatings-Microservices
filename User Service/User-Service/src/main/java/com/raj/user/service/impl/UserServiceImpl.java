package com.raj.user.service.impl;

import com.raj.user.service.entities.Hotel;
import com.raj.user.service.entities.Rating;
import com.raj.user.service.entities.User;
import com.raj.user.service.entities.UserRole;
import com.raj.user.service.exceptions.ResourceNotFoundException;
import com.raj.user.service.external.services.HotelService;
import com.raj.user.service.repositories.UserRepository;
import com.raj.user.service.services.UserService;
import feign.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;



@Service
public class UserServiceImpl implements UserService {

    private static final long EXPIRATION_TIME = 1000*60*60;



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User createUser(User user) {
        // 1. Check if user with same email already exists
        Optional<User> existingUserOpt = userRepository.findByUserEmail(user.getUserEmail());

        if (existingUserOpt.isPresent()) {
            User dbUser = existingUserOpt.get();

            // 2. Update Auth0Id if new one is provided
            if (user.getAuth0Id() != null && !user.getAuth0Id().isEmpty()) {
                for (String newId : user.getAuth0Id()) {
                    if (!dbUser.getAuth0Id().contains(newId)) {
                        dbUser.getAuth0Id().add(newId);
                    }
                }
            }



            // Safely update roles without replacing the collection
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                // Clear existing roles
                dbUser.getRoles().clear();
                // Add new roles
                for (UserRole role : user.getRoles()) {
                    role.setUser(dbUser);
                    dbUser.getRoles().add(role);
                }
            }

            return userRepository.save(dbUser); // update existing user
        }

        // 3. Create new user if email doesn't exist
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            user.setUserId(UUID.randomUUID().toString());
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (UserRole role : user.getRoles()) {
                role.setUser(user);
            }
        }

        return userRepository.save(user); // save new user
    }


    public List<User> getAllUsers() {
            return userRepository.findAll();
        }



    @Override
    public List<User> getAllUser() {

        return userRepository.findAll();
    }

    public String getAuth0Token() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            return jwt.getTokenValue(); // This is your Auth0 access token
        }
        return null; // No token found
    }

    @Override
    public User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user with given id is not found !!" + userId));
        //get user ratings by userId  by calling rating service
        // --- Call RatingService with JWT token ---
        String token = getAuth0Token();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Rating[]> response = restTemplate.exchange("http://RATINGSERVICE/ratings/users/" + userId, HttpMethod.GET,
                entity,Rating[].class);
        Rating[] ratings = response.getBody();
        List<Rating> ratingsList = Arrays.stream(ratings).toList();

        //get hotelinfo by hotel id for each rating
        List<Rating> ratingWithHotel = ratingsList.stream().map(rating -> {
            try {
                ResponseEntity<Hotel> forEntity = restTemplate.exchange("http://HOTELSERVICE/hotels/" + rating.getHotelId(), HttpMethod.GET,
                     entity, Hotel.class);
               Hotel hotel = forEntity.getBody();
               //  Hotel hotel = hotelService.getHotel(rating.getHotelId(),token);
                rating.setHotel(hotel);

            } catch (Exception e) {
                System.out.println("Failed to fetch hotel for rating: " + rating.getHotelId());
                rating.setHotel(null); // or set a dummy/default hotel
            } return rating;
        }
        ).collect(Collectors.toList());
        user.setRatings(ratingWithHotel);
        return user;
    }

    // Example method in UserServiceImpl for Feign call
    // Pass token in header




@Override
public User deleteUser(String userId)
{
    userRepository.deleteById(userId);
    return null;
}

@Override
public User updateUser(User user) {
    // Fetch existing user
    User existingUser = userRepository.findById(user.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getUserId()));

    // Update fields if provided
    if (user.getUserName() != null) existingUser.setUserName(user.getUserName());
    if (user.getUserEmail() != null) existingUser.setUserEmail(user.getUserEmail());


    // Optionally update roles or enabled if needed (usually admin only)
    if (user.getRoles() != null && !user.getRoles().isEmpty()) existingUser.setRoles(user.getRoles());
    existingUser.setEnabled(user.isEnabled());

    return userRepository.save(existingUser);
}


@Override
public User promoteUserToAdmin(String userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    // Check if user already has ADMIN role
    boolean isAdmin = user.getRoles().stream()
            .anyMatch(role -> role.getRole().equals("ADMIN"));

    if (!isAdmin) {
        UserRole adminRole = new UserRole();
        adminRole.setRole("ADMIN");
        adminRole.setUser(user);

        List<UserRole> updatedRoles = new ArrayList<>(user.getRoles());
        updatedRoles.add(adminRole);
        user.setRoles(updatedRoles);

        userRepository.save(user);
    }

    return user;
}



}
