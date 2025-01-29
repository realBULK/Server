package umc7th.bulk.mealItem.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.meal.entity.Meal;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MealItem { // 각 식사별 음식 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_item_id")
    private Long id;

    @Column(name = "meal_item_name", nullable = false)
    private String name;

    @Column(name = "gram")
    private Long gram;

    @Column(name = "calories")
    private Long calories;

    @Column(name = "carbos")
    private Long carbos;

    @Column(name = "proteins")
    private Long proteins;

    @Column(name = "fats")
    private Long fats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id")
    private Meal meal;
}


