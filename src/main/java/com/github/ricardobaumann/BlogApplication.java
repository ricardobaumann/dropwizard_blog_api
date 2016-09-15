package com.github.ricardobaumann;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.github.ricardobaumann.db.User;
import com.hubspot.dropwizard.guice.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.db.DataSourceFactory;
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

    @Override
    public void initialize(final Bootstrap<BlogConfiguration> bootstrap) {
        GuiceBundle<BlogConfiguration> guiceBundle = GuiceBundle.<BlogConfiguration> newBuilder()
                .addModule(new BlogModule()).enableAutoConfig(getClass().getPackage().getName())
                .setConfigClass(BlogConfiguration.class).build();

        bootstrap.addBundle(guiceBundle);

        bootstrap.addBundle(new MigrationsBundle<BlogConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(BlogConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void run(final BlogConfiguration configuration, final Environment environment) {
      environment.jersey().register(RolesAllowedDynamicFeature.class);
      environment.jersey().register(new AuthValueFactoryProvider.Binder(User.class));
    }

}
