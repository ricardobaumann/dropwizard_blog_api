/**
 * 
 */
package com.github.ricardobaumann.security;

import java.security.Principal;
import java.util.Optional;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

/**
 * @author ricardobaumann
 *
 */
public class SimpleAuthenticator implements Authenticator<BasicCredentials, Principal> {

    @Override
    public Optional<Principal> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        if (basicCredentials.getPassword().equalsIgnoreCase("admin")) {
            return Optional.of((Principal)new User(1L,"user","admin"));
        }
        return Optional.empty();
    }

}
