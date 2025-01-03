package com.example.demo.resolver;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Controller;

@Controller
@Component
@Transactional
public class UserResolver {
    private final UserRepository userRepo;

    public UserResolver(UserRepository userRepo) {
        this.userRepo = userRepo;
        System.out.println("UserResolver initialized with repository: " + userRepo);
        System.out.println("Initial user count: " + userRepo.count());
    }

    @QueryMapping
    public List<User> users() {
        List<User> users = userRepo.findAll();
        System.out.println("Fetched users count: " + users.size());
        users.forEach(user -> System.out.println("User found: " + user.getName()));
        return users;
    }

    @MutationMapping
    public User createUser(@Argument String name, @Argument String email, 
                           @Argument String password, @Argument Set<String> roles) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        return userRepo.save(user);
    }

    @MutationMapping
    public User updateUser(@Argument Long id, @Argument String name, 
                           @Argument String email, @Argument String password, 
                           @Argument Set<String> roles) {
        User user = userRepo.findById(id).orElseThrow();
        if (name != null) user.setName(name);
        if (email != null) user.setEmail(email);
        if (password != null) user.setPassword(password);
        if (roles != null && !roles.isEmpty()) user.setRoles(roles);
        return userRepo.save(user);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        userRepo.deleteById(id);
        return true;
    }
}
