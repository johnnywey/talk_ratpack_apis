package com.johnnywey.ratpackapi;

import com.johnnywey.ratpackapi.client.MongoClientConfig;
import com.johnnywey.ratpackapi.client.MongoClientConfigDevelopment;
import de.caluga.morphium.Morphium;
import ratpack.handling.Handler;
import ratpack.launch.HandlerFactory;
import ratpack.launch.LaunchConfig;

import static ratpack.handling.Handlers.chain;

public class RatpackApiHandler implements HandlerFactory {

    @Override
    public Handler create(LaunchConfig launchConfig) throws Exception {
        // Setup DB
        // :NOTE: - Can put DB properties into LaunchConfig properties or in environment vars
        MongoClientConfig mongoClientConfig = new MongoClientConfigDevelopment();
        Morphium morphium = new Morphium(mongoClientConfig.toConfig().get());

        // Load sample data ...
        Bootstrap.loadSampleData(morphium);

        Handler helloWorldHandler = new HelloWorldHandler();
        return chain(launchConfig, (chain) -> chain.handler(helloWorldHandler));
    }
}
