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
@Table(name = "TB_MUSICS")
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MUS_ID")
    private UUID id;

    @Column(name = "MUS_DURATION", nullable = false)
    private Long duration;

    @Column(name = "MUS_NAME", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MUS_ALBUM_ID")
    private Album album;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MUS_MUSICIAN_ID", nullable = false)
    private Musician musician;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MUS_CATEGORY_ID")
    private Category category;

    @Column(name = "MUS_ALBUM_ORDER")
    private Long albumOrder;

    @OneToMany(mappedBy = "music")
    private Set<PlaylistMusic> playlistMusics;

    @Column(name = "MUSIC_IS_SINGLE", nullable = false)
    private Boolean isSingle;

    @Column(name = "MUS_DISABLED")
    private Boolean disabled = false;

    @Column(name = "MUS_DISABLED_AT")
    private Date disabledAt;

    @CreationTimestamp
    @Column(name = "MUS_CREATED_AT", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "MUS_UPDATED_AT", nullable = false)
    private Date updatedAt;
}
