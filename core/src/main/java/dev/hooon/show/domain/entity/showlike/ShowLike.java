package dev.hooon.show.domain.entity.showlike;

import dev.hooon.common.entity.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "show_like_table")
@NoArgsConstructor(access = PROTECTED)
public class ShowLike extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "show_like_id")
    private Long id;

    @Column(name = "show_like_user_id", nullable = false)
    private Long userId;

    @Column(name = "show_like_show_id", nullable = false)
    private Long showId;
}
