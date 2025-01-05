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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Controller;

@Controller
@Component
@Transactional
public class UserResolver {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserResolver(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
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

    // @MutationMapping
    // public User createUser(@Argument String name, @Argument String email, 
    //                        @Argument String password) {
    //     try {
    //         User user = new User();
    //         user.setName(name);
    //         user.setEmail(email);
    //         user.setPassword(passwordEncoder.encode(password));
    //         user.setRoles(Set.of("USER"));
    //         return userRepo.save(user);
    //     } catch (Exception e) {
    //         System.out.println("Error: " + e);
    //         return null;
    //     }
    // }

    @MutationMapping
    public User updateUser(@Argument Long id, @Argument String name, 
                           @Argument String email, @Argument String password, 
                           @Argument Set<String> roles) {
        User user = userRepo.findById(id).orElseThrow();
        if (name != null) user.setName(name);
        if (email != null) user.setEmail(email);
        if (password != null) user.setPassword(passwordEncoder.encode(password));
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
    private String password;
    
    // Getters and Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
