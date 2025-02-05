package umc7th.bulk.meal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc7th.bulk.meal.entity.Meal;

public interface MealRepository extends JpaRepository<Meal, Long> {

}
