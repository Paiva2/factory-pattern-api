package com.root.pattern.domain.entity;

import com.root.pattern.domain.enums.Role;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "TB_USERS")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "U_ID", nullable = false)
    private Long id;

    @Column(name = "U_PASSWORD", nullable = false)
    private String password;

    @Column(name = "U_EMAIL", unique = true)
    private String email;

    @Column(name = "U_NAME", nullable = false)
    private String name;

    @Column(name = "U_ROLE", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "TB_USER_FAVORITE_LIST",
            joinColumns = {@JoinColumn(name = "USF_USER_ID", referencedColumnName = "U_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USF_FAVORITE_ID", referencedColumnName = "FAV_ID")})
    private Favorite userFavoriteList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Playlist> playlists;

    @CreationTimestamp
    @Column(name = "U_CREATED_AT", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "U_UPDATED_AT", nullable = false)
    private Date updatedAt;

    @Column(name = "U_DISABLED")
    private boolean disabled = false;

    @Column(name = "U_DISABLED_AT")
    private Date disabledAt;
}
