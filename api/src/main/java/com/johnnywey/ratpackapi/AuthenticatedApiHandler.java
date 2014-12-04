package com.johnnywey.ratpackapi;

import com.johnnywey.flipside.failable.Fail;
import com.johnnywey.flipside.failable.Failable;
import com.johnnywey.ratpackapi.domain.User;
import com.johnnywey.ratpackapi.service.AuthenticationService;
import io.netty.handler.codec.http.Cookie;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Status;

import java.util.Optional;

import static ratpack.registry.Registries.just;

public class AuthenticatedApiHandler implements Handler {
    public final static String AUTH_COOKIE_NAME = "RATPACK_AUTH";

    private final AuthenticationService authenticationService;

    public AuthenticatedApiHandler(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void handle(Context context) throws Exception {
        Optional<Cookie> cookieBox = context
                .getRequest()
                .getCookies()
                .stream()
                .filter(c -> AUTH_COOKIE_NAME.equals(c.getName())).findFirst();

        Failable<User> userFailable;

        if (cookieBox.isPresent() &&
                (userFailable = authenticationService.findUserForSession(cookieBox.get().getValue())).isSuccess()) {
            context.next(just(User.class, userFailable.get()));
        } else {
            context.getResponse().status(new UnauthorizedStatus("Not authorized."));
            context.render("Not authorized.");
        }
    }

    /**
     * Internal impl of an unauthorized response.
     */
    protected class UnauthorizedStatus implements Status {
        private final String message;

        public UnauthorizedStatus(String message) {
            this.message = message;
        }

        @Override
        public int getCode() {
            return Fail.ACCESS_DENIED.getHttpResponseCode();
        }

        @Override
        public String getMessage() {
            return this.message;
        }
    }
}
