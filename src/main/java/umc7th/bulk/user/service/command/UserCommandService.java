package umc7th.bulk.user.service.command;

import umc7th.bulk.user.dto.UserRequestDTO;
import umc7th.bulk.user.dto.UserResponseDTO;

public interface UserCommandService {
    UserResponseDTO.UserTokenDTO signup(UserRequestDTO.SignupDTO dto); // 회원가입 완료
    UserResponseDTO.UserTokenDTO login(UserRequestDTO.UserLoginDTO dto); // 로그인
}
