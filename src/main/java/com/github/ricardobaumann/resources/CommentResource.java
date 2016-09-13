package com.github.ricardobaumann.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.ricardobaumann.api.CommentDTO;
import com.github.ricardobaumann.db.Comment;
import com.github.ricardobaumann.db.CommentDAO;
import com.github.ricardobaumann.db.PostDAO;

@Path("/posts/{post_id}/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentResource {
    
    private CommentDAO commentDAO;
    private PostDAO postDAO;

    public CommentResource(CommentDAO commentDAO, PostDAO postDAO) {
        this.commentDAO  = commentDAO;
        this.postDAO  =postDAO;
    }

    @POST
    public CommentDTO create(CommentDTO dto, @PathParam("post_id") Long postId) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setPost(postDAO.find(postId));
        comment = commentDAO.save(comment);
        dto.setId(comment.getId());
        return dto;
    }
    
}
