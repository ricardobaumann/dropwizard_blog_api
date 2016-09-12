package com.github.ricardobaumann.db;

import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;

public class PostDAO extends AbstractDAO<Post> {

    public PostDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Post find(Long id) {
        return get(id);
    }

    public Post save(Post post) {
       return persist(post);
        
    }

}
