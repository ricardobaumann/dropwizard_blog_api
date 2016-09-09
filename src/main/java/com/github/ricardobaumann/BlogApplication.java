package com.github.ricardobaumann;

import com.github.ricardobaumann.db.Post;
import com.github.ricardobaumann.db.PostDAO;
import com.github.ricardobaumann.health.TemplateHealthCheck;
import com.github.ricardobaumann.resources.PostResource;

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
    
    private final HibernateBundle<BlogConfiguration> hibernate = new HibernateBundle<BlogConfiguration>(Post.class) {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(BlogConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(final Bootstrap<BlogConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(final BlogConfiguration configuration,
                    final Environment environment) {
        final PostDAO postDAO = new PostDAO(hibernate.getSessionFactory());
        environment.jersey().register(new PostResource(postDAO));
        
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck();
            environment.healthChecks().register("template", healthCheck);
    }
    
    

}
