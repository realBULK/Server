package umc7th.bulk.mealPlan.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.global.BaseTimeEntity;
import umc7th.bulk.user.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// 일주일 식단 계획
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MealPlan extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_plan_id")
    private Long id;

    @Column(name = "start_date")
    private LocalDate start_date; // 식단 시작일

    @Column(name = "end_date")
    private LocalDate end_date; // 식단 종료일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL)
    private List<DailyMeal> dailyMeals = new ArrayList<>();
}
