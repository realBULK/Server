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

    @Column(name = "calories")
    private Long calories;

    @Column(name = "carbos")
    private Long carbos;

    @Column(name = "proteins")
    private Long proteins;

    @Column(name = "fats")
    private Long fats;

}


