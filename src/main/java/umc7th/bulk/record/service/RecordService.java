package umc7th.bulk.record.service;

import umc7th.bulk.record.dto.RecordRequestDto;
import umc7th.bulk.record.dto.RecordResponseDto;
import umc7th.bulk.record.dto.RecordWithGPTResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface RecordService {
    RecordResponseDto createRecord(RecordRequestDto.Create requestDto);
    RecordWithGPTResponseDTO createNotFollowedRecord(RecordRequestDto.CreateNotFollowed requestDto);
    RecordResponseDto getRecord(Long userId, LocalDate date, String mealType);
}
