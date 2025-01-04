package com.example.demo.resolver;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class AuthResolver {
    
    private final AuthService authService;

    public AuthResolver(AuthService authService) {
        this.authService = authService;
    }

    @MutationMapping
    public User register(@Argument String name, @Argument String email, @Argument String password) {
        return authService.registerUser(name, email, password);
    }

    @MutationMapping
    public User login(@Argument String email, @Argument String password) {
        Authentication authentication = authService.login(email, password);
        return authService.getCurrentUser();
    }

    @MutationMapping
    public boolean logout() {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder
            .currentRequestAttributes())
            .getRequest()
            .getSession(false);
        
        authService.logout(session);
        return true;
    }

    @QueryMapping
    public User currentUser() {
        return authService.getCurrentUser();
    }
}
