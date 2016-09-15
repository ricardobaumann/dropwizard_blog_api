package com.github.ricardobaumann.health;

import com.hubspot.dropwizard.guice.InjectableHealthCheck;

public class TemplateHealthCheck extends InjectableHealthCheck {

    public TemplateHealthCheck() {
    }

    @Override
    protected Result check() throws Exception {
        final String saying = String.format("hello", "TEST");
        if (!saying.contains("TEST")) {
            return Result.unhealthy("template doesn't include a name");
        }
        return Result.healthy();
    }

    @Override
    public String getName() {
        return "blog health";
    }
}
