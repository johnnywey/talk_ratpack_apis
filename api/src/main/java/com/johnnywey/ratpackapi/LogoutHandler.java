package com.johnnywey.ratpackapi;

import com.johnnywey.ratpackapi.service.AuthenticationService;
import io.netty.handler.codec.http.Cookie;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.util.Optional;

public class LogoutHandler implements Handler {
    private final AuthenticationService authenticationService;

    public LogoutHandler(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void handle(Context context) throws Exception {
        Optional<Cookie> cookieBox = context
                .getRequest()
                .getCookies()
                .stream()
                .filter(c -> AuthenticatedApiHandler.AUTH_COOKIE_NAME.equals(c.getName())).findFirst();

        if (cookieBox.isPresent()) {
            authenticationService.destroySessionBySessionId(cookieBox.get().getValue());
            context.getResponse().expireCookie(AuthenticatedApiHandler.AUTH_COOKIE_NAME);
            context.redirect(LoginHandler.LOGIN_PAGE);
        }
    }
}
