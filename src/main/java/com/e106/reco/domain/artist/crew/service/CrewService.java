package com.e106.reco.domain.artist.crew.service;

import com.e106.reco.domain.artist.crew.dto.CreateDto;
import com.e106.reco.domain.artist.crew.entity.Crew;
import com.e106.reco.domain.artist.crew.repository.CrewRepository;
import com.e106.reco.domain.artist.user.dto.CustomUserDetails;
import com.e106.reco.domain.artist.user.entity.User;
import com.e106.reco.domain.artist.user.repository.MailRepository;
import com.e106.reco.domain.artist.user.repository.UserRepository;
import com.e106.reco.global.auth.dto.JoinDto;
import com.e106.reco.global.common.CommonResponse;
import com.e106.reco.global.error.exception.BusinessException;
import com.e106.reco.global.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.e106.reco.global.error.errorcode.AuthErrorCode.USER_NOT_FOUND;


@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class CrewService {
    private final CrewRepository crewRepository;
    private final UserRepository userRepository;

    public CommonResponse createCrew (CreateDto creatDto) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();
        User user = userRepository.findBySeq(userDetails.getSeq())
                .orElseThrow(()->new BusinessException(USER_NOT_FOUND));

        Crew crew = Crew.of(creatDto);
        crew.modifyManagerSeq(user);


        crewRepository.save(crew);
        return new CommonResponse("크루 생성 완료");
    }
}