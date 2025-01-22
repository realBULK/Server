package umc7th.bulk.dayplan.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.user.domain.User;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DayPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_plan_id")
    private Long id;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @Column(name = "calories")
    private Long calories;

    @Column(name = "carbos")
    private Long carbos;

    @Column(name = "proteins")
    private Long proteins;

    @Column(name = "fats")
    private Long fats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
