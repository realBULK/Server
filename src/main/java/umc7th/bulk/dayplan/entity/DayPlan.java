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

    @Column(name = "calorie")
    private Long cal;

    @Column(name = "carbohydrate")
    private Long carb;

    @Column(name = "protein")
    private Long protein;

    @Column(name = "fat")
    private Long fat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
