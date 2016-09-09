/**
 * 
 */
package com.github.ricardobaumann.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.ricardobaumann.api.PostDTO;

/**
 * Rest controller for posts
 * @author ricardobaumann
 *
 */
@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

    private static Map<Long, PostDTO> map = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();
    
    @GET
    @Path("{id}")
    public PostDTO getByID(@PathParam("id") Long id) {
        return map.get(id);
    }
    
    @POST
    public PostDTO create(PostDTO postDTO) {
        postDTO.setId(counter.getAndIncrement());
        map.put(postDTO.getId(), postDTO);
        return postDTO;
    }
    
}
