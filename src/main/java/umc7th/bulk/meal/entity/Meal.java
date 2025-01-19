package umc7th.bulk.meal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Long id;

    @Column(name = "meal_type")
    @Enumerated(EnumType.STRING)
    private MealType type;

    @Column(name = "calorie")
    private Long cal;

    @Column(name = "carbohydrate")
    private Long carb;

    @Column(name = "protein")
    private Long protein;

    @Column(name = "fat")
    private Long fat;

    private String unit;
}
