package umc7th.bulk.meal.dto;

import lombok.*;
import umc7th.bulk.mealItem.dto.MealItemDTO;

import java.util.List;

public class MealResponseDTO {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class MealPreviewDTO {

        private MealDTO.MealSummaryDTO mealSummary;
        private List<MealItemDTO.MealItemPreviewDTO> items;
        private Long nextCursorId;

        public static MealPreviewDTO from(MealDTO.MealSummaryDTO mealSummary, List<MealItemDTO.MealItemPreviewDTO> items, Long nextCursorId) {
            return MealPreviewDTO.builder()
                    .mealSummary(mealSummary)
                    .items(items)
                    .nextCursorId(nextCursorId)
                    .build();
        }
    }
}
