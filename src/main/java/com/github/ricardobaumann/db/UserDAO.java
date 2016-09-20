package com.github.ricardobaumann.db;

import java.util.Optional;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.hibernate.UnitOfWork;

public class UserDAO extends AbstractDAO<User>{

    private SessionFactory sessionFactory;

    @Inject
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }
    
    @UnitOfWork
    public Optional<User> find(String username, String password) {
        Query query = currentSession()
                .createQuery("select u from User u where u.password = :password and u.username = :username")
                .setParameter("username", username)
                .setParameter("password", password);
       return Optional.ofNullable(uniqueResult(query));
    }

    public Optional<User> findUserByUsernameAndPassword(String username, String password) {
       return find(username, password);
    }

    public User find(Long userId) {
        return sessionFactory.openSession().get(User.class, userId);//workaround for dropwizard bug of unitofwork outside jersey resources
    }

}
