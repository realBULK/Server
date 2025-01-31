package umc7th.bulk.recordedFood.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.record.entity.Record;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class RecordedFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordedFoodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private Record record;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private MealItem foodId;

    @Column(nullable = false)
    private int quantity; // 음식 섭취량 (gram)

}
