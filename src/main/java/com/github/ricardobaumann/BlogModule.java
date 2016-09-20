package com.github.ricardobaumann;

import javax.inject.Singleton;
import javax.persistence.Entity;

import org.hibernate.SessionFactory;
import org.reflections.Reflections;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;

public class BlogModule extends AbstractModule {

    @Singleton
    public static class MyHibernateBundle extends HibernateBundle<BlogConfiguration> implements ConfiguredBundle<BlogConfiguration>
    {
        private static ImmutableList<Class<?>> myDbEntities()
        {
            Reflections reflections = new Reflections("com.github.ricardobaumann.db");
            ImmutableList<Class<?>> entities = ImmutableList.copyOf(reflections.getTypesAnnotatedWith(Entity.class));
            return entities;
        }
        
        public MyHibernateBundle()
        {
            super(myDbEntities(), new SessionFactoryFactory());
        }


        @Override
        public PooledDataSourceFactory getDataSourceFactory(BlogConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    }

    @Provides
    public SessionFactory sessionFactory(MyHibernateBundle hibernate)
    {
        return hibernate.getSessionFactory();
    }

    @Override
    protected void configure() {
        // TODO Auto-generated method stub
        
    }
}
