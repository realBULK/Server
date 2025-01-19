package umc7th.bulk.food.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    @Column(name = "food_name", nullable = false)
    private String name;

    @Column(name = "gram")
    private Long gram;

    @Column(name = "calorie")
    private Long cal;

    @Column(name = "carbohydrate")
    private Long carb;

    @Column(name = "protein")
    private Long protein;

    @Column(name = "fat")
    private Long fat;

}


