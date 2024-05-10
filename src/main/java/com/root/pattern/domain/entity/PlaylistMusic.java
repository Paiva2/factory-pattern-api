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
@Table(name = "TB_PLAYLIST_MUSICS")
public class PlaylistMusic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PLM_ID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "PLM_MUSIC_ID")
    private Music music;

    @ManyToOne
    @JoinColumn(name = "PLM_PLAYLIST_ID")
    private Playlist playlist;

    @Column(name = "PLM_MUSIC_PLAYLIST_ORDER", nullable = false)
    private Integer musicPlaylistOrder;

    @Column(name = "PLM_DISABLED")
    private boolean disabled = false;

    @Column(name = "PLM_DISABLED_AT")
    private Date disabledAt;
    @CreationTimestamp
    @Column(name = "PLM_CREATED_AT", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "PLM_UPDATED_AT", nullable = false)
    private Date updatedAt;
}
