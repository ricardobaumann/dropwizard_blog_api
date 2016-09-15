/**
 * 
 */
package com.github.ricardobaumann.security;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.setup.Environment;

/**
 * @author ricardobaumann
 *
 */
@Provider
public class OAuthDynamicFeature extends AuthDynamicFeature {
    
    
    @Inject
    public OAuthDynamicFeature(OAuthAuthenticator authenticator, OAuthAuthorizer authorizer, Environment environment) {
        super(new OAuthCredentialAuthFilter.Builder<User>()
                .setAuthenticator(authenticator)
                .setAuthorizer(authorizer)
                .setPrefix("Bearer")
                .buildAuthFilter());
    }  


}
