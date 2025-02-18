package umc7th.bulk.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc7th.bulk.character.entity.BulkCharacter;
import umc7th.bulk.global.BaseTimeEntity;
import umc7th.bulk.group.entity.Group;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;
import umc7th.bulk.user.enums.UserStatus;

@Entity
@Table(name = "user")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true, name = "kakao_id")
    private String kakaoId;

    @Column(nullable = false, length = 50)
    public String email;

    @Column(length = 100)
    public String password; // 비밀번호는 nullable로 설정 (소셜 로그인 시 필요 없을 수 있음)

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15)")
    public UserStatus status;

    @Column(nullable = true, name = "gender")
    private String gender;

    @Column(nullable = true, name = "birth_year")
    private String birthyear;

    @Column(nullable = true, name = "nickname")
    private String nickname;

    @Column(nullable = true, name = "height")
    private Double height;

    @Column(nullable = true, name = "weight")
    private Double weight;

    @Column(nullable = true, name = "goal_weight")
    private Double goalWeight;

    @Column(nullable = true, name = "activity_level")
    private String activityLevel;//활동량

    @Column(nullable = true, name = "meal_number")
    private String mealNumber; //식사 횟수

    @Column(nullable = true, name = "cook_time")
    private String cookTime; //요리시간

    @Column(nullable = true, name = "delivery_num")
    private String deliveryNum; //배달음식 빈도

    @Column(nullable = true, name = "meal_time")
    private String mealTime; //식사시간 규칙적

    @Column(nullable = true, name = "eating_out")
    private String eatingOut; //외식 빈도

    @Column(nullable = true, name = "favorite_food")
    private String favoriteFood;

    @Column(nullable = false, name = "record_complete")
    private boolean recordComplete;

    @Column(nullable = false, name = "access_token")
    private String accessToken;

    @Column(nullable = false, name = "refresh_token")
    private String refreshToken;

    @Column(name = "target_calories")
    private Long targetCalories;

    @Column(name = "target_fats")
    private Long targetFats;

    @Column(name = "target_proteins")
    private Long targetProteins;

    @Column(name = "target_carbos")
    private Long targetCarbos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = true)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private BulkCharacter bulkCharacter;

    @Column(name = "cur_calories")
    private Long curCalories;

    @Column(name = "cur_fats")
    private Long curFats;

    @Column(name = "cur_proteins")
    private Long curProteins;

    @Column(name = "cur_carbos")
    private Long curCarbos;

    @Column(name = "BMR", nullable = true)
    private Long BMR;

    @Column(name = "TDEE" , nullable = true)
    private Long TDEE;

    public void update(String nickname, Double height, Double weight, Double goalWeight, String activityLevel, String mealNumber, String cookTime,
                       String deliveryNum, String mealTime, String eatingOut, String favoriteFood) {
        this.nickname = nickname;
        this.height = height;
        this.weight = weight;
        this.goalWeight = goalWeight;
        this.activityLevel = activityLevel;
        this.mealNumber = mealNumber;
        this.cookTime = cookTime;
        this.deliveryNum = deliveryNum;
        this.mealTime = mealTime;
        this.eatingOut = eatingOut;
        this.favoriteFood = favoriteFood;
    }

    public void reportUpdate(Long calories, Long carbos, Long proteins, Long fats) {
        this.targetCalories = calories;
        this.targetCarbos = carbos;
        this.targetProteins = proteins;
        this.targetFats = fats;
    }

    public void updateTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void updateGoalNutrition(MealPlanRequestDTO.TargetNutritionDTO targetNutritionDTO) {
        this.targetCalories = targetNutritionDTO.getTarget_calories();
        this.targetCarbos = targetNutritionDTO.getCarbs_target();
        this.targetProteins = targetNutritionDTO.getProtein_target();
        this.targetFats = targetNutritionDTO.getFat_target();
        this.BMR = targetNutritionDTO.getBmr();
        this.TDEE = targetNutritionDTO.getTdee();
    }
}
