package com.lazar.auth_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    @Email
    private String email;
    private String name;
    @Column(unique = true, nullable = false)
    private String nickname;
    private String description;
    private String profileDescription;
    private String picture;
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    public User(long id, String email, String name, String nickname, String description, String profileDescription, AuthProvider authProvider) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.description = description;
        this.profileDescription = profileDescription;
        this.authProvider = authProvider;
    }

    public User() {
    }

    public User(String email,String name,String picture,AuthProvider authProvider){
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.authProvider = authProvider;
        this.nickname = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public enum AuthProvider {
        GOOGLE,
        FACEBOOK
    }
}
