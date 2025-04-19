package org.rj.auth_service.domain.user.ports.in;

import org.rj.auth_service.domain.user.dto.UserModifyRequest;
import org.rj.auth_service.domain.user.model.AuthUserId;

public interface ModifyUserUserCase {

    AuthUserId modify(UserModifyRequest userModifyRequest);
}
