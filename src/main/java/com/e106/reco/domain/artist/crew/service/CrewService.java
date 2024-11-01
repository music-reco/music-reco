package com.e106.reco.domain.artist.crew.service;

import com.e106.reco.domain.artist.crew.dto.CreateDto;
import com.e106.reco.domain.artist.crew.dto.CrewAcceptDto;
import com.e106.reco.domain.artist.crew.dto.CrewJoinDto;
import com.e106.reco.domain.artist.crew.entity.Crew;
import com.e106.reco.domain.artist.crew.entity.CrewUser;
import com.e106.reco.domain.artist.crew.repository.CrewRepository;
import com.e106.reco.domain.artist.crew.repository.CrewUserRepository;
import com.e106.reco.domain.artist.user.dto.CustomUserDetails;
import com.e106.reco.domain.artist.user.entity.User;
import com.e106.reco.domain.artist.user.repository.UserRepository;
import com.e106.reco.global.common.CommonResponse;
import com.e106.reco.global.error.exception.BusinessException;
import com.e106.reco.global.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.e106.reco.global.error.errorcode.AuthErrorCode.USER_NOT_FOUND;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.CREW_ALREADY_FULL;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.CREW_NOT_FOUND;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.USER_ALREADY_JOIN;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.USER_NOT_WAIT;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class CrewService {
    private final CrewRepository crewRepository;
    private final UserRepository userRepository;
    private final CrewUserRepository crewUserRepository;

    public CommonResponse acceptCrew(CrewAcceptDto acceptDto) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();
        User user = userRepository.findBySeq(userDetails.getSeq()).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        Crew crew = crewRepository.findBySeqAndUser(acceptDto.getCrewSeq(), user)
                .orElseThrow(() -> new BusinessException(CREW_NOT_FOUND));

        if(crewUserRepository.isPossibleCrewUserAccept(crew)>=20)
            throw new BusinessException(CREW_ALREADY_FULL);

        CrewUser crewUser = crewUserRepository.findById(new CrewUser.PK(acceptDto.getCrewSeq(), acceptDto.getCrewSeq()))
                .orElseThrow(() -> new BusinessException(USER_NOT_WAIT));

        crewUser.acceptCrew(crewUser.getState());

        return new CommonResponse("회원 승인 완료");
    }
    public CommonResponse joinCrew(CrewJoinDto joinDto) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();
        User user = userRepository.findBySeq(userDetails.getSeq()).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        Crew crew = crewRepository.findBySeq(joinDto.getCrewSeq()).orElseThrow(() -> new BusinessException(CREW_NOT_FOUND));

        if(crewUserRepository.existsById(new CrewUser.PK(joinDto.getCrewSeq(), userDetails.getSeq())))
            throw new BusinessException(USER_ALREADY_JOIN);

        crewUserRepository.save(CrewUser.of(user, crew));
        return new CommonResponse("가입 신청이 완료되었습니다.");
    }

    public CommonResponse createCrew (CreateDto creatDto) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();
        User user = userRepository.findBySeq(userDetails.getSeq())
                .orElseThrow(()->new BusinessException(USER_NOT_FOUND));

        Crew crew = Crew.of(creatDto);
        crew.modifyManagerSeq(user);

        CrewUser crewUser = CrewUser.of(user, crewRepository.save(crew));
        crewUser = crewUserRepository.save(crewUser);
        crewUser.grantBoard(crewUser.getState());
        crewUser.grantChat(crewUser.getState());

        return new CommonResponse("크루 생성 완료");
    }
}