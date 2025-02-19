package umc7th.bulk.mealMealItemMapping.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.global.error.GeneralErrorCode;
import umc7th.bulk.global.error.exception.CustomException;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealItem.repository.MealItemRepository;
import umc7th.bulk.mealMealItemMapping.dto.MealMealItemMappingRequestDTO;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;
import umc7th.bulk.mealMealItemMapping.exception.MealMealItemMappingErrorCode;
import umc7th.bulk.mealMealItemMapping.exception.MealMealItemMappingErrorException;
import umc7th.bulk.mealMealItemMapping.repository.MealMealItemMappingRepository;
import umc7th.bulk.user.domain.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MealMealItemMappingCommandServiceImpl implements MealMealItemMappingCommandService {

    private final MealMealItemMappingRepository mappingRepository;
    private final MealItemRepository mealItemRepository;

    @Override
    public void deleteMealMealItemMapping(User user, Long mappingId) {

        MealMealItemMapping mealMealItemMapping = mappingRepository.findById(mappingId)
                .orElseThrow(() -> new MealMealItemMappingErrorException(MealMealItemMappingErrorCode.NOT_FOUND));

        if (!mealMealItemMapping.getMeal().getDailyMeal().getMealPlan().getUser().getId().equals(user.getId())) {
            throw new CustomException(GeneralErrorCode.FORBIDDEN_403);
        }
        mappingRepository.deleteById(mappingId);
    }

    @Override
    @Transactional
    public MealMealItemMapping createMapping(Meal meal, MealItem mealItem) {

        Optional<MealItem> mealItem1 = mealItemRepository.findByName(mealItem.getName());

        MealItem mealItem2 = null;
        if (mealItem1.isPresent()) {
           mealItem2 = mealItem1.get();
        } else {
            mealItem2 = mealItem;
        }

        MealMealItemMappingRequestDTO.CreateMealMealItemMappingDTO dto =
                new MealMealItemMappingRequestDTO.CreateMealMealItemMappingDTO();

        return mappingRepository.save(dto.toEntity(meal, mealItem2));
    }
}
