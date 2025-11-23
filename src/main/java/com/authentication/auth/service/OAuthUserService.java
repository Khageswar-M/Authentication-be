package com.authentication.auth.service;


import com.authentication.auth.model.OAutheUser;
import com.authentication.auth.repository.OAuthUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OAuthUserService {

    private final OAuthUserRepository repo;

    public OAuthUserService (OAuthUserRepository repo){
        this.repo = repo;
    }

    public OAutheUser saveOrUpdate(OAutheUser user){
        return repo.findByEmail(user.getEmail())
                .map(existing -> {
                    existing.setName(user.getName());
                    existing.setPicture(user.getPicture());
                    existing.setProvider(user.getProvider());
                    existing.setProviderId(user.getProviderId());
                    existing.setStatus("Active");
                    return repo.save(existing);
                })
                .orElseGet(() -> {
                    user.setStatus("Active");
                    return repo.save(user);
                });
    }
    public OAutheUser processOAuthLogin(
            String provider,
            String providerId,
            String name,
            String email,
            String picture
    ){
        if (email == null || email.isEmpty()) {
            email = providerId + "@github-user.com";   // fallback for GitHub no-email
        }

        Optional<OAutheUser> existing = repo.findByEmail(email);

        if (existing.isPresent()) {
            OAutheUser user = existing.get();
            user.setProvider(provider);
            user.setProviderId(providerId);
            user.setName(name);
            user.setPicture(picture);
            user.setStatus("Active");
            return repo.save(user);
        }

        // New user
        OAutheUser newUser = new OAutheUser(
                providerId,
                provider,
                name,
                email,
                picture,
                "Active"
        );

        return repo.save(newUser);
    }

    public List<OAutheUser> getAllUsers(){
        return repo.findAll();
    }

    public boolean deleteByEmail(String email) {
        Optional<OAutheUser> userOpt = repo.findByEmail(email);

        if (userOpt.isPresent()) {
            repo.deleteByEmail(email);
            System.out.println("✅ Deleted user: " + email);
            return true;
        } else {
            System.out.println("⚠️ No user found for email: " + email);
            return false;
        }
    }

}
