package com.github.ricardobaumann.resources;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.validation.ValidationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.github.ricardobaumann.api.PostDTO;
import com.github.ricardobaumann.db.Post;
import com.github.ricardobaumann.db.PostDAO;
import com.github.ricardobaumann.db.User;
import com.github.ricardobaumann.providers.NotFoundExceptionProvider;
import com.github.ricardobaumann.providers.ValidationExceptionProvider;
import com.github.ricardobaumann.security.OAuthAuthenticator;
import com.github.ricardobaumann.security.OAuthAuthorizer;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;

public class PostResourceTest {
    
    private static PostDAO postDAO = mock(PostDAO.class);
    
    private static PostResource postResource = new PostResource(postDAO);
    
    private static OAuthAuthenticator oAuthAuthenticator = mock(OAuthAuthenticator.class);
    
    private static OAuthAuthorizer oAuthAuthorizer = mock(OAuthAuthorizer.class);
    
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule
    .builder()
    .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
    .addResource(postResource)
    .addProvider(new ValidationExceptionProvider())
    .addProvider(new NotFoundExceptionProvider())
    .addProvider(new AuthDynamicFeature(new OAuthCredentialAuthFilter.Builder<User>()
            .setAuthenticator(oAuthAuthenticator)
            .setAuthorizer(oAuthAuthorizer)
            .setRealm("SUPER SECRET STUFF")
            .setPrefix("Bearer")
            .buildAuthFilter()))
    .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
    .build();

    @Before
    public void setUp() throws Exception {
        reset(postDAO, oAuthAuthenticator, oAuthAuthorizer);
        postResource.resetCounter();
    }
   

    @Test
    public void testGetExistentPostByID() {
       
        Long id = 1L;
        String title = "testpost";
        String content = "testcontent";
        Post post = new Post(id, title, content);
        
        when(postDAO.find(id )).thenReturn(post );
        
        assertThat(resources
                .getJerseyTest()
                .target("/posts/"+id)
                .request(MediaType.APPLICATION_JSON)
                .get(PostDTO.class),is(new PostDTO(title, content,id)));
        verify(postDAO).find(id);
    }
    
    @Test
    public void testGetInexistentPostByID() {
        Long id = 1L;
        when(postDAO.find(id)).thenReturn(null);
        
        Response response = resources.getJerseyTest()
                .target("/posts/"+id)
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void testCreateSuccesfully() throws AuthenticationException {
        
        String credentials = "credentials";
        User user = new User(1L, "user junit test", "user");
        when(oAuthAuthenticator.authenticate(credentials)).thenReturn(Optional.of(user));
        
        String role = "admin";
        when(oAuthAuthorizer.authorize(user, role )).thenReturn(true);
        
        Long id = 1L;
        String title = "testpost";
        String content = "testcontent";
        Post post = new Post(id, title, content);
        
        when(postDAO.save(post)).thenReturn(new Post(id, title, content));
        
        PostDTO dto = resources
                .getJerseyTest()
                .target("/posts")
                .request()
                .header("Authorization", "Bearer "+credentials)
                .post(Entity.entity(new PostDTO(title, content, null), MediaType.APPLICATION_JSON), PostDTO.class);
        
        assertThat(dto.getId(), is(id));
        assertThat(dto.getTitle(), is(title));
        assertThat(dto.getContent(), is(content));
        
        
    }
    
    @Test
    public void testCreateValidationErrors() throws AuthenticationException {
        Long id = 1L;
        String title = "testpost";
        String content = "testcontent";
        Post post = new Post(id, title, content);
        
        User user = new User(1L, "user junit test", "user");
        String credentials = "credentials";
        when(oAuthAuthenticator.authenticate(credentials )).thenReturn(Optional.of(user));
        
        String role = "admin";
        when(oAuthAuthorizer.authorize(user, role )).thenReturn(true);
        
        doThrow(ValidationException.class).when(postDAO).save(post);
        
        Response response = resources
        .getJerseyTest()
        .target("/posts")
        .request()
        .header("Authorization", "Bearer "+credentials)
        .post(Entity.entity(new PostDTO(title, content, null), MediaType.APPLICATION_JSON));
        
        assertThat(response.getStatus(), is(422));
    }

}
