package umc7th.bulk.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc7th.bulk.global.BaseTimeEntity;

@Entity
@Table(name = "user")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, name = "kakao_id")
    private String kakaoId;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, name = "gender")
    private String gender;

    @Column(nullable = false, name = "birth_year")
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

    @Column(nullable = true, name = "record_complete")
    private boolean recordComplete;

    @Column(nullable = false, name = "access_token")
    private String accessToken;

    @Column(nullable = false, name = "refresh_token")
    private String refreshToken;
}
