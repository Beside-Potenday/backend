package alphamail.com.backend.google.model;

import alphamail.com.backend.user.entity.MemberEntity;

public record GoogleUserInfoResponse(String email, String name, String picture) {

    public MemberEntity toMemberEntity() {
        return new MemberEntity(
            name,
            email,
            picture
        );
    }
}
