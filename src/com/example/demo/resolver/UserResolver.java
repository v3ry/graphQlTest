package com.example.demo.resolver;

public class UserResolver {
  private UserRepository userRepo;
  public UserResolver(UserRepository userRepo) {
    this.userRepo = userRepo;
  }
  @QueryMapping
  public List<User> users() {
    return userRepo.findAll();
  }
  @MutationMapping
  public User createUser(@Argument String name, @Argument String email) {
    User user = new User();
    user.setName(name);
    user.setEmail(email);
    return userRepo.save(user);
  }
  @MutationMapping
  public User updateUser(@Argument Long id, @Argument String name, @Argument String email) {
    User user = userRepo.findById(id).orElseThrow();
    user.setName(name);
    user.setEmail(email);
    return userRepo.save(user);
  }
  @MutationMapping
  public Boolean deleteUser(@Argument Long id) {
    userRepo.deleteById(id);
    return true;
  }
    
}
