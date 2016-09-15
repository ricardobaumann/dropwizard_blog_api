package com.github.ricardobaumann.security;

import java.util.Optional;

import javax.inject.Singleton;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

// may be external class (internal for simplicity)
@Singleton
public class OAuthAuthenticator implements Authenticator<String, User> {

    @Override
    public Optional<User> authenticate(String credentials) throws AuthenticationException {
        System.out.println(credentials);
        return Optional.ofNullable(credentials.length()>0 ? new User(1L, "user", "user") : null);
    }
}