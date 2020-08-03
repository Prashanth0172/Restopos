package com.hyva.restopos.Interceptor.Interceptor;


import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class UserInterceptor extends HandlerInterceptorAdapter {
//    @Autowired
//    BsUserRepository bsUserRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("request = " + request.getRequestURL());
        Cookie[] accessTokens = request.getCookies();
        Cookie accessTokenCookie = null;
        String accessToken = null;
        String user = null;
        if (accessTokens != null) {
            Stream<Cookie> cookieStream = Arrays.stream(accessTokens)
                    .filter(cookie -> cookie.getName().equalsIgnoreCase("fullName"));
            Optional<Cookie> first = cookieStream.findFirst();
            if (first.isPresent()) {
                accessTokenCookie = first.get();
                user = accessTokenCookie.getValue();
            }
        }

        if(user == null) {
            HttpSession session = request.getSession();
            user = (String) session.getAttribute("fullName");
        }

        if (StringUtils.isBlank(user)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return true;
        }
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(518400);//extending time for 60 days
        response.addCookie(accessTokenCookie);
        request.setAttribute("fullName", user);
        return true;
    }
}
