package ru.mtuci.antivirus.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.antivirus.entities.requests.UserRequest;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Получение информации о пользователе по ID
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/info/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long id){
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(404).body("ERROR: User with this ID not found");
        }
        return ResponseEntity.status(200).body("User: " + user.toString());
    }

    // Получение информации обо всех пользователях
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/info")
    public ResponseEntity<List<User>> getUserInfo(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(200).body(users);
    }

    // Обновление информации о пользователе
    @PatchMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserRequest user, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String findUsername = userDetails.getUsername();
            User currentUser = (User) userService.findUserByLogin(findUsername);

            // Обработка изменений логина
            if (user.getLogin() != null && !user.getLogin().equals(currentUser.getLogin())) {
                if (userService.existsByLogin(user.getLogin())) {
                    return ResponseEntity.status(400).body("ERROR: Login totally used");
                }
                currentUser.setLogin(user.getLogin());
            }

            // Обработка изменений email
            if (user.getEmail() != null && !user.getEmail().equals(currentUser.getEmail())) {
                if (userService.existsByEmail(user.getEmail())) {
                    return ResponseEntity.status(400).body("ERROR: Email totally used");
                }
                currentUser.setEmail(user.getEmail());
            }

            // Обработка изменения пароля
            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                currentUser.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            }

            // Сохранение обновленного пользователя
            userService.saveUser(currentUser);

            return ResponseEntity.status(200).body("User: " + currentUser.getLogin() + " successfully updated");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error via updated user");
        }
    }

    // Удаление пользователя
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(404).body("ERROR: User with this ID not founded");
            }

            userService.deleteUser(id);
            return ResponseEntity.status(200).body("User with this ID: " + id + " successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error via deleted user");
        }
    }
}
