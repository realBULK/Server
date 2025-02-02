package umc7th.bulk.record.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.recordedFood.entity.RecordedFood;
import umc7th.bulk.user.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "food_photo")
    private String foodPhoto;

    @Column(name = "ate_on_plan")
    private boolean ateOnPlan;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealType mealType;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecordedFood> foods = new ArrayList<>();

}
