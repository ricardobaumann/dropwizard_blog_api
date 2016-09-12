package com.github.ricardobaumann.resources;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.validation.ValidationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.validation.internal.ValidationExceptionMapper;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.github.ricardobaumann.api.PostDTO;
import com.github.ricardobaumann.db.Post;
import com.github.ricardobaumann.db.PostDAO;
import com.github.ricardobaumann.providers.NotFoundExceptionProvider;
import com.github.ricardobaumann.providers.ValidationExceptionProvider;

import static org.hamcrest.core.Is.*;

import io.dropwizard.testing.junit.ResourceTestRule;

public class PostResourceTest {
    
    private static PostDAO postDAO = mock(PostDAO.class);
    
    private static PostResource postResource = new PostResource(postDAO);
    
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule
    .builder()
    .addResource(postResource)
    .addProvider(new ValidationExceptionProvider())
    .addProvider(new NotFoundExceptionProvider())
    .build();

    @Before
    public void setUp() throws Exception {
        reset(postDAO);
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
                .client()
                .target("/posts/"+id)
                .request(MediaType.APPLICATION_JSON)
                .get(PostDTO.class),is(new PostDTO(title, content,id)));
        verify(postDAO).find(id);
    }
    
    @Test
    public void testGetInexistentPostByID() {
        Long id = 1L;
        when(postDAO.find(id)).thenReturn(null);
        
        Response response = resources.client()
                .target("/posts/"+id)
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void testCreateSuccesfully() {
        Long id = 1L;
        String title = "testpost";
        String content = "testcontent";
        Post post = new Post(id, title, content);
        
        when(postDAO.save(post)).thenReturn(new Post(id, title, content));
        
        PostDTO dto = resources
                .client()
                .target("/posts")
                .request()
                .post(Entity.entity(new PostDTO(title, content, null), MediaType.APPLICATION_JSON), PostDTO.class);
        
        assertThat(dto.getId(), is(id));
        assertThat(dto.getTitle(), is(title));
        assertThat(dto.getContent(), is(content));
        
        
    }
    
    @Test
    public void testCreateValidationErrors() {
        Long id = 1L;
        String title = "testpost";
        String content = "testcontent";
        Post post = new Post(id, title, content);
        
        doThrow(ValidationException.class).when(postDAO).save(post);
        
        Response response = resources
        .client()
        .target("/posts")
        .request()
        .post(Entity.entity(new PostDTO(title, content, null), MediaType.APPLICATION_JSON));
        
        assertThat(response.getStatus(), is(422));
    }

}
