package umc7th.bulk.meal.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.global.BaseTimeEntity;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// 각 식단
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Meal extends BaseTimeEntity { // 식사 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Long id;

    @Column(name = "meal_type")
    @Enumerated(EnumType.STRING)
    private MealType type;

    @Column(name = "meal_calories")
    private Long mealCalories;

    @Column(name = "meal_carbohydrates")
    private Long mealCarbos;

    @Column(name = "meal_proteins")
    private Long mealProteins;

    @Column(name = "meal_fats")
    private Long mealFats;

    @Column(name = "day")
    private LocalDate localDate;

    @Column(name = "meal_name")
    private String mealName;

    @Column(name = "price")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_meal_id")
    private DailyMeal dailyMeal;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MealMealItemMapping> mealMealItemMappings = new ArrayList<>();
}
