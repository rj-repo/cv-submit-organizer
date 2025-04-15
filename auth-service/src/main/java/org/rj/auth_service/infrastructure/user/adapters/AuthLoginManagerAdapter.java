package org.rj.auth_service.infrastructure.user.adapters;

import lombok.RequiredArgsConstructor;
import org.rj.auth_service.domain.user.dto.LoginUserRequest;
import org.rj.auth_service.domain.user.ports.in.AuthManagerPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthLoginManagerAdapter implements AuthManagerPort {
    private final AuthenticationManager authenticationManager;

    @Override
    public void authenticate(LoginUserRequest loginUserRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginUserRequest.email(), loginUserRequest.password()
            ));
        }catch (AuthenticationException e){
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
