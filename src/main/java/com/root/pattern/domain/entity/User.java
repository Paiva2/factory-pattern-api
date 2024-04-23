package com.root.pattern.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

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
