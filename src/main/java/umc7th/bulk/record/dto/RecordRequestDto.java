package umc7th.bulk.record.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import umc7th.bulk.meal.entity.MealType;

import java.time.LocalDate;
import java.util.List;


@Schema(description = "기록 요청 DTO")
public class RecordRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @Schema(description = "식단대로 먹었을 시 기록 생성 요청 DTO")
    public static class Create {
        private LocalDate date;
        private MealType mealType;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Schema(description = "식단과 다르게 먹었을 시 기록 생성 요청 DTO")
    public static class CreateNotFollowed {
        private LocalDate date;
        private MealType mealType;

        @Schema(description = "사용자가 업로드한 이미지")
        private MultipartFile image;
    }
}