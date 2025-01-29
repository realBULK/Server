package umc7th.bulk.dailyMeal.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.mealPlan.entity.MealPlan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// 하루 식단
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class DailyMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_meal_id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "daily_calories")
    private Long dailyCalories;

    @Column(name = "daily_carbos")
    private Long dailyCarbos;

    @Column(name = "daily_proteins")
    private Long dailyProteins;

    @Column(name = "daily_fats")
    private Long dailyFats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_id")
    private MealPlan mealPlan;

    @OneToMany(mappedBy = "dailyMeal", cascade = CascadeType.ALL)
    private List<Meal> meals = new ArrayList<>();
}
