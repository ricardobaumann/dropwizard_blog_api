package com.github.ricardobaumann.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.ricardobaumann.api.CommentDTO;
import com.github.ricardobaumann.db.Comment;
import com.github.ricardobaumann.db.CommentDAO;
import com.github.ricardobaumann.db.Post;
import com.github.ricardobaumann.db.PostDAO;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/posts/{post_id}/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentResource {
    
    private CommentDAO commentDAO;
    private PostDAO postDAO;

    @Inject
    public CommentResource(CommentDAO commentDAO, PostDAO postDAO) {
        this.commentDAO  = commentDAO;
        this.postDAO  =postDAO;
    }

    @POST
    @UnitOfWork
    public CommentDTO create(CommentDTO dto, @PathParam("post_id") Long postId) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setPost(postDAO.find(postId));
        comment = commentDAO.save(comment);
        dto.setId(comment.getId());
        return dto;
    }
    
    @DELETE
    @UnitOfWork
    @Path("{comment_id}")
    public void delete(@PathParam("post_id") Long postId, @PathParam("comment_id") Long commentId) {
        Post post = postDAO.find(postId);
        if (post==null) {
            throw new NotFoundException();
        }
        Comment comment = commentDAO.find(post, commentId);
        if (comment==null) {
            throw new NotFoundException();
        }
        commentDAO.delete(post,comment);
    }
    
}
