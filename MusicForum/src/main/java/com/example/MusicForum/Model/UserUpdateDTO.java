package com.example.MusicForum.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {

    //Not required to update
    @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres")
    private String username;

    //Not required to update
    @Email(message = "El formato del email no es válido")
    private String email;

    private String avatar;

    //Not required to update
    @Size(min = 6, max = 64, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // Constructor vacío obligatorio para Spring
    public UserUpdateDTO() {
    }

    //Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}