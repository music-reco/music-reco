package com.e106.reco.domain.artist.crew.service;

import com.e106.reco.domain.artist.crew.dto.CreateDto;
import com.e106.reco.domain.artist.crew.dto.CrewChangeDto;
import com.e106.reco.domain.artist.crew.dto.CrewDto;
import com.e106.reco.domain.artist.crew.dto.CrewGrantDto;
import com.e106.reco.domain.artist.crew.dto.CrewInfoDto;
import com.e106.reco.domain.artist.crew.dto.CrewRoleDto;
import com.e106.reco.domain.artist.crew.entity.Crew;
import com.e106.reco.domain.artist.crew.entity.CrewUser;
import com.e106.reco.domain.artist.crew.entity.CrewUserState;
import com.e106.reco.domain.artist.crew.repository.CrewRepository;
import com.e106.reco.domain.artist.crew.repository.CrewUserRepository;
import com.e106.reco.domain.artist.user.dto.CustomUserDetails;
import com.e106.reco.domain.artist.user.dto.UserSummaryDto;
import com.e106.reco.domain.artist.user.entity.User;
import com.e106.reco.domain.artist.user.repository.UserRepository;
import com.e106.reco.global.common.CommonResponse;
import com.e106.reco.global.error.exception.BusinessException;
import com.e106.reco.global.s3.S3FileService;
import com.e106.reco.global.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
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

    private final S3FileService s3FileService;

    @Value("${spring.image.profile.crew}")
    private String configProfile;

    public List<CrewRoleDto> roleCrew(Long crewSeq) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();
        if(!crewRepository.existsBySeqAndManagerSeq(crewSeq, userDetails.getSeq()))
            throw new BusinessException(USER_NOT_MASTER);

        return getCrewRoles(crewSeq);
    }

    public List<CrewRoleDto> grantCrew(Long crewSeq, CrewGrantDto crewGrantDto) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();

        Map<Long, CrewGrantDto.UserCrewDto> crews
                = crewGrantDto.getCrews().stream().collect(Collectors.toMap(CrewGrantDto.UserCrewDto::getUserSeq, userCrewDto -> userCrewDto));

        if(crewGrantDto.getCrews().stream()
                .filter(crew -> crew.getState() == CrewUserState.ALL || crew.getState() == CrewUserState.CHAT)
                .count()>1)
            throw new BusinessException(GRANT_CHAT_ONLY_ONE);

        if(crewGrantDto.getCrews().stream().anyMatch(crew -> crew.getState() == CrewUserState.WAITING))
            throw new BusinessException(USER_ALREADY_JOIN);

        if(!crewRepository.existsBySeqAndManagerSeq(crewSeq, userDetails.getSeq()))
            throw new BusinessException(USER_NOT_MASTER);

        List<CrewUser> crewUsers = crewUserRepository.findCrewUsersByCrewSeqWithoutMaster(crewSeq, userDetails.getSeq());

        if(crewUsers.size() != crews.size()) throw new BusinessException(CREW_USER_NOT_FOUND);

        for(CrewUser crewUser : crewUsers){
            if(!crews.containsKey(crewUser.getPk().getUserSeq())) throw new BusinessException(CREW_USER_NOT_FOUND);
            crewUser.modifyState(crews.get(crewUser.getPk().getUserSeq()).getState());
        }

        return getCrewRoles(crewSeq);
    }
    public List<CrewRoleDto> getCrewRoles(Long crewSeq) {
        Crew crew = crewRepository.findBySeq(crewSeq)
                .orElseThrow(() -> new BusinessException(CREW_NOT_FOUND));

        List<CrewUser> crewUsers = crewUserRepository.findCrewUsersByCrewSeqWithoutMaster(crewSeq, crew.getManager().getSeq());

        return crewUsers.stream().map(crewUser -> CrewRoleDto.builder()
                        .user(UserSummaryDto.of(crewUser.getUser()))
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
                                .userSeq(declineDto.getUserSeq())
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

        // TODO : 크루 마스터가 떠나는 경우 크루 권한 양도 필수
        if(crew.getManager().getSeq() == userDetails.getSeq())
            throw new BusinessException(USER_IS_MASTER);

        CrewUser crewUser = crewUserRepository.findById(new CrewUser.PK(leaveDto.getCrewSeq(), userDetails.getSeq()))
                .orElseThrow(() -> new BusinessException(CREW_USER_NOT_FOUND));

        crewUserRepository.delete(crewUser);
        return new CommonResponse("크루 탈퇴가 완료되었습니다.");
    }
    public CommonResponse updateCrew (Long crewSeq, CreateDto crewDto, MultipartFile file) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();
        User user = userRepository.findBySeq(userDetails.getSeq())
                .orElseThrow(()->new BusinessException(USER_NOT_FOUND));
        Crew crew = crewRepository.findBySeqAndManagerSeq(crewSeq, user.getSeq())
                .orElseThrow(()->new BusinessException(CREW_NOT_FOUND));

        crewDto.setProfileImage(file==null ? configProfile : s3FileService.uploadFile(file));
        Crew.of(crew, crewDto);
        crewRepository.save(crew);
        return new CommonResponse("크루 업데이트 완료");
    }
    public CommonResponse createCrew (CreateDto creatDto, MultipartFile file) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();
        User user = userRepository.findBySeq(userDetails.getSeq())
                .orElseThrow(()->new BusinessException(USER_NOT_FOUND));

        String imageUrl = file==null ? configProfile : s3FileService.uploadFile(file);

        Crew crew = Crew.of(creatDto);
        crew.modifyProfileImage(imageUrl);
        crew.modifyManagerSeq(user);

        CrewUser crewUser = CrewUser.of(user, crewRepository.save(crew));
        crewUser = crewUserRepository.save(crewUser);
        crewUser.modifyState(CrewUserState.ALL);

        return new CommonResponse("크루 생성 완료");
    }

    public CrewInfoDto infoCrew(Long crewSeq) {
        Crew crew = crewRepository.findBySeq(crewSeq).orElseThrow(()->new BusinessException(CREW_NOT_FOUND));
        List<CrewUser> crewUsers = crewUserRepository.findCrewUsersByCrewSeqWithoutMaster(crewSeq, crew.getManager().getSeq());

        return CrewInfoDto.of(crew, crewUsers);
    }

    public List<UserSummaryDto> waitngCrew(Long crewSeq) {
        CustomUserDetails userDetails = AuthUtil.getCustomUserDetails();
        Crew crew = crewRepository.findBySeqAndManagerSeq(crewSeq, userDetails.getSeq()).orElseThrow(()->new BusinessException(USER_NOT_MASTER));
        List<CrewUser> crewUsers = crewUserRepository.findWatingCrewUsersByCrewSeq(crewSeq);

        return crewUsers.stream().map(crewUser -> UserSummaryDto.of(crewUser.getUser())).toList();
    }
}