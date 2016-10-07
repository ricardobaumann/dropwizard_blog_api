package com.github.ricardobaumann.db;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

public class PostDAO extends AbstractDAO<Post> {

    @Inject
    public PostDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Post find(Long id) {
        return get(id);
    }

    public Post save(Post post) {
       return persist(post);
        
    }

    public List<Post> findAll() {
        return list(currentSession().createQuery("select p from Post"));
    }
}
