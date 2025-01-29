package umc7th.bulk.stageRecord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StageInfoDto {
    private int stageNumber;
    private int recordedUsers;
    private int totalUsers;
}
