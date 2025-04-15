package org.rj.auth_service.application.user.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rj.auth_service.application.user.exception.AuthUserNotFoundException;
import org.rj.auth_service.domain.token.Token;
import org.rj.auth_service.domain.user.dto.LoginResponse;
import org.rj.auth_service.domain.user.dto.LoginUserRequest;
import org.rj.auth_service.domain.user.model.AuthUser;
import org.rj.auth_service.domain.user.ports.in.AuthManagerPort;
import org.rj.auth_service.domain.user.ports.out.UserAuthRepoPort;
import org.rj.auth_service.domain.verification.ports.out.AuthTokenProviderPort;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginUserServiceTest {

    @Mock
    private AuthManagerPort authManagerPort;

    @Mock
    private AuthTokenProviderPort tokenService;

    @Mock
    private UserAuthRepoPort userRepository;

    @InjectMocks
    private LoginUserService loginService;

    @Test
    void loginSuccesfully() {
        // given
        String email = "test@example.com";
        String tokenValue = "mocked-jwt-token";
        String username = "testuser";

        LoginUserRequest request = new LoginUserRequest(email, "password123");

        AuthUser authUser = AuthUser.builder()
                .email(email)
                .enabled(true).build();
        Token token = new Token(tokenValue, username);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(authUser));
        when(tokenService.createToken(anyString())).thenReturn(token);

        // when
        LoginResponse response = loginService.login(request);

        // then
        assertEquals(tokenValue, response.token());
        assertEquals(username, response.username());

        verify(authManagerPort).authenticate(request);
        verify(tokenService).createToken(email);
    }

    @Test
    void throwsExceptionWhenUserNotFound() {
        // given
        String email = "missing@example.com";
        LoginUserRequest request = new LoginUserRequest(email, "password123");

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThrows(AuthUserNotFoundException.class, () -> loginService.login(request));

        verify(authManagerPort, never()).authenticate(any());
        verify(tokenService, never()).createToken(any());
    }
}
