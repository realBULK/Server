package umc7th.bulk.mealMealItemMapping.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;
import umc7th.bulk.mealMealItemMapping.exception.MealMealItemMappingErrorCode;
import umc7th.bulk.mealMealItemMapping.exception.MealMealItemMappingErrorException;
import umc7th.bulk.mealMealItemMapping.repository.MealMealItemMappingRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MealMealItemMappingCommandServiceImpl implements MealMealItemMappingCommandService {

    private final MealMealItemMappingRepository mappingRepository;

    @Override
    public void deleteMealMealItemMapping(Long mappingId) {

        MealMealItemMapping mealMealItemMapping = mappingRepository.findById(mappingId)
                .orElseThrow(() -> new MealMealItemMappingErrorException(MealMealItemMappingErrorCode.NOT_FOUND));

        mappingRepository.deleteById(mappingId);
    }
}
