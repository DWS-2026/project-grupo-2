package com.example.MusicForum.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.MusicForum.Model.User;
import com.example.MusicForum.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        // Encrypt the plain text password before saving
        String encryptedPassword = passwordEncoder.encode(user.getEncodedPassword());
        user.setEncodedPassword(encryptedPassword);

        // Assign default role (e.g., USER)
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        user.setRoles(roles);

        // Save the new user to the database
        userRepository.save(user);
    }
}
