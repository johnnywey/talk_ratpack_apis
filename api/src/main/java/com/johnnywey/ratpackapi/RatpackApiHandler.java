package com.johnnywey.ratpackapi;

import ratpack.handling.Handler;
import ratpack.launch.HandlerFactory;
import ratpack.launch.LaunchConfig;

import static ratpack.handling.Handlers.chain;

public class RatpackApiHandler implements HandlerFactory {

    @Override
    public Handler create(LaunchConfig launchConfig) throws Exception {
        return chain(launchConfig, (chain) -> chain.handler(context -> context.render("Hello World!!"))
        );
    }
}
