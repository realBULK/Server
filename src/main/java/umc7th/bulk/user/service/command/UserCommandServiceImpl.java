package umc7th.bulk.user.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umc7th.bulk.global.jwt.util.JwtProvider;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.dto.UserRequestDTO;
import umc7th.bulk.user.dto.UserResponseDTO;
import umc7th.bulk.user.enums.UserStatus;
import umc7th.bulk.user.exception.UserErrorCode;
import umc7th.bulk.user.exception.UserException;
import umc7th.bulk.user.repository.UserRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    @Override
    public UserResponseDTO.UserTokenDTO signup(UserRequestDTO.SignupDTO dto) {
        log.info("Completing signup for email: {}", dto.getEmail());

        String emailKey = dto.getEmail().trim().toLowerCase();

        // 이미 가입된 이메일인지 확인
        if (userRepository.existsByEmail(emailKey)) {
            throw new UserException(UserErrorCode.ALREADY_EXIST);
        }

        // 비밀번호 암호화
        String encodedPassword = encoder.encode(dto.getPassword());

        // AccessToken & RefreshToken 생성
        User tempUser = User.builder()
                .email(emailKey)
                .password(encodedPassword)
                .build();

        String accessToken = jwtProvider.createAccessToken(tempUser);
        String refreshToken = jwtProvider.createRefreshToken(tempUser);

        // User 저장
        User user = User.builder()
                .email(emailKey)
                .password(encodedPassword)
                .accessToken(accessToken) // 추가
                .refreshToken(refreshToken) // 추가
                .build();

        try {
            user = userRepository.save(user);
            log.info("User saved successfully: {}", user);
        } catch (Exception e) {
            log.error("Error while saving user to the database", e);
            throw e;
        }

        return UserResponseDTO.UserTokenDTO.builder()
                .accessToken(jwtProvider.createAccessToken(user))
                .refreshToken(jwtProvider.createRefreshToken(user))
                .redirectUrl("https://bulkapp.site/report")
                .build();
    }

    /** 로그인 */
    @Override
    public UserResponseDTO.UserTokenDTO login(UserRequestDTO.UserLoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 탈퇴 상태 확인
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new UserException(UserErrorCode.INACTIVE_ACCOUNT);
        }

        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.INCORRECT_PASSWORD);
        }

        return UserResponseDTO.UserTokenDTO.builder()
                .accessToken(jwtProvider.createAccessToken(user))
                .refreshToken(jwtProvider.createRefreshToken(user))
                .redirectUrl("https://bulkapp.site/home")
                .build();
    }

}
