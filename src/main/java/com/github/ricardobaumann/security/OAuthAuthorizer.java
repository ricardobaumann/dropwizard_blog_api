package com.github.ricardobaumann.security;

import javax.inject.Singleton;

import com.github.ricardobaumann.db.User;

import io.dropwizard.auth.Authorizer;

// may be external class (internal for simplicity)
@Singleton
public class OAuthAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String role) {
        return true;
    }
}