package umc7th.bulk.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDTO {

    @Getter
    public static class UpdateUserDTO {
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
    }

    @Getter
    public static class UpdateUserReportDTO {
        private Long calories;
        private Long carbos;
        private Long proteins;
        private Long fats;

    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLoginDTO {

        private String email;

        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupDTO {

        private String email;

        private String password;
    }
}
