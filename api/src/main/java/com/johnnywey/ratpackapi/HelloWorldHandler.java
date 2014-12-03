package com.johnnywey.ratpackapi;

import ratpack.handling.Context;
import ratpack.handling.Handler;

public class HelloWorldHandler implements Handler {

    @Override
    public void handle(Context context) throws Exception {
        context.render("Hello World!");
    }
}
