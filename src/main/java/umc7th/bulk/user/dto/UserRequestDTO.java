package umc7th.bulk.user.dto;

import lombok.Getter;
import umc7th.bulk.user.domain.User;

public class UserRequestDTO {

    @Getter
    public static class CreateUserQuestionDTO{
        private String nickname;
        private Double height;
        private Double weight;
        private Double goalWeight;
        private String activityLevel;
        private String mealNumber;
        private String cookTime;
        private String deliveryNum;
        private String mealTime;
        private String eatingOut;
        private String favoriteFood;

        public User toEntity() {
            return User.builder()
                    .nickname(this.nickname)
                    .height(this.height)
                    .weight(this.weight)
                    .goalWeight(this.goalWeight)
                    .activityLevel(this.activityLevel)
                    .mealNumber(this.mealNumber)
                    .cookTime(this.cookTime)
                    .deliveryNum(this.deliveryNum)
                    .mealTime(this.mealTime)
                    .eatingOut(this.eatingOut)
                    .favoriteFood(this.favoriteFood)
                    .build();
        }
    }
}
