package ru.project.board.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.project.board.board.entity.Avatar;
import ru.project.board.board.entity.Role;
import ru.project.board.board.entity.User;
import ru.project.board.board.exception.UserNotFoundException;
import ru.project.board.board.repository.AvatarRepo;
import ru.project.board.board.repository.UserRepo;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AvatarRepo avatarRepo;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepo.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return user;
    }

    public void registration(User user, MultipartFile file) throws IOException {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRole(Role.ROLE_USER);
        user = userRepo.save(user);
        if (file.getSize() != 0) {
            user.setAvatar(toAvatar(file, user));
        } else {
            user.setAvatar(null);
        }
        userRepo.save(user);
    }

    @Transactional
    public void edit(User currentUser, User user, MultipartFile file) throws IOException {
        currentUser.setName(user.getName());
        currentUser.setSurname(user.getSurname());
        if (file.getSize() != 0) {
            avatarRepo.delete(currentUser.getAvatar());
            currentUser.setAvatar(toAvatar(file, currentUser));
        }
        userRepo.save(currentUser);
    }

    private Avatar toAvatar(MultipartFile file, User user) throws IOException {
        Avatar avatar = new Avatar();
        avatar.setName(file.getName());
        avatar.setOriginalFileName(file.getOriginalFilename());
        avatar.setContentType(file.getContentType());
        avatar.setSize(file.getSize());
        avatar.setBytes(file.getBytes());
        avatar.setUser(user);
        avatar = avatarRepo.save(avatar);
        return avatar;
    }

    public User getUserById(UUID id) throws UserNotFoundException {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public boolean isPhoneNumberAlreadyExists(String phoneNumber) {
        User user = userRepo.findByPhoneNumber(phoneNumber);
        return user != null;
    }

    public void deleteUserById(UUID id) {
        userRepo.deleteById(id);
    }

    public Iterable<User> getAllUsers() {
        return userRepo.findAll();
    }
}
