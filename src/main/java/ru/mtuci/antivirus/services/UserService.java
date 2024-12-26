package ru.mtuci.antivirus.services;

import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.repositories.UserRepository;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = repository.findUserByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with login: " + username);
        }
        return user;
    }

    public UserDetails findUserByLogin(String username) {
        return null;
    }

    public User getUserById(Long ownerId) {
        return repository.getUserById(ownerId);
    }

    public void saveUser(User user) {
        repository.save(user);
    }

    public User getUserByLogin(@NotBlank(message = "Login cannot be empty") String login) {
        return null;
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
