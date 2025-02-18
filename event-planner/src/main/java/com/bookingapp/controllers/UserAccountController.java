package com.bookingapp.controllers;

import com.bookingapp.dtos.*;
import com.bookingapp.entities.*;
 import com.bookingapp.enums.Role;
 import com.bookingapp.services.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;


    @Autowired
    private ActivationService activationService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @GetMapping(value = "/users/username/token/{token}", produces = "text/plain")
    public ResponseEntity<String> getUsername(@PathVariable String token) {
        Long userId = userAccountService.getUserIdByToken(token);
        if (userId == null) {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }
        UserAccount userAccount = userAccountService.getUserById(userId);
        if (userAccount == null) {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userAccount.getUsername(), HttpStatus.OK);
    }



    @PutMapping("/verify/users/{userId}")
    public ResponseEntity<String> verifyUserAccount(@PathVariable Long userId) {
        try {
            Activation activation= activationService.getActivationByUserId(userId);

            // check if activation is expired
            if (activation.isExpired()) {
                return new ResponseEntity<>("Activation link has expired.", HttpStatus.BAD_REQUEST);
            }
            userAccountService.verifyUserAccount(userId);
            return new ResponseEntity<>("User successfully verified.", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/users/token/{token}", name = "get user id by bearer token")
    public ResponseEntity<Long> getUserAccountByToken(@PathVariable String token) {
        Long userId = userAccountService.getUserIdByToken(token);
        if (userId == null) {
            return new ResponseEntity<>((long) -1, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @GetMapping(value = "/users/androidToken/{token}", name = "android get user id by bearer token")
    public ResponseEntity<Long> getUserAccountByTokenAndroid(@PathVariable String token) {
        Long userId = userAccountService.getUserIdByToken(token);
        if (userId == null) {
            return new ResponseEntity<>((long) -1, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @PutMapping(value = "/users/{id}", name = "user updates his profile")
    public ResponseEntity<String> updateUserAccount(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserAccount user = userAccountService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>("Account Not Found", HttpStatus.NOT_FOUND);
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAddress(userDTO.getAddress());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        userAccountService.save(user);
        return new ResponseEntity<>("Account Updated", HttpStatus.OK);
    }

    @PutMapping(value = "/users/{id}/basicInfo", name = "user updates his basic info")
    public ResponseEntity<String> updateUserBasicInfo(@PathVariable Long id, @RequestBody UserBasicInfoNoImageDTO userDTO) {
        UserAccount user = userAccountService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>("Account Not Found", HttpStatus.NOT_FOUND);
        }
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAddress(userDTO.getAddress());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        userAccountService.save(user);
        return new ResponseEntity<>("Account Updated", HttpStatus.OK);
    }

    @PutMapping(value = "/users/{id}/password", name = "user updates his password")
    public ResponseEntity<String> updateUserPassword(@PathVariable Long id, @RequestBody String password) {
        UserAccount user = userAccountService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>("Account Not Found", HttpStatus.NOT_FOUND);
        }
        user.setPassword(passwordEncoder.encode(password));
        userAccountService.save(user);
        return new ResponseEntity<>("Account Updated", HttpStatus.OK);
    }






    @GetMapping(value = "/users/owner/{userId}")
    public ResponseEntity<?> getUserAccountInfoById(@PathVariable Long userId){
        UserAccount user = userAccountService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new UserInfoDTO(user), HttpStatus.OK);

    }










    public String getImageBytes(String relativePath) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/images/" + relativePath);
        if (inputStream == null) {
            throw new IOException("Image not found");
        }
        int imageSize = inputStream.available();
        byte[] buffer = new byte[imageSize];
        int bytesRead = inputStream.read(buffer);
        if (bytesRead != imageSize) {
            throw new IOException("Error reading image:");
        }
        inputStream.close();
        return Base64.getEncoder().encodeToString(buffer);
    }


}