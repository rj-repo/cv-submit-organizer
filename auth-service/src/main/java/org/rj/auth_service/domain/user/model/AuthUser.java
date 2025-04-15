package org.rj.auth_service.domain.user.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

@Builder
@Getter
@AllArgsConstructor
public class AuthUser  {
    private AuthUserId id;
    private String email;
    @Setter
    private String password;
    private boolean enabled;


    public void enableAccount() {
        this.enabled = true;
    }

    public boolean isAccountEnabled() {
        return enabled;
    }


    public void validateEmail() {
        final String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        boolean matches = Pattern.compile(emailRegex).matcher(email).matches();
        if (!matches) {
            throw new AuthUserDomainException("Wrong email");
        }
    }


    public void validatePassword() {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
        boolean matches = Pattern.compile(passwordRegex).matcher(password).matches();
        if (!matches) {
            throw new AuthUserDomainException("Wrong password");
        }
    }

    public void checkIfUserEnabled(){
        if(!enabled){
            throw new AuthUserDomainException("User is not enabled");
        }
    }


}
