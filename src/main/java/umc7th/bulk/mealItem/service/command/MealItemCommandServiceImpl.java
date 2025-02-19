package umc7th.bulk.mealItem.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealItem.repository.MealItemRepository;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class MealItemCommandServiceImpl implements MealItemCommandService {

    private final MealItemRepository mealItemRepository;

    @Override
    @Transactional
    public MealItem createMealItem(MealPlanRequestDTO.MealItemDTO dto) {

        Optional<MealItem> mealItem1 = mealItemRepository.findByName(dto.getName());

        MealItem mealItem2 = null;
        if (mealItem1.isPresent()) {
            mealItem2 = mealItem1.get();
        } else {
            mealItem2 = mealItemRepository.save(dto.toMealItemEntity(dto));
        }

        return mealItem2;
    }
}
