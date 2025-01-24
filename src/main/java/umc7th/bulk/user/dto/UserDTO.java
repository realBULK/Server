package umc7th.bulk.user.dto;

import lombok.Getter;
import umc7th.bulk.user.domain.User;

public class UserDTO {

    @Getter
    public static class CharacterDTO {

        private String name;
        private int level;
        private Integer point;

        public CharacterDTO(User user) {
            name = user.getBulkCharacter().getName();
            level = user.getBulkCharacter().getLevel();
            point = user.getBulkCharacter().getPoint();
        }
    }

    @Getter
    public static class UserNutritionDTO {

        private Long curCalories;
        private Long curFats;
        private Long curProteins;
        private Long curCarbos;

        private Long calories;
        private Long fats;
        private Long proteins;
        private Long carbos;

        public UserNutritionDTO(User user) {
            curCalories = user.getCurCalories();
            curFats = user.getCurFats();
            curProteins = user.getCurProteins();
            curCarbos = user.getCurCarbos();

            calories = user.getCalories();
            fats = user.getFats();
            proteins = user.getProteins();
            carbos = user.getCarbos();
        }

    }
}
