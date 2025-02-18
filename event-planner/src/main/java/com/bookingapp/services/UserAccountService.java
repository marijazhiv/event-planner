package com.bookingapp.services;

import com.bookingapp.dtos.UserRequest;
 import com.bookingapp.entities.UserAccount;
import com.bookingapp.enums.Role;
 import com.bookingapp.repositories.UserAccountRepository;
import com.bookingapp.util.TokenUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userAccountRepository;



    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private JavaMailSender mailSender;



    public UserAccount getUserById(Long userId) {
        Optional<UserAccount> userOptional = userAccountRepository.findById(userId);
        return userOptional.orElse(null);
    }

    public UserAccount findById(Long id) throws AccessDeniedException {
        return userAccountRepository.findById(id).orElseGet(null);
    }

    /*public UserAccount findByUsername(String username) {
        Optional<UserAccount> userOptional = userAccountRepository.findByUsername(username);
        return userOptional.orElse(null);
    }*/
    //@Override
    public UserAccount findByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUsername(username);
    }

    public boolean deleteUser(Long userId) {
        if (userAccountRepository.existsById(userId)) {
            userAccountRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<UserAccount> findAll() throws AccessDeniedException {
        return userAccountRepository.findAll();
    }

    public String getUserRole(Long userId) {
        Optional<UserAccount> userOptional = userAccountRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserAccount user = userOptional.get();
            return user.getRole().toString();
        }
        return null;
    }

    public void save(UserAccount account) {
        userAccountRepository.save(account);
    }

    public UserAccount save(UserRequest userRequest) {
        UserAccount u = new UserAccount();
        u.setUsername(userRequest.getUsername());

        // pre nego sto postavimo lozinku u atribut hesiramo je kako bi se u bazi nalazila hesirana lozinka
        // treba voditi racuna da se koristi isi password encoder bean koji je postavljen u AUthenticationManager-u kako bi koristili isti algoritam
        u.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        u.setFirstName(userRequest.getFirstname());
        u.setLastName(userRequest.getLastname());


        return this.userAccountRepository.save(u);
    }


    private String findUserImageName(Long id) {
        File directory = new File("src\\main\\resources\\images\\userAvatars");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String filename = file.getName();
                if (filename.startsWith("user-" + id)) {
                    return "userAvatars\\" + file.getName();
                }
            }
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userAccountRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }



    public void verifyUserAccount(Long userId) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setVerified(true);
        userAccountRepository.save(user);
    }

    public Long getUserIdByToken(String token) {
        String username = tokenUtils.getUsernameFromToken(token);
        if (username == null) {
            return null;
        }
        UserAccount user = userAccountRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        return user.getId();
    }

    public Iterable<Object> getOwners() {
        return userAccountRepository.findAllOwners();
    }




}
