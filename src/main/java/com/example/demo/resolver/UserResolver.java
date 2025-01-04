package com.example.demo.resolver;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<UserDTO> users() {
        List<User> users = userRepo.findAll();
        return users.stream()
                   .map(this::convertToDTO)
                   .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles());
        return dto;
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

class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Set<String> roles;
    
    // Getters and Setters (sans password)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    } 

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    
}
