package umc7th.bulk.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.record.dto.RecordRequestDto;
import umc7th.bulk.record.dto.RecordResponseDto;
import umc7th.bulk.record.service.RecordService;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    @Operation(summary = "유저가 **식단대로 먹었어요** 선택 시 기록 등록")
    @PostMapping
    public ResponseEntity<CustomResponse<RecordResponseDto>> createRecord(
            @RequestParam Long userId,
            @RequestBody RecordRequestDto requestDto) {
        RecordResponseDto responseDto = recordService.createRecord(userId, requestDto);
        return ResponseEntity.ok(CustomResponse.onSuccess(GeneralSuccessCode.OK, responseDto));
    }

    @Operation(summary = "특정 유저의 특정 날짜, 특정 끼니 기록 조회")
    @GetMapping
    public ResponseEntity<CustomResponse<RecordResponseDto>> getRecord(
            @RequestParam @Parameter(description = "유저 ID") Long userId,
            @RequestParam @Parameter(description = "조회할 날짜 (yyyy-MM-dd)") String date,
            @RequestParam @Parameter(description = "끼니 타입 (BREAKFAST, LUNCH, DINNER, SNACK)") String mealType) {

        LocalDate localDate = LocalDate.parse(date);
        RecordResponseDto responseDto = recordService.getRecord(userId, localDate, mealType);

        return ResponseEntity.ok(CustomResponse.onSuccess(GeneralSuccessCode.OK, responseDto));
    }
}