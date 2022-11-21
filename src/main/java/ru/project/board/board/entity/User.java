package ru.project.board.board.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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

    @NotBlank
    @Column(unique = true, name = "phonenumber")
    @Pattern(regexp = "^\\+\\d{11}", message = "The phone number must match +___________")
    private String phoneNumber;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Pattern(regexp = "[а-яА-Яa-zA-Z]*", message = "The name must contain only alphabetic characters")
    @NotBlank(message = "Name required")
    private String name;

    @Pattern(regexp = "[а-яА-Яa-zA-Z]*", message = "The surname must contain only alphabetic characters")
    @NotBlank(message = "Surname required")
    private String surname;

    private Role role;

    private boolean active;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Ad> adList;

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

    public User() {
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

    public List<Ad> getAdList() {
        return adList;
    }

    public void setAdList(List<Ad> adList) {
        this.adList = adList;
    }
}