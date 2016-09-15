package com.github.ricardobaumann;

import java.security.Principal;

import org.glassfish.jersey.server.validation.internal.ValidationExceptionMapper;
import org.hibernate.SessionFactory;

import com.github.ricardobaumann.db.CommentDAO;
import com.github.ricardobaumann.db.Post;
import com.github.ricardobaumann.db.PostDAO;
import com.github.ricardobaumann.health.TemplateHealthCheck;
import com.github.ricardobaumann.providers.NotFoundExceptionProvider;
import com.github.ricardobaumann.providers.ValidationExceptionProvider;
import com.github.ricardobaumann.resources.CommentResource;
import com.github.ricardobaumann.resources.PostResource;
import com.github.ricardobaumann.security.SimpleAuthenticator;
import com.github.ricardobaumann.security.User;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class BlogApplication extends Application<BlogConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BlogApplication().run(args);
    }

    @Override
    public String getName() {
        return "Blog";
    }
    
    private final HibernateBundle<BlogConfiguration> hibernate = new HibernateBundle<BlogConfiguration>(Post.class) {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(BlogConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(final Bootstrap<BlogConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(new MigrationsBundle<BlogConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(BlogConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final BlogConfiguration configuration,
                    final Environment environment) {
        SessionFactory sessionFactory = hibernate.getSessionFactory();
        final PostDAO postDAO = new PostDAO(sessionFactory);
        
        environment.jersey().register(new PostResource(postDAO));
        environment.jersey().register(new CommentResource(new CommentDAO(sessionFactory), postDAO));
        
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck();
            environment.healthChecks().register("template", healthCheck);
            
            //TODO disabling auth because of dropwizard problems with simple authentications
           /*
            *  environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<Principal>()
                    .setAuthenticator(new SimpleAuthenticator())
                    .buildAuthFilter())); 
            */
            
            environment.jersey().register(new ValidationExceptionProvider());
            environment.jersey().register(new NotFoundExceptionProvider());
    }
    
    

}
