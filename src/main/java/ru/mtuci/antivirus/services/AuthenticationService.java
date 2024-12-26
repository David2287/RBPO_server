package ru.mtuci.antivirus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.repositories.UserRepository;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String login, String password) {
        User user = userRepository.findUserByLogin(login);
        if (user == null) {
            logger.warn("Authentication failed: user not found for login {}", login);
            return false;
        }
        boolean isAuthenticated = passwordEncoder.matches(password, user.getPassword());
        if (isAuthenticated) {
            logger.info("Authentication successful for login {}", login);
        } else {
            logger.warn("Authentication failed: incorrect password for login {}", login);
        }
        return isAuthenticated;
    }
}
