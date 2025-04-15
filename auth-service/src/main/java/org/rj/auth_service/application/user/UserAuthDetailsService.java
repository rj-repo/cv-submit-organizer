package org.rj.auth_service.application.user;

import lombok.RequiredArgsConstructor;
import org.rj.auth_service.application.user.exception.AuthUserNotFoundException;
import org.rj.auth_service.domain.user.model.AuthUser;
import org.rj.auth_service.domain.user.ports.out.UserAuthRepoPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserAuthDetailsService implements UserDetailsService {

    private final UserAuthRepoPort userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = userRepository.findByEmail(username).orElseThrow(() -> new AuthUserNotFoundException(username));

        return new org.springframework.security.core.userdetails.User(
                authUser.getEmail(),
                authUser.getPassword(),
                authUser.isEnabled(),
                true,
                true,
                true,
                Collections.emptyList());
    }
}
