package com.johnnywey.ratpackapi;

import com.johnnywey.ratpackapi.client.MongoClientConfig;
import com.johnnywey.ratpackapi.client.MongoClientConfigDevelopment;
import com.johnnywey.ratpackapi.service.UserService;
import de.caluga.morphium.Morphium;
import ratpack.guice.Guice;
import ratpack.handling.Handler;
import ratpack.jackson.JacksonModule;
import ratpack.launch.HandlerFactory;
import ratpack.launch.LaunchConfig;

import static ratpack.jackson.Jackson.json;

public class RatpackApiHandler implements HandlerFactory {

    @Override
    public Handler create(LaunchConfig launchConfig) throws Exception {
        // Setup DB
        // :NOTE: - Can put DB properties into LaunchConfig properties or in environment vars
        MongoClientConfig mongoClientConfig = new MongoClientConfigDevelopment();
        Morphium morphium = new Morphium(mongoClientConfig.toConfig().get());

        // Load sample data ...
        Bootstrap.loadSampleData(morphium);

        UserService userService = new UserService(morphium);

        Handler baseHandler = new BaseHandler();

        return Guice.builder(launchConfig)
                .bindings(bindingSpec -> bindingSpec.add(new JacksonModule()))
                .build((chain) ->
                        chain.prefix("api", (api) ->
                                        api.get("user", context -> context.render(json(userService.findAll())))
                        ).handler(baseHandler));
    }
}
