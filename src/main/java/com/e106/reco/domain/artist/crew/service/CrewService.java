package com.e106.reco.domain.artist.crew.service;

import com.e106.reco.domain.artist.crew.dto.CreateDto;
import com.e106.reco.domain.artist.crew.dto.CrewChangeDto;
import com.e106.reco.domain.artist.crew.dto.CrewDto;
import com.e106.reco.domain.artist.crew.dto.CrewGrantDto;
import com.e106.reco.domain.artist.crew.dto.CrewRoleDto;
import com.e106.reco.domain.artist.crew.entity.Crew;
import com.e106.reco.domain.artist.crew.entity.CrewUser;
import com.e106.reco.domain.artist.crew.entity.CrewUserState;
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

import java.util.List;
import java.util.stream.Collectors;

import static com.e106.reco.global.error.errorcode.AuthErrorCode.USER_NOT_FOUND;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.CREW_ALREADY_FULL;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.CREW_NOT_FOUND;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.CREW_USER_NOT_FOUND;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.GRANT_CHAT_ONLY_ONE;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.USER_ALREADY_JOIN;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.USER_IS_MASTER;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.USER_NOT_MASTER;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.USER_NOT_WAIT;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class CrewService {
    private final CrewRepository crewRepository;
    private final UserRepository userRepository;
    private final CrewUserRepository crewUserRepository;

    public List<CrewRoleDto> roleCrew(Long crewSeq) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();
        if(!crewRepository.existsBySeqAndManagerSeq(crewSeq, userDetails.getSeq()))
            throw new BusinessException(USER_NOT_MASTER);

        return getCrewRoles(crewSeq);
    }


    public List<CrewRoleDto> grantCrew(CrewGrantDto crewGrantDto) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();

        if(!crewRepository.existsBySeqAndManagerSeq(crewGrantDto.getCrewSeq(), userDetails.getSeq()))
            throw new BusinessException(USER_NOT_MASTER);

        if(crewGrantDto.getCrews().stream()
                .filter(crew -> crew.getState()== CrewUserState.ALL || crew.getState() == CrewUserState.CHAT)
                .count()>1)
            throw new BusinessException(GRANT_CHAT_ONLY_ONE);

        if(crewGrantDto.getCrews().stream()
                .anyMatch(crew -> crew.getState() == CrewUserState.WAITING))
            throw new BusinessException(USER_ALREADY_JOIN);

        if(crewUserRepository.countCrewUsers(crewGrantDto.getCrewSeq(),
                    crewGrantDto.getCrews().stream()
                            .map(CrewRoleDto::getUserSeq)
                            .collect(Collectors.toList()))
                != crewGrantDto.getCrews().size())
            throw new BusinessException(CREW_USER_NOT_FOUND);

        List<CrewUser> crewUsers = crewGrantDto.getCrews().stream()
                        .map(crewRoleDto -> CrewUser.builder()
                                .pk(CrewUser.PK.builder()
                                        .crewSeq(crewGrantDto.getCrewSeq())
                                        .userSeq(crewRoleDto.getUserSeq())
                                        .build())
                                .crew(Crew.builder()
                                        .seq(crewGrantDto.getCrewSeq())
                                        .build())
                                .user(User.builder()
                                        .seq(crewRoleDto.getUserSeq())
                                        .build())
                                .state(crewRoleDto.getState())
                                .build()
                        ).toList();


        crewUserRepository.deleteAllByCrewWithoutMaster(crewGrantDto.getCrewSeq(), userDetails.getSeq());
        crewUserRepository.saveAll(crewUsers);

        return getCrewRoles(crewGrantDto.getCrewSeq());
    }
    public List<CrewRoleDto> getCrewRoles(Long crewSeq) {
        Crew crew = crewRepository.findBySeq(crewSeq)
                .orElseThrow(() -> new BusinessException(CREW_NOT_FOUND));

        List<CrewUser> crewUsers = crewUserRepository.findCrewUsersByCrewSeqWithoutMaster(crewSeq, crew.getManager().getSeq());

        return crewUsers.stream().map(crewUser -> CrewRoleDto.builder()
                        .userSeq(crewUser.getPk().getUserSeq())
                        .state(crewUser.getState())
                        .build())
                .collect(Collectors.toList());
    }
    public CommonResponse acceptCrew(CrewChangeDto acceptDto) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();

        if(!crewRepository.existsBySeqAndManagerSeq(acceptDto.getCrewSeq(), userDetails.getSeq()))
            throw new BusinessException(USER_NOT_MASTER);
        if(crewUserRepository.isPossibleCrewUserAccept(acceptDto.getCrewSeq())>=20)
            throw new BusinessException(CREW_ALREADY_FULL);

        CrewUser crewUser = crewUserRepository.findByPkAndState(
                    CrewUser.PK.builder()
                            .crewSeq(acceptDto.getCrewSeq())
                            .userSeq(acceptDto.getUserSeq())
                    .build(), CrewUserState.WAITING)
                .orElseThrow(() -> new BusinessException(USER_NOT_WAIT));

        crewUser.acceptCrew(crewUser.getState());

        return new CommonResponse("회원 승인 완료");
    }
    public CommonResponse declineCrew(CrewChangeDto declineDto) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();

        if(!crewRepository.existsBySeqAndManagerSeq(declineDto.getCrewSeq(), userDetails.getSeq()))
            throw new BusinessException(USER_NOT_MASTER);

        CrewUser crewUser = crewUserRepository.findById(
                        CrewUser.PK.builder()
                                .crewSeq(declineDto.getCrewSeq())
                                .userSeq(userDetails.getSeq())
                                .build())
                .filter(crewuser -> crewuser.getState() != CrewUserState.WAITING)
                .orElseThrow(() -> new BusinessException(CREW_USER_NOT_FOUND));

        crewUserRepository.delete(crewUser);

        return new CommonResponse("회원 거부 완료");
    }
    public CommonResponse joinCrew(CrewDto joinDto) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();

        if(!crewRepository.existsBySeq(joinDto.getCrewSeq()))
            throw new BusinessException(CREW_NOT_FOUND);
        if(crewUserRepository.existsById(new CrewUser.PK(joinDto.getCrewSeq(), userDetails.getSeq())))
            throw new BusinessException(USER_ALREADY_JOIN);

        crewUserRepository.save(CrewUser.builder()
                .pk(CrewUser.PK.builder()
                        .crewSeq(joinDto.getCrewSeq())
                        .userSeq(userDetails.getSeq())
                        .build())
                .crew(Crew.builder()
                        .seq(joinDto.getCrewSeq())
                        .build())
                .user(User.builder()
                        .seq(userDetails.getSeq())
                        .build())
                .build());
        return new CommonResponse("가입 신청이 완료되었습니다.");
    }
    public CommonResponse leaveCrew(CrewDto leaveDto) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();

        Crew crew = crewRepository.findBySeq(leaveDto.getCrewSeq())
                .orElseThrow(() -> new BusinessException(CREW_NOT_FOUND));
        if(crew.getManager().getSeq() == userDetails.getSeq())
            throw new BusinessException(USER_IS_MASTER);

        CrewUser crewUser = crewUserRepository.findById(new CrewUser.PK(leaveDto.getCrewSeq(), userDetails.getSeq()))
                .orElseThrow(() -> new BusinessException(CREW_USER_NOT_FOUND));

        crewUserRepository.delete(crewUser);
        return new CommonResponse("크루 탈퇴가 완료되었습니다.");
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