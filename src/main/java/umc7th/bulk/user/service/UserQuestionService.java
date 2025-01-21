package umc7th.bulk.user.service;

import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.dto.UserRequestDTO;

import java.util.Optional;

public interface UserQuestionService {
    boolean nicknameCheck(String nickname);
    User updateUser(Long id, UserRequestDTO.UpdateUserDTO dto);

    User updateUserReport(Long id, UserRequestDTO.UpdateUserReportDTO dto);

    User getUserReport(Long id);

}
