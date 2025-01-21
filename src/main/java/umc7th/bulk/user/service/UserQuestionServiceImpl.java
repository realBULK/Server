package umc7th.bulk.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.dto.UserRequestDTO;
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
    public User updateUser(Long id, UserRequestDTO.UpdateUserDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserException(UserErrorCode.USER_NOT_FOUND));

        user.update(dto.getNickname(), dto.getHeight(), dto.getWeight(), dto.getGoalWeight(), dto.getActivityLevel(), dto.getMealNumber(),
                dto.getCookTime(), dto.getDeliveryNum(), dto.getMealTime(), dto.getEatingOut(), dto.getFavoriteFood());
        return userRepository.save(user);
    }

    @Override
    public User updateUserReport(Long id, UserRequestDTO.UpdateUserReportDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserException(UserErrorCode.USER_NOT_FOUND));

        user.reportUpdate(dto.getCalories(), dto.getCarbos(), dto.getProteins(), dto.getFats());
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserReport(Long id) {

        User user = userRepository.findById(id).orElseThrow(() ->
                new UserException(UserErrorCode.USER_NOT_FOUND));
        return user;
    }
}
