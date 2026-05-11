package com.learning.tracker.service;

import com.learning.tracker.model.User;
import com.learning.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void registerUser(User user) {
        userRepository.save(user);
    }

    public boolean authenticate(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.isPresent() && optionalUser.get().getPassword().equals(password);
    }

    public boolean verifySecurityAnswers(String username, String friendName, String schoolName, String petName) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getFriendName().equalsIgnoreCase(friendName) &&
                   user.getSchoolName().equalsIgnoreCase(schoolName) &&
                   user.getPetName().equalsIgnoreCase(petName);
        }
        return false;
    }

    public void updatePassword(String username, String newPassword) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(newPassword);
            userRepository.save(user);
        }
    }
}
