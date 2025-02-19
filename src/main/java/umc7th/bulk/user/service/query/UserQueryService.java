package umc7th.bulk.user.service.query;

import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.dto.UserResponseDTO;

public interface UserQueryService {
    UserResponseDTO.UserInforDTO getProfile(User user); // 본인 정보 조회
    User getUser(String email);
}
