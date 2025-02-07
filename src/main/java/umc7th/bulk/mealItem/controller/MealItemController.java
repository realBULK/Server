package umc7th.bulk.mealItem.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc7th.bulk.mealItem.dto.MealItemDTO;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealItem.service.MealItemService;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class MealItemController {
    private final MealItemService mealItemService;

    // 전체 음식 조회 (무한 스크롤)
    @GetMapping
    public ResponseEntity<List<MealItemDTO.MealItemPreviewDTO>> getMealItems(@RequestParam(required = false) Long cursor) {
        List<MealItemDTO.MealItemPreviewDTO> mealItems = mealItemService.getMealItems(cursor);
        return ResponseEntity.ok(mealItems);
    }

    // 검색어 기반 음식 조회 (무한 스크롤)
    @GetMapping("/keyword")
    public ResponseEntity<List<MealItemDTO.MealItemPreviewDTO>> searchMealItems(@RequestParam String keyword, @RequestParam(required = false) Long cursor) {
        List<MealItemDTO.MealItemPreviewDTO> mealItems = mealItemService.searchMealItems(keyword, cursor);
        return ResponseEntity.ok(mealItems);
    }
}
