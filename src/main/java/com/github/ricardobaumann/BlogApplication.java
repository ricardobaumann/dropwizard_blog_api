package com.github.ricardobaumann;

import com.google.inject.Inject;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.lifecycle.Managed;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.github.ricardobaumann.db.User;
import com.hubspot.dropwizard.guice.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.net.URI;

public class BlogApplication extends Application<BlogConfiguration> {

    public static void main(final String[] args) throws Exception {

        new BlogApplication().run(args);
    }

    //private static final MyManaged MY_MANAGED = new MyManaged(environment);

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

        environment.lifecycle().manage(new MyManaged(environment));

    }

    public static class MyManaged implements Managed {

        private static CloseableHttpClient client;

        @Inject
        public MyManaged(Environment environment) {
            if (client==null) {
                client = new HttpClientBuilder(environment).build("eureka");
            }

        }

        @Override
        public void start() throws Exception {
            System.out.println("Yayyyyy, started");
            HttpPost post = new HttpPost();
            String text = "{\n" +
                    "    \"instance\": {\n" +
                    "        \"hostName\": \"WKS-SOF-L011\",\n" +
                    "        \"app\": \"dwblog_app\",\n" +
                    "        \"vipAddress\": \"localhost\",\n" +
                    "        \"secureVipAddress\": \"localhost\"\n" +
                    "        \"ipAddr\": \"localhost\",\n" +
                    "        \"status\": \"STARTING\",\n" +
                    "        \"port\": {\"$\": \"8080\", \"@enabled\": \"true\"},\n" +
                    "        \"securePort\": {\"$\": \"8443\", \"@enabled\": \"false\"},\n" +
                   // "        \"healthCheckUrl\": \"http://WKS-SOF-L011:8080/healthcheck\",\n" +
                    //"        \"statusPageUrl\": \"http://WKS-SOF-L011:8080/status\",\n" +
                    //"        \"homePageUrl\": \"http://WKS-SOF-L011:8080\",\n" +
                    "        \"dataCenterInfo\": {\n" +
                    "            \"@class\": \"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo\", \n" +
                    "            \"name\": \"MyOwn\"\n" +
                    "        },\n" +
                    "    }\n" +
                    "}";
            post.setEntity(EntityBuilder.create().setText(text).build());

            post.setHeader("Content-Type","application/json");

            post.setURI(new URI("http://localhost:1111/eureka/apps/dwblog_app"));

            CloseableHttpResponse res = client.execute(post);
            res.getEntity().writeTo(System.out);
        }

        @Override
        public void stop() throws Exception {
            System.out.println("Yayyyyy, stop");
        }
    }

}
