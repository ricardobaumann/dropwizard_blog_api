package com.github.ricardobaumann;

import java.util.Optional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.github.ricardobaumann.security.User;

import io.dropwizard.hibernate.AbstractDAO;

public class UserDAO extends AbstractDAO<User>{

    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public Optional<User> find(String username, String password) {
        Criteria criteria = criteria()
                .add(Restrictions.eq("username", username))
                .add(Restrictions.eq("password", password));
        return Optional.ofNullable(uniqueResult(criteria));
    }

}
