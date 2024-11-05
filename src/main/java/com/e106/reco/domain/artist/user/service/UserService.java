package com.e106.reco.domain.artist.user.service;

import com.e106.reco.domain.artist.crew.repository.CrewUserRepository;
import com.e106.reco.domain.artist.user.dto.UserInfoDto;
import com.e106.reco.domain.artist.user.entity.User;
import com.e106.reco.domain.artist.user.repository.UserRepository;
import com.e106.reco.global.auth.dto.JoinDto;
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

import static com.e106.reco.global.error.errorcode.AuthErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserService {
    private final CrewUserRepository crewUserRepository;
    private final UserRepository userRepository;
    private final S3FileService s3FileService;

    @Value("${spring.image.profile.user}")
    private String configProfile;

    public UserInfoDto getInfo() {
        User user = userRepository.findBySeq(AuthUtil.getCustomUserDetails().getSeq())
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        List<Long> crews = crewUserRepository.findPk_CrewSeqByPk_userSeq(user.getSeq());
        log.info(user.getNickname());
        return UserInfoDto.of(user,crews);
    }

    public CommonResponse updateUserInfo(JoinDto joinDto, MultipartFile file) {
        User user = userRepository.findBySeq(AuthUtil.getCustomUserDetails().getSeq())
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        joinDto.setProfileImage(file == null ? configProfile : s3FileService.uploadFile(file));

        User.of(user, joinDto);
        userRepository.save(user);

        return new CommonResponse("회원 정보 수정 완료");
    }
}



