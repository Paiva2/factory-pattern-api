package com.root.pattern.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "TB_ALBUMS")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ALB_ID")
    private UUID id;

    @Column(name = "ALB_NAME", nullable = false)
    private String name;

    @Column(name = "ALB_DISABLED")
    private Boolean disabled = false;

    @Column(name = "ALB_DISABLED_AT")
    private Date disabledAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "album")
    @OrderBy("albumOrder ASC")
    private List<Music> music;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALB_MUSICIAN_ID")
    private Musician musician;

    @CreationTimestamp
    @Column(name = "ALB_CREATED_AT", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "ALB_UPDATED_AT", nullable = false)
    private Date updatedAt;
}
