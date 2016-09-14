package com.github.ricardobaumann;

import org.hibernate.SessionFactory;

import com.github.ricardobaumann.db.CommentDAO;
import com.github.ricardobaumann.db.Post;
import com.github.ricardobaumann.db.PostDAO;
import com.github.ricardobaumann.health.TemplateHealthCheck;
import com.github.ricardobaumann.providers.NotFoundExceptionProvider;
import com.github.ricardobaumann.providers.ValidationExceptionProvider;
import com.github.ricardobaumann.resources.CommentResource;
import com.github.ricardobaumann.resources.PostResource;
import com.hubspot.dropwizard.guice.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
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
   

    @Override
    public void initialize(final Bootstrap<BlogConfiguration> bootstrap) {
        GuiceBundle<BlogConfiguration> guiceBundle = GuiceBundle.<BlogConfiguration>newBuilder()
                .addModule(new BlogModule())
                .enableAutoConfig(getClass().getPackage().getName())
                .setConfigClass(BlogConfiguration.class)
                .build();

              bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(final BlogConfiguration configuration,
                    final Environment environment) {
        
    }
    
    

}
