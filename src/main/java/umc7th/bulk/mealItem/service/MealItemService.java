package umc7th.bulk.mealItem.service;

import umc7th.bulk.mealItem.dto.MealItemDTO;
import umc7th.bulk.mealItem.entity.MealItem;

import java.util.List;

public interface MealItemService {
    List<MealItemDTO.MealItemPreviewDTO> getMealItems(Long cursor);
    List<MealItemDTO.MealItemPreviewDTO> searchMealItems(String keyword, Long cursor);

    List<MealItemDTO.MealItemPopularityDTO> getPopularityMealItems();

    List<MealItemDTO.MealItemSearchInfoDTO> getSearchMealItemInfo(Long mealItemId);
}
