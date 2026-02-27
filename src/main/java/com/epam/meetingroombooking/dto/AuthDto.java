package com.epam.meetingroombooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

public class AuthDto {

    public static class LoginRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;

        public LoginRequest() {}
        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
        @NotBlank
        private String email;
        private boolean admin;

        public RegisterRequest() {}
        public RegisterRequest(String username, String password, String email, boolean admin) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.admin = admin;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public boolean isAdmin() { return admin; }
        public void setAdmin(boolean admin) { this.admin = admin; }
    }

    public static class JwtResponse {
        private String token;
        private String username;
        private String role;

        public JwtResponse(String token, String username, String role) {
            this.token = token;
            this.username = username;
            this.role = role;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
