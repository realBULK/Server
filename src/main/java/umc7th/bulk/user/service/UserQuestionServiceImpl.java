package umc7th.bulk.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.exception.UserErrorCode;
import umc7th.bulk.user.exception.UserException;
import umc7th.bulk.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQuestionServiceImpl implements UserQuestionService {

    private final UserRepository userRepository;
    @Override
    public boolean nicknameCheck(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public User getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserException(UserErrorCode.USER_NOT_FOUND));
        return user;
    }
}
