package org.rj.auth_service.application.user.usecase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.rj.auth_service.application.user.exception.AuthUserAlreadyExistsException;
import org.rj.auth_service.domain.user.AuthDomainService;
import org.rj.auth_service.domain.user.dto.RegisterUserRequest;
import org.rj.auth_service.domain.user.model.AuthUser;
import org.rj.auth_service.domain.user.ports.in.RegisterUserUseCase;
import org.rj.auth_service.domain.user.ports.out.PasswordEncoderPort;
import org.rj.auth_service.domain.user.ports.out.UserAuthRepoPort;
import org.rj.auth_service.domain.verification.ports.out.SendVerificationTokenPort;
import org.rj.cvsubmitorganizer.common.UseCaseService;

@RequiredArgsConstructor
@UseCaseService
public class RegisterUserService implements RegisterUserUseCase {

    private final AuthDomainService authDomainService;
    private final PasswordEncoderPort passwordEncoder;
    private final SendVerificationTokenPort verificationTokenSenderPort;
    private final UserAuthRepoPort userRepository;

    @Override
    @Transactional
    public void signup(RegisterUserRequest input) {
        userRepository.findByEmail(input.email()).ifPresent(user -> {
            throw new AuthUserAlreadyExistsException("User " + user.getEmail() + " already exists");
        });
        AuthUser user = authDomainService.createUser(input);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AuthUser savedUser = userRepository.save(user);
        verificationTokenSenderPort.sendToken(savedUser.getId(), savedUser.getEmail());
    }
}
