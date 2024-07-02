package io.sillysillyman.deventer.security;

import io.sillysillyman.deventer.entity.User;
import io.sillysillyman.deventer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));

        return new UserDetailsImpl(user);
    }
}