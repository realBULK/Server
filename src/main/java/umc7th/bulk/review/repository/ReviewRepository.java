package umc7th.bulk.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umc7th.bulk.review.entity.Review;
import umc7th.bulk.user.domain.User;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMealItemIdOrderByCreatedAtDesc(Long mealItemId); // 최신순으로

    boolean existsByMealItemIdAndWriter(Long mealItemId, User writer);

    @Query("SELECT COALESCE(AVG(r.rate), 0) FROM Review r WHERE r.mealItem.id = :mealItemId")
    Double findAverageRateByMealItemId(@Param("mealItemId") Long mealItemId);

    Long countReviewsByMealItemId(Long mealItemId);
}
