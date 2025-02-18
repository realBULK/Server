package umc7th.bulk.user.principal;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.exception.UserErrorCode;
import umc7th.bulk.user.exception.UserException;
import umc7th.bulk.user.repository.UserRepository;

/**
 * Spring Security에서 사용자 정보를 조회하기 위해 UserDetailsService 인터페이스를 구현한 서비스
 * 사용자의 이메일을 기반으로 데이터를 조회하고, 인증에 사용할 UserDetails 객체를 반환
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 주어진 username(이메일)으로 사용자를 조회하여 UserDetails를 반환하는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 이메일로 회원 정보 조회
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UserException(UserErrorCode.USER_NOT_FOUND)); // 회원이 없을 경우 예외 발생
        // 조회된 회원 정보를 기반으로 PrincipalDetails 객체를 생성해 반환
        return new PrincipalDetails(user);
    }
}
