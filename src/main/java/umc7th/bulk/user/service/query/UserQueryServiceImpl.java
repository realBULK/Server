package umc7th.bulk.user.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import umc7th.bulk.global.jwt.util.JwtProvider;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.dto.UserResponseDTO;
import umc7th.bulk.user.exception.UserErrorCode;
import umc7th.bulk.user.exception.UserException;
import umc7th.bulk.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    /** 본인 정보 조회 */
    @Override
    public UserResponseDTO.UserInforDTO getProfile(User user) {

        return UserResponseDTO.UserInforDTO.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
//                .alarmStatus(user.getAlarmStatus())
//                .alarmTime(user.getAlarmTime())
                .build();
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
}

