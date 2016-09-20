package com.github.ricardobaumann.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;

import com.github.ricardobaumann.db.AccessToken;
import com.github.ricardobaumann.db.User;

import io.dropwizard.hibernate.AbstractDAO;

public class AccessTokenDAO extends AbstractDAO<AccessToken>{

    private SessionFactory sessionFactory;

    @Inject
    public AccessTokenDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    private static Map<String, AccessToken> accessTokenTable = new HashMap<>();

    public Optional<AccessToken> findAccessTokenById(final UUID accessTokenId) {
        System.out.println("trying to fetch token: "+accessTokenId);
        String key = accessTokenId.toString();
        System.out.println("fetching key: "+key);
        AccessToken accessToken = accessTokenTable.get(accessTokenId.toString());
        if (accessToken == null) {
           System.out.println("not found on cache. fetching from database");
           Query query = sessionFactory.openSession()
                   .createQuery("select a from AcessToken a");
           list(query).forEach((r -> {
               System.out.println(r.getAccessTokenId());
           }));
            accessToken = sessionFactory.openSession().get(AccessToken.class,accessTokenId.toString());
            if (accessToken==null) {
                System.out.println("not found on database");
                return Optional.empty();
            }
            accessTokenTable.put(accessToken.getAccessTokenId(), accessToken);
        }
        return Optional.of(accessToken);
    }

    public AccessToken generateNewAccessToken(final User user, final DateTime dateTime) {
        AccessToken accessToken = new AccessToken(UUID.randomUUID().toString(), user.getId(), dateTime);
        accessTokenTable.put(accessToken.getAccessTokenId(), accessToken);
        persist(accessToken);
        currentSession().flush();
        return accessToken;
    }

    public void setLastAccessTime(final UUID accessTokenUUID, final DateTime dateTime) {
        AccessToken accessToken = accessTokenTable.get(accessTokenUUID.toString());
        if (accessToken==null) {
            accessToken = sessionFactory.openSession().get(AccessToken.class,accessTokenUUID.toString());
            
        }
        AccessToken updatedAccessToken = accessToken.withLastAccessUTC(dateTime);
        persist(updatedAccessToken);
        currentSession().flush();
        accessTokenTable.put(accessTokenUUID.toString(), updatedAccessToken);
    }

}
