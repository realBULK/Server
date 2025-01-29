package umc7th.bulk.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import umc7th.bulk.stageRecord.dto.StageInfoDto;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class GroupMapResponseDto {
    private Long groupId;
    private int currentStage;
    private List<StageInfoDto> stages;
}
