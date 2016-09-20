package com.github.ricardobaumann.security;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.ricardobaumann.db.AccessToken;
import com.github.ricardobaumann.db.User;
import com.github.ricardobaumann.db.UserDAO;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.hibernate.UnitOfWork;

// may be external class (internal for simplicity)
@Singleton
public class OAuthAuthenticator implements Authenticator<String, User> {

    private AccessTokenDAO accessTokenDAO;
    private UserDAO userDAO;

    @Inject
    public OAuthAuthenticator(AccessTokenDAO accessTokenDAO, UserDAO userDAO) {
        this.accessTokenDAO = accessTokenDAO;
        this.userDAO = userDAO;
    }

    @UnitOfWork
    @Override
    public Optional<User> authenticate(String credentials) throws AuthenticationException {
        // System.out.println(credentials);
        Optional<AccessToken> accessToken = accessTokenDAO.findAccessTokenById(UUID.fromString(credentials));
        if (!accessToken.isPresent()) {
            return Optional.empty();
        }
        User user = userDAO.find(accessToken.get().getUserId());
        return Optional.of(user);
    }
}