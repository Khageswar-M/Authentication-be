package com.authentication.auth.controller;

import com.authentication.auth.model.OAutheUser;
import com.authentication.auth.service.OAuthUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {

    private final OAuthUserService service;

    public UserController(OAuthUserService service){
        this.service = service;
    }

    @GetMapping("/user-info")
    public Object user(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not logged in"));
        }
        System.out.println("from UserController: " + principal.getAttributes());
        return principal.getAttributes();
    }


    @GetMapping("/all-users")
    public List<OAutheUser> getAllUsers(){
        return service.getAllUsers();
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestParam String email) {
        boolean deleted = service.deleteByEmail(email);

        if (deleted) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
