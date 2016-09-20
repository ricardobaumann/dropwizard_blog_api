package com.github.ricardobaumann.resources;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import com.github.ricardobaumann.db.AccessToken;
import com.github.ricardobaumann.db.User;
import com.github.ricardobaumann.db.UserDAO;
import com.github.ricardobaumann.security.AccessTokenDAO;
import com.github.ricardobaumann.security.CredentialsDTO;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/oauth/token")
@Produces(MediaType.APPLICATION_JSON)
public class OAuthResource {

    private AccessTokenDAO accessTokenDAO;
    private UserDAO userDAO;

    @Inject
    public OAuthResource(AccessTokenDAO accessTokenDAO, UserDAO userDAO) {
        this.accessTokenDAO = accessTokenDAO;
        this.userDAO = userDAO;

        //log.info("Constructed OAuth2Resource with grant types {}", allowedGrantTypes);
    }
    
    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public String postForToken(CredentialsDTO dto
    ) {
        System.out.println("post: "+dto);
        // Check if the grant type is allowed
        //if (!allowedGrantTypes.contains(grantType)) {
          //  Response response = Response.status(Status.METHOD_NOT_ALLOWED).build();
            //throw new WebApplicationException(response);
        //}

        // Try to find a user with the supplied credentials.
        Optional<User> user = userDAO.findUserByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        System.out.println("resgatou: "+user);
        if (user == null || !user.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
        System.out.println("resgatou: "+user.get());
        // User was found, generate a token and return it.
        AccessToken accessToken = accessTokenDAO.generateNewAccessToken(user.get(), new DateTime());
        return accessToken.getAccessTokenId().toString();
    }

}
