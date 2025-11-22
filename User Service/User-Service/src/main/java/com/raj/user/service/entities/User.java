package com.raj.user.service.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "ID")
    private String userId;   // internal primary key (UUID)

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String userEmail;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_auth0_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "auth0_id")
    private List<String> auth0Id = new ArrayList<>();

    @Column(name = "USERNAME", length = 50)
    private String userName;

    @Column(name = "PICTURE")
    private String picture;

    @Column(name = "EMAIL_VERIFIED")
    private boolean emailVerified;

    @Column(name = "ENABLED")
    private boolean enabled = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<UserRole> roles = new ArrayList<>();

    @Transient
    private List<Rating> ratings = new ArrayList<>();
}
