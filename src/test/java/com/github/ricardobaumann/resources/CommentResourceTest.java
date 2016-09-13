package com.github.ricardobaumann.resources;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.core.Is.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.github.ricardobaumann.api.CommentDTO;
import com.github.ricardobaumann.db.Comment;
import com.github.ricardobaumann.db.CommentDAO;
import com.github.ricardobaumann.db.Post;
import com.github.ricardobaumann.db.PostDAO;
import com.github.ricardobaumann.providers.NotFoundExceptionProvider;
import com.github.ricardobaumann.providers.ValidationExceptionProvider;

import io.dropwizard.testing.junit.ResourceTestRule;

public class CommentResourceTest {
    
    private static PostDAO postDAO = mock(PostDAO.class);
    private static CommentDAO commentDAO = mock(CommentDAO.class);
    
    private static CommentResource CommentResource = new CommentResource(commentDAO, postDAO);
    
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule
    .builder()
    .addResource(CommentResource)
    .addProvider(new ValidationExceptionProvider())
    .addProvider(new NotFoundExceptionProvider())
    .build();

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        reset(postDAO,commentDAO);
    }

    @Test
    public void testCreateSucessfully() {
        Long postid = 1L;
        String title = "post";
        String content = "content";
        String commentContent = "comment";
        Long id = 1L;
        
        Post post = new Post(postid, title, content);
        
        Comment comment = new Comment();
        comment.setContent(commentContent);
        comment.setPost(post);
        
        when(postDAO.find(postid)).thenReturn(post);
        when(commentDAO.save(comment)).thenReturn(new Comment(id,post,commentContent));
        
        CommentDTO commentDTO = resources.client().target(String.format("/posts/%s/comments", postid))
                .request().post(Entity.entity(new CommentDTO(null, commentContent), MediaType.APPLICATION_JSON), CommentDTO.class);
                
        assertThat(commentDTO.getId(), is(id));
        
        verify(postDAO).find(postid);
        verify(commentDAO).save(comment);
    }

}
