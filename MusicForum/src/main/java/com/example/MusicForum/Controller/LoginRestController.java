package com.example.MusicForum.Controller;

import com.example.MusicForum.Model.User;
import com.example.MusicForum.Service.UserService;
import com.example.MusicForum.Security.jwt.AuthResponse;
import com.example.MusicForum.Security.jwt.LoginRequest;
import com.example.MusicForum.Security.jwt.UserLoginService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginRestController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserLoginService userLoginService;          //Teachers' folder

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        
        return userLoginService.login(response, loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente");      //Created without errors
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());        //404 error
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@CookieValue(name = "RefreshToken", required = false) String refreshToken, HttpServletResponse response) {
        
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body(new AuthResponse(AuthResponse.Status.FAILURE, ""));  //Refresh token is missing
        }
        
        return userLoginService.refresh(response, refreshToken);    
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        
        String message = userLoginService.logout(response);             //Delegate the logout logic to the UserLoginService to clear cookies
        return ResponseEntity.ok(message);
    }
}
