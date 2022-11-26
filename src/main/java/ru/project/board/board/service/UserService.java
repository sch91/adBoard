package ru.project.board.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.project.board.board.entity.Role;
import ru.project.board.board.entity.User;
import ru.project.board.board.exception.UserNotFoundException;
import ru.project.board.board.repository.UserRepo;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepo.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return user;
    }

    public void registration(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRole(Role.ROLE_USER);
        userRepo.save(user);
    }

    public User getUserById(UUID id) throws UserNotFoundException {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public boolean isPhoneNumberAlreadyExists(String phoneNumber) {
        User user = userRepo.findByPhoneNumber(phoneNumber);
        return user != null;
    }
}
