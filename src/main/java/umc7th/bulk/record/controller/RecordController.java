package umc7th.bulk.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.record.dto.RecordRequestDto;
import umc7th.bulk.record.dto.RecordResponseDto;
import umc7th.bulk.record.service.RecordService;
import umc7th.bulk.record.upload.S3Service;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.service.UserService;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;
    private final S3Service s3Service;
    private final UserService userService;

    @Operation(summary = "유저가 **식단대로 먹었어요** 선택 시 기록 등록")
    @PostMapping
    public ResponseEntity<CustomResponse<RecordResponseDto>> createRecord(
            @RequestBody RecordRequestDto.Create requestDto) {
        RecordResponseDto responseDto = recordService.createRecord(requestDto);
        return ResponseEntity.ok(CustomResponse.onSuccess(GeneralSuccessCode.OK, responseDto));
    }

    @Operation(summary = "유저가 **다르게 먹었어요** 선택 시 기록 등록")
    @PostMapping(path = "/not-followed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponse<RecordResponseDto>> createNotFollowedRecord(
            @ModelAttribute RecordRequestDto.CreateNotFollowed requestDto) {

        RecordResponseDto responseDto = recordService.createNotFollowedRecord(requestDto);
        return ResponseEntity.ok(CustomResponse.onSuccess(GeneralSuccessCode.OK, responseDto));
    }


    @Operation(summary = "특정 유저의 특정 날짜, 특정 끼니 기록 조회")
    @GetMapping
    public ResponseEntity<CustomResponse<RecordResponseDto>> getRecord(
            @RequestParam @Parameter(description = "조회할 날짜 (yyyy-MM-dd)") String date,
            @RequestParam @Parameter(description = "끼니 타입 (BREAKFAST, LUNCH, DINNER, SNACK)") String mealType) {

        LocalDate localDate = LocalDate.parse(date);
        User user = userService.getAuthenticatedUserInfo();
        RecordResponseDto responseDto = recordService.getRecord(user, localDate, mealType);

        return ResponseEntity.ok(CustomResponse.onSuccess(GeneralSuccessCode.OK, responseDto));
    }

    @Operation(summary = "오늘 하루 동안 먹은 영양 정보 조회")
    @GetMapping("/today")
    public ResponseEntity<CustomResponse<RecordResponseDto.TodaySummary>> getTodayRecord() {
        User user = userService.getAuthenticatedUserInfo();
        RecordResponseDto.TodaySummary responseDto = recordService.getTodayRecord(user);
        return ResponseEntity.ok(CustomResponse.onSuccess(GeneralSuccessCode.OK, responseDto));
    }

}