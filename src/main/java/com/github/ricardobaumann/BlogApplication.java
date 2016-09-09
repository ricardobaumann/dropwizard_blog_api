package com.github.ricardobaumann;

import com.github.ricardobaumann.health.TemplateHealthCheck;
import com.github.ricardobaumann.resources.PostResource;

import io.dropwizard.Application;
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
        // TODO: application initialization
    }

    @Override
    public void run(final BlogConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(new PostResource());
        
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck();
            environment.healthChecks().register("template", healthCheck);
            
    }

}
