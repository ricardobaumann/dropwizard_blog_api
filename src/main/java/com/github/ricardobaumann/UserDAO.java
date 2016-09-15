package com.github.ricardobaumann;

import java.util.Optional;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.github.ricardobaumann.security.User;

import io.dropwizard.hibernate.AbstractDAO;

public class UserDAO extends AbstractDAO<User>{

    @Inject
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public Optional<User> find(String username, String password) {
        Criteria criteria = criteria()
                .add(Restrictions.eq("username", username))
                .add(Restrictions.eq("password", password));
        return Optional.ofNullable(uniqueResult(criteria));
    }

    public Optional<User> findUserByUsernameAndPassword(String username, String password) {
        if (username.equalsIgnoreCase("admin")) {
            return Optional.of(new User(1L, username, password));
        }
        return Optional.empty();
    }

}
