package alphamail.com.backend.user.service;

import alphamail.com.backend.google.model.GoogleLoginResponse;
import alphamail.com.backend.google.model.GoogleUserInfoResponse;
import alphamail.com.backend.user.entity.MemberEntity;
import alphamail.com.backend.user.entity.TokenEntity;
import alphamail.com.backend.user.repository.MemberRepository;
import alphamail.com.backend.user.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    public MemberService(MemberRepository memberRepository, TokenRepository tokenRepository) {
        this.memberRepository = memberRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void createMember(GoogleLoginResponse googleLoginResponse,
        GoogleUserInfoResponse googleUserInfoResponse) {
        if (!memberRepository.existsByEmail(googleUserInfoResponse.email())) {
            MemberEntity member = googleUserInfoResponse.toMemberEntity();
            memberRepository.save(member);
        }
        MemberEntity member = memberRepository.findByEmail(googleUserInfoResponse.email());
        TokenEntity token = new TokenEntity(member, googleLoginResponse.getRefreshToken(),
            googleLoginResponse.getAccessToken(), googleLoginResponse.getExpiresIn());
        tokenRepository.save(token);
    }
}
