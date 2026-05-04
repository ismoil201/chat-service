package dev.ismoil.chat_service.service;

import dev.ismoil.chat_service.entities.Member;
import dev.ismoil.chat_service.enums.Gender;
import dev.ismoil.chat_service.repositories.MemberRepository;
import dev.ismoil.chat_service.vos.CustomOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    final MemberRepository memberRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);


        Map<String, Object> attributeMap = oAuth2User.getAttribute("kakao_account");

        String email = (String) attributeMap.get("email");
        Member member = memberRepository.findByEmail(email).orElseGet(()-> registerMember( attributeMap));


        return new  CustomOAuth2User(member, oAuth2User.getAttributes());

    }

    private Member registerMember(Map<String, Object> attributeMap){

        Member member = Member.builder()
                .email((String) attributeMap.get("email"))
                .nickname((String)((Map) attributeMap.get("profile")).get("nickname"))
                .name((String) attributeMap.get("name"))
                .phoneNumber((String) attributeMap.get("phone_number"))
                .gender(Gender.valueOf(((String) attributeMap.get("gender")).toUpperCase()))
                .birthDay(parseBirthDay(attributeMap))
                .role("USER_ROLE")
                .build();

        return memberRepository.save(member);

    }

    private LocalDate parseBirthDay(Map<String, Object> attributeMap){
        String birthday = (String) attributeMap.get("birthday");
        String birthYear = (String) attributeMap.get("birthyear");
        if (birthday == null || birthYear == null) {
            return null;
        }

        return LocalDate.parse(birthYear + birthday , DateTimeFormatter.BASIC_ISO_DATE);
    }
}
