package umc7th.bulk.mealItem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc7th.bulk.mealItem.dto.MealItemDTO;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealItem.repository.MealItemRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MealItemServiceImpl implements MealItemService{

    private final MealItemRepository mealItemRepository;
    private static final int PAGE_SIZE = 10;

    public List<MealItemDTO.MealItemPreviewDTO> getMealItems(Long cursor) {
        Page<MealItem> page = mealItemRepository.findByCursor(
                cursor == null ? Long.MAX_VALUE : cursor,
                PageRequest.of(0, PAGE_SIZE)
        );
        return page.getContent().stream().map(MealItemDTO.MealItemPreviewDTO::new).collect(Collectors.toList());
    }

    public List<MealItemDTO.MealItemPreviewDTO> searchMealItems(String keyword, Long cursor) {
        Page<MealItem> page = mealItemRepository.searchByKeyword(
                keyword,
                cursor == null ? Long.MAX_VALUE : cursor,
                PageRequest.of(0, PAGE_SIZE)
        );
        return page.getContent().stream().map(MealItemDTO.MealItemPreviewDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<MealItemDTO.MealItemPopularityDTO> getPopularityMealItems() {

        List<MealItem> mealItems = mealItemRepository.findTop5MealItemByRecordCount(PageRequest.of(0, 5));
        
        // 인기 식단 순위 매겨서 반환
        return IntStream.range(0, mealItems.size())
                .mapToObj(k -> new MealItemDTO.MealItemPopularityDTO(mealItems.get(k), k + 1))
                .toList();
    }
}
