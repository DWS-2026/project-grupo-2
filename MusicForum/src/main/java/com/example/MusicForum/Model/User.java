package com.example.MusicForum.Model;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String username;
    private String password;
    private String email;
    private String avatar;

    @Enumerated(EnumType.STRING)
    private UserRole userRole; // "USER" "INVITADO" "ADMIN"
    
     //UserRole
    public enum UserRole{
        USER,
        INVITADO,
        ADMIN
    }

    public User(){
    }
    
    public User(String username, String password, String email, UserRole userRole){
        this.username = username;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getAvatar(){
        return avatar;
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }
    
    public UserRole getUserRole(){
        return userRole;
    }

    public void setUserRole(UserRole userRole){
        this.userRole = userRole;
    }

}
