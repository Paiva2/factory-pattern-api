package com.root.pattern.domain.entity;

import com.root.pattern.domain.enums.MusicCategory;
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
@Table(name = "TB_CATEGORIES")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CAT_ID")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "CAT_NAME", nullable = false, unique = true)
    private MusicCategory name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    List<Music> musics;

    @Column(name = "CAT_DISABLED")
    private Boolean disabled = false;

    @Column(name = "CAT_DISABLED_AT")
    private Date disabledAt;

    @CreationTimestamp
    @Column(name = "CAT_CREATED_AT", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "CAT_UPDATED_AT", nullable = false)
    private Date updatedAt;
}
