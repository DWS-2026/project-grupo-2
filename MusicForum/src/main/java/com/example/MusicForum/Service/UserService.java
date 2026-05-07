package com.example.MusicForum.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.MusicForum.Model.User;
import com.example.MusicForum.Model.UserDTO;
import com.example.MusicForum.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        throw new RuntimeException("El nombre de usuario ya está en uso");
    }
        if (userRepository.existsByEmail(user.getEmail())) {
        throw new RuntimeException("El email ya está en uso");
    }
        //Encrypt the plain text password before saving
        String encryptedPassword = passwordEncoder.encode(user.getEncodedPassword());
        user.setEncodedPassword(encryptedPassword);

        // Assign default role (e.g., USER)
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        user.setRoles(roles);

        //Save the new user to the database
        userRepository.save(user);
    }

    //Update user profile
     public void updateUser(User existingUser, String newUsername, String newEmail, String newPassword) {
        existingUser.setUsername(newUsername);
        existingUser.setEmail(newEmail);
        
        if (newPassword != null && !newPassword.isBlank()) {
            existingUser.setEncodedPassword(passwordEncoder.encode(newPassword));
        }
        
        userRepository.save(existingUser);
    }

    //Pageable
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(user -> new UserDTO(user));
    }
}
