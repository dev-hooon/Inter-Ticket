package dev.hooon.show.domain.entity.showlike;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.hooon.common.entity.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
