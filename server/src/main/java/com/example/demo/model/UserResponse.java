package com.example.demo.model;

public class UserResponse {
    private User user;
    private String error;

    public UserResponse(User user, String error) {
        this.user = user;
        this.error = error;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
