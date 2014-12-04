package com.johnnywey.ratpackapi;

import com.johnnywey.flipside.failable.Failable;
import com.johnnywey.ratpackapi.domain.User;
import com.johnnywey.ratpackapi.service.AuthenticationService;
import com.johnnywey.ratpackapi.service.UserService;
import io.netty.handler.codec.http.Cookie;
import org.mindrot.jbcrypt.BCrypt;
import ratpack.form.Form;
import ratpack.handling.Context;
import ratpack.handling.Handler;

public class LoginHandler implements Handler {
    public final static String LOGIN_PAGE = "/login.html";
    public final static String SUCCESS_PAGE = "/success.html";

    public final static String LOGIN_FAILURE_STRING = "?error=true";

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public LoginHandler(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @Override
    public void handle(Context context) throws Exception {
        Form form = context.parse(Form.class);

        String username = form.get("username");
        String password = form.get("password");

        Failable<User> user = userService.findUserByUsername(username);
        if(!(user.isSuccess() && BCrypt.checkpw(password, user.get().getPassword()))) {
            context.redirect(LOGIN_PAGE + LOGIN_FAILURE_STRING);
        } else {
            // Create session
            String sessionId = authenticationService.createSessionForUser(user.get());
            Cookie cookie = context.getResponse().cookie(AuthenticatedApiHandler.AUTH_COOKIE_NAME, sessionId);
            context.getResponse().getCookies().add(cookie);
            context.redirect(SUCCESS_PAGE);
        }
    }
}
