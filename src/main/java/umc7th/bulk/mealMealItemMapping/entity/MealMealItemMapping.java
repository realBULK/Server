package umc7th.bulk.mealMealItemMapping.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.mealItem.entity.MealItem;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MealMealItemMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "meal_mealItem_mapping_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_item_id")
    private MealItem mealItem;

}
