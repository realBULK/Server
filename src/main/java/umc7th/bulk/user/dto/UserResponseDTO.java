package umc7th.bulk.user.dto;

import lombok.*;
import umc7th.bulk.user.domain.User;

import java.time.LocalDateTime;

public class UserResponseDTO {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DuplicateCheckResponseDTO {
        private String nickname;
        private boolean isDuplicated;

        public static DuplicateCheckResponseDTO from(String nickname, boolean isDuplicated) {
            return DuplicateCheckResponseDTO.builder()
                    .nickname(nickname)
                    .isDuplicated(isDuplicated)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class ReviewPreviewDTO {
        private Long id;
        private Long calories;
        private Long carbos;
        private Long proteins;
        private Long fats;

        public static ReviewPreviewDTO from(User user) {
            return ReviewPreviewDTO.builder()
                    .id(user.getId())
                    .calories(user.getTargetCalories())
                    .carbos(user.getTargetCarbos())
                    .proteins(user.getTargetProteins())
                    .fats(user.getTargetFats())
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserReportResponseDTO {
        private Long id;
        private Double weight;
        private Double goalWeight;
        private Long calories;
        private Long carbos;
        private Long proteins;
        private Long fats;

        public static  UserReportResponseDTO from(User user) {
            return UserReportResponseDTO.builder()
                    .id(user.getId())
                    .weight(user.getWeight())
                    .goalWeight(user.getGoalWeight())
                    .calories(user.getTargetCalories())
                    .carbos(user.getTargetCarbos())
                    .proteins(user.getTargetProteins())
                    .fats(user.getTargetFats())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class UpdateUserResponseDTO {
        private Long id;
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
        private LocalDateTime updatedAt;

        public static UpdateUserResponseDTO from(User user) {
            return UpdateUserResponseDTO.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .height(user.getHeight())
                    .weight(user.getWeight())
                    .goalWeight(user.getGoalWeight())
                    .activityLevel(user.getActivityLevel())
                    .mealNumber(user.getMealNumber())
                    .cookTime(user.getCookTime())
                    .deliveryNum(user.getDeliveryNum())
                    .mealTime(user.getMealTime())
                    .eatingOut(user.getEatingOut())
                    .favoriteFood(user.getFavoriteFood())
                    .updatedAt(user.getUpdatedAt())
                    .build();
        }

    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserInfoDTO {
        private UserDTO.CharacterDTO characterDTO;
        private UserDTO.UserNutritionDTO nutritionDTO;

        public static UserInfoDTO from(UserDTO.CharacterDTO bulkCharacterDTO, UserDTO.UserNutritionDTO nutritionDTO) {
            return UserInfoDTO.builder()
                    .characterDTO(bulkCharacterDTO)
                    .nutritionDTO(nutritionDTO)
                    .build();
        }
    }

    // 토큰 정보 응답
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserTokenDTO {
        private String accessToken;
        private String refreshToken;
        private String redirectUrl;
    }

    // 회원 정보 응답 시 필요한 정보
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInforDTO {
        private String email;
        private String nickname;
    }

}
