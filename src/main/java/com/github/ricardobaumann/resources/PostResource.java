/**
 * 
 */
package com.github.ricardobaumann.resources;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.ricardobaumann.api.PostDTO;
import com.github.ricardobaumann.db.Post;
import com.github.ricardobaumann.db.PostDAO;
import com.github.ricardobaumann.security.User;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

/**
 * Rest controller for posts
 * @author ricardobaumann
 *
 */
@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

    private final AtomicLong counter = new AtomicLong();
    private PostDAO postDAO;
    
    public PostResource(PostDAO postDAO) {
       this.postDAO = postDAO;
    }

    @UnitOfWork
    @GET
    @Path("{id}")
    public PostDTO getByID(@PathParam("id") Long id) {
        Post post = postDAO.find(id);
        return new PostDTO(post.getTitle(), post.getContent(), post.getId());
    }
    
    @UnitOfWork
    @POST
    public PostDTO create(PostDTO postDTO, @Auth User user) {
       Post post = new Post(counter.getAndIncrement(), postDTO.getTitle(), postDTO.getContent());
       post = postDAO.save(post);
       postDTO.setId(post.getId());
       return postDTO;
    }
    
}
