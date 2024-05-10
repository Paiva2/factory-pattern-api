package com.root.pattern.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "TB_PLAYLISTS")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PLY_ID")
    private UUID id;

    @Column(name = "PLY_NAME", nullable = false)
    private String name;

    @Column(name = "PLY_COVER_IMG")
    private String coverImage;

    @OneToMany(mappedBy = "playlist", fetch = FetchType.LAZY)
    private Set<PlaylistMusic> playlistMusics;

    @Column(name = "PLY_ORDER", nullable = false)
    private Integer order;

    @Column(name = "PLY_DISABLED")
    private boolean disabled = false;

    @Column(name = "PLY_DISABLED_AT")
    private Date disabledAt;
    
    @CreationTimestamp
    @Column(name = "PLY_CREATED_AT", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "PLY_UPDATED_AT", nullable = false)
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PLY_USER_ID")
    private User user;
}
