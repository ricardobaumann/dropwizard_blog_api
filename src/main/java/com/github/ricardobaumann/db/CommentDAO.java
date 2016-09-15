package com.github.ricardobaumann.db;

import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;

public class CommentDAO extends AbstractDAO<Comment>{

    public CommentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Comment save(Comment comment) {
        return persist(comment);
    }
    
    

}
