package umc7th.bulk.mealItem.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "unit", length = 30)
    private String unit;

    @Column(name = "grade")
    private Double grade; // 음식 평점

    @Column(name = "grade_people")
    private Long gradePeopleNum; // 평점 인원

    @OneToMany(mappedBy = "mealItem", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MealMealItemMapping> mealMealItemMappings = new ArrayList<>();


}


