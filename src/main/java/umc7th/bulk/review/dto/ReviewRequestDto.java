package umc7th.bulk.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewRequestDto {
    @Min(1)
    @Max(5)
    private Integer rate;
    private String content;
}
