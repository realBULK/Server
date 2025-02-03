package umc7th.bulk.record.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;
import umc7th.bulk.meal.entity.MealType;

import java.time.LocalDate;


@Schema(description = "기록 요청 DTO")
public class RecordRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @Schema(description = "기록 생성 요청 DTO")
    public static class Create {
        private Long userId;
        private LocalDate date;
        private MealType mealType;
        private String foodPhoto;
    }
}