package umc7th.bulk.record.service;

import umc7th.bulk.record.dto.RecordRequestDto;
import umc7th.bulk.record.dto.RecordResponseDto;

import java.time.LocalDate;

public interface RecordService {
    RecordResponseDto createRecord(RecordRequestDto.Create requestDto);
    RecordResponseDto getRecord(Long userId, LocalDate date, String mealType);
}
