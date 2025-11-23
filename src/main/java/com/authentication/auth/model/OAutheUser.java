package com.authentication.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "oauth_users")
public class OAutheUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String providerId;  // sub from google or id from github
    private String provider;    // GOOGLE or GITHUB
    private String name;

    @Column(unique = true)
    private String email;   // google email OR Fake Github email

    private String picture;
    private String status;  // Active or Offline

    public OAutheUser() {};

    public OAutheUser(
            String providerId,
            String provider,
            String name,
            String email,
            String picture,
            String status
    ){
        this.providerId = providerId;
        this.provider = provider;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.status = status;
    }

    // Getters & Setters

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getProviderId(){
        return providerId;
    }
    public void setProviderId(String providerId){
        this.providerId = providerId;
    }

    public String getProvider(){
        return provider;
    }
    public void setProvider(String provider){
        this.provider = provider;
    }

    public String getName() {
         return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPicture(){
        return picture;
    }
    public void setPicture(String picture){
        this.picture = picture;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
}
