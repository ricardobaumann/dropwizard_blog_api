package com.github.ricardobaumann.security;

import javax.inject.Singleton;

import io.dropwizard.auth.Authorizer;

// may be external class (internal for simplicity)
@Singleton
public class OAuthAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String role) {
        return user.getName().equals("good-guy");
    }
}