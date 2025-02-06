package umc7th.bulk.record.service;

import umc7th.bulk.record.dto.RecordRequestDto;
import umc7th.bulk.record.dto.RecordResponseDto;
import umc7th.bulk.user.domain.User;

import java.time.LocalDate;

public interface RecordService {
    RecordResponseDto createRecord(RecordRequestDto.Create requestDto);
    RecordResponseDto createNotFollowedRecord(RecordRequestDto.CreateNotFollowed requestDto);
    RecordResponseDto getRecord(User user, LocalDate date, String mealType);
}
