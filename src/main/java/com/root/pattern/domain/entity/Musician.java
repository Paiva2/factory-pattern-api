package com.root.pattern.domain.entity;

import com.root.pattern.domain.enums.Role;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "TB_MUSICIANS")
@Entity
public class Musician {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "M_ID")
    private Long id;

    @Column(name = "M_ROLE", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.MUSICIAN;

    @Column(name = "MUS_EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "MUS_PASSWORD", nullable = false)
    private String password;

    @Column(name = "M_DISABLED")
    private boolean disabled = false;

    @Column(name = "M_DISABLED_AT")
    private Date disabledAt;

    @CreationTimestamp
    @Column(name = "M_CREATED_AT", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "M_UPDATED_AT", nullable = false)
    private Date updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "musician")
    List<Album> albums;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "musician")
    List<Music> musics;
}
