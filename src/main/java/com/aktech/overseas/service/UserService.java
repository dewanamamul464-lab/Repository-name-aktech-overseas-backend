package com.aktech.overseas.service;

import com.aktech.overseas.dto.UserDTO;
import com.aktech.overseas.entity.User;
import com.aktech.overseas.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User register(User user) {
        return userRepository.save(user);
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public List<UserDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    private UserDTO convertToDTO(User user) {

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}