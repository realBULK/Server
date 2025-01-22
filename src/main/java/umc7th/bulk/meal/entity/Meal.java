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

    @Column(name = "calories")
    private Long calories;

    @Column(name = "carbohydrates")
    private Long carbs;

    @Column(name = "proteins")
    private Long proteins;

    @Column(name = "fats")
    private Long fats;

    private String unit;
}
