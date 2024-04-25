package com.root.pattern.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "TB_FAVORITES")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "FAV_ID")
    private UUID id;

    @Column(name = "FAV_DISABLED")
    private boolean disabled = false;

    @Column(name = "FAV_DISABLED_AT")
    private Date disabledAt;

    @CreationTimestamp
    @Column(name = "FAV_CREATED_AT", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "FAV_UPDATED_AT", nullable = false)
    private Date updatedAt;

    @OneToOne(mappedBy = "userFavoriteList")
    private User user;
}
