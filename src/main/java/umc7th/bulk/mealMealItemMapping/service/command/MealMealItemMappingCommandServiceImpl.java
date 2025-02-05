package umc7th.bulk.mealMealItemMapping.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.meal.exception.MealErrorCode;
import umc7th.bulk.meal.exception.MealErrorException;
import umc7th.bulk.meal.repository.MealRepository;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealItem.exception.MealItemErrorCode;
import umc7th.bulk.mealItem.exception.MealItemErrorException;
import umc7th.bulk.mealItem.repository.MealItemRepository;
import umc7th.bulk.mealMealItemMapping.dto.MealMealItemMappingRequestDTO;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;
import umc7th.bulk.mealMealItemMapping.exception.MealMealItemMappingErrorCode;
import umc7th.bulk.mealMealItemMapping.exception.MealMealItemMappingErrorException;
import umc7th.bulk.mealMealItemMapping.repository.MealMealItemMappingRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MealMealItemMappingCommandServiceImpl implements MealMealItemMappingCommandService {

    private final MealMealItemMappingRepository mappingRepository;
    private final MealRepository mealRepository;
    private final MealItemRepository mealItemRepository;

    @Override
    public void deleteMealMealItemMapping(Long mappingId) {

        MealMealItemMapping mealMealItemMapping = mappingRepository.findById(mappingId)
                .orElseThrow(() -> new MealMealItemMappingErrorException(MealMealItemMappingErrorCode.NOT_FOUND));

        mappingRepository.deleteById(mappingId);
    }

    @Override
    public MealMealItemMapping createMapping(Meal meal, MealItem mealItem) {

        mealRepository.findById(meal.getId()).orElseThrow(
                () -> new MealErrorException(MealErrorCode.NOT_FOUND)
        );

        mealItemRepository.findById(mealItem.getId()).orElseThrow(
                () -> new MealItemErrorException(MealItemErrorCode.NOT_FOUND)
        );
        MealMealItemMappingRequestDTO.CreateMealMealItemMappingDTO dto =
                new MealMealItemMappingRequestDTO.CreateMealMealItemMappingDTO();
        return mappingRepository.save(dto.toEntity(meal, mealItem));
    }
}
