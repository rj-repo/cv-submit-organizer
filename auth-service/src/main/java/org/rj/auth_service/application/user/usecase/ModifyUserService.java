package org.rj.auth_service.application.user.usecase;

import lombok.RequiredArgsConstructor;
import org.rj.auth_service.domain.user.dto.UserModifyRequest;
import org.rj.auth_service.domain.user.model.AuthUserId;
import org.rj.auth_service.domain.user.ports.in.ModifyUserUserCase;
import org.rj.auth_service.domain.user.ports.out.UserAuthRepoPort;
import org.rj.cvsubmitorganizer.common.UseCaseService;

@UseCaseService
@RequiredArgsConstructor
public class ModifyUserService implements ModifyUserUserCase {

    private UserAuthRepoPort userAuthRepoPort;
    @Override
    public AuthUserId modify(UserModifyRequest userModifyRequest) {
        return null;
    }
}
