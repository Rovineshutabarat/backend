package com.chelly.backend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_generator")
    @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq_id_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false, length = 25)
    private String username;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(length = 50)
    private String fullName;

    @Column(length = 16, nullable = false)
    private String phoneNumber;

    @Column(length = 200)
    private String address;

    @Column(name = "User_Level")
    private Integer level = 1;
    private Integer points = 0;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    private LocalDateTime birthdate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonManagedReference
    private List<Role> roles;

    @CreationTimestamp
    @Column(updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Jakarta")
    @JsonIgnore
    private Date createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Jakarta")
    @JsonIgnore
    private Date updatedAt;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .toList();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
