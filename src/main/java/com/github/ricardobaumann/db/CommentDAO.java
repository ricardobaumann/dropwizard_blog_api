package com.github.ricardobaumann.db;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;

public class CommentDAO extends AbstractDAO<Comment>{

    @Inject
    public CommentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Comment save(Comment comment) {
        return persist(comment);
    }

    public Comment find(Post post, Long commentId) {
        return get(commentId);
    }

    public void delete(Post post, Comment comment) {
       currentSession().delete(comment);
        
    }
    
    

}
