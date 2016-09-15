package com.github.ricardobaumann.security;

import javax.inject.Singleton;

import com.github.ricardobaumann.db.User;

import io.dropwizard.auth.Authorizer;

@Singleton
public class OAuthAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String role) {
        System.out.println(String.format("authorizing %s on %s", user,role));
        return true;
    }
}