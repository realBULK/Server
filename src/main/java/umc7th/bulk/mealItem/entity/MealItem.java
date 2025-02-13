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
    @Builder.Default
    private Double grade = 0.0; // 음식 평점

    @Column(name = "grade_people_num")
    @Builder.Default
    private Long gradePeopleNum = 0L; // 평점 인원

    @Column(name = "record_count")
    @Builder.Default
    private Long recordCount = 0L; // 음식을 기록한 인원 수

    @OneToMany(mappedBy = "mealItem", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MealMealItemMapping> mealMealItemMappings = new ArrayList<>();


    // 후기 개수, 평균 평점 업데이트 메서드
    public void updateReviewStats(Long reviewCount, Double averageRate) {
        this.gradePeopleNum = reviewCount;
        this.grade = averageRate;
    }

    public void increaseRecordFoodCount() {
        recordCount++;
    }

}


