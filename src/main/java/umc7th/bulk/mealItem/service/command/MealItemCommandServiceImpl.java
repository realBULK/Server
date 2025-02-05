package umc7th.bulk.mealItem.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealItem.repository.MealItemRepository;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealItemCommandServiceImpl implements MealItemCommandService {

    private final MealItemRepository mealItemRepository;

    @Override
    public MealItem createMealItem(MealPlanRequestDTO.MealItemDTO dto) {

        Optional<MealItem> isExist = mealItemRepository.findByName(dto.getName());

        if (isExist.isPresent()) {
            return isExist.get();
        }

        return mealItemRepository.save(dto.toMealItemEntity(dto));
    }
}
