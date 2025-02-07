package umc7th.bulk.mealItem.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealItem.repository.MealItemRepository;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;


@Service
@RequiredArgsConstructor
@Slf4j
public class MealItemCommandServiceImpl implements MealItemCommandService {

    private final MealItemRepository mealItemRepository;

    @Override
    @Transactional
    public MealItem createMealItem(MealPlanRequestDTO.MealItemDTO dto) {

        log.info("🔍 Checking if meal item exists: {}", dto.getName());
        MealItem saveMealItem = mealItemRepository.saveAndFlush(dto.toMealItemEntity(dto));
        return saveMealItem;

//        System.out.println(mealItem.getName());

        /*if (mealItem == null) {
            MealItem saveMeal = mealItemRepository.save(dto.toMealItemEntity(dto));
            return saveMeal;
        }*/

        /*if (mealItems.size() >= 2) {
            log.warn("⚠️ Found existing meal item: {}", dto.getName());
            mealItemRepository.deleteLastByName(dto.getName());
        }*/

        /*MealItem newMealItem = mealItemRepository.save(dto.toMealItemEntity(dto));
        log.info("💾 Saving new meal item: {}", newMealItem);*/

//        return mealItem;

    }
}
