package org.rj.user_service.profile.infrastructure.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.rj.user_service.profile.domain.model.UserProfile;
import org.rj.user_service.profile.domain.model.UserProfileId;

@Entity
@Table(name = "users",schema = "profiles")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String firstname;
    private String surname;

    public UserProfile toAggregate(){
        return UserProfile.builder()
                .id(new UserProfileId(id))
                .email(email)
                .firstname(firstname)
                .surname(surname)
                .build();
    }

    public static UserProfileEntity toEntity(UserProfile userProfile){
        return UserProfileEntity.builder()
                .id(userProfile.getId() != null ? userProfile.getId().id() : null)
                .email(userProfile.getEmail())
                .firstname(userProfile.getFirstname())
                .surname(userProfile.getSurname())
                .build();
    }
}
