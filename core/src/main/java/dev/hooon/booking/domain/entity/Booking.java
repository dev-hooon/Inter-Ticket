package dev.hooon.booking.domain.entity;

import dev.hooon.common.entity.TimeBaseEntity;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.ConstraintMode.NO_CONSTRAINT;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "booking_table")
@NoArgsConstructor(access = PROTECTED)
public class Booking extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "booking_user_id", nullable = false, foreignKey = @ForeignKey(value = NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "booking_show_id", nullable = false, foreignKey = @ForeignKey(value = NO_CONSTRAINT))
    private Show show;
}
