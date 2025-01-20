package umc7th.bulk.user.service;

import umc7th.bulk.user.domain.User;

import java.util.Optional;

public interface UserQuestionService {
    boolean nicknameCheck(String nickname);
    User getUser(Long userId);

}
