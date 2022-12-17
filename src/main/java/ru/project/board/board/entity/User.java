package ru.project.board.board.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, length = 16)
    private UUID id;

    @NotBlank(message = "Поле номер телефона не должно быть пустым")
    @Column(unique = true, name = "phoneNumber", nullable = false)
    @Pattern(regexp = "^\\+\\d{11}", message = "Номер телефона должен быть в формате '+###########'")
    private String phoneNumber;

    @NotBlank(message = "Поле пароль не должно быть пустым")
    @Column(nullable = false)
    @Size(min = 4, message = "Пароль должен быть не менее 4 символов")
    private String password;

    @Pattern(regexp = "[а-яА-Яa-zA-Z]*-?[а-яА-Яa-zA-Z]*", message = "Имя должно содержать только буквенные символы")
    @NotBlank(message = "Поле имя не должно быть пустым")
    private String name;

    @Pattern(regexp = "[а-яА-Яa-zA-Z]*-?[а-яА-Яa-zA-Z]*", message = "Фамилия должна содержать только буквенные символы")
    @NotBlank(message = "Поле фамилия не должно быть пустым")
    private String surname;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean active;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Advertisement> advertisementList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    public User() {
    }

    public User(String phoneNumber, String password, String name, String surname) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Advertisement> getAdvertisementList() {
        return advertisementList;
    }

    public void setAdvertisementList(List<Advertisement> advertisementList) {
        this.advertisementList = advertisementList;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
}
