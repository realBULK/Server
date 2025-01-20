package umc7th.bulk.user.dto;

import lombok.*;

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

}
