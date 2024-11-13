package com.e106.reco.global.auth.service;

import com.e106.reco.domain.artist.crew.repository.CrewUserRepository;
import com.e106.reco.domain.artist.entity.Genre;
import com.e106.reco.domain.artist.entity.Position;
import com.e106.reco.domain.artist.entity.Region;
import com.e106.reco.domain.artist.user.dto.CustomUserDetails;
import com.e106.reco.domain.artist.user.entity.Gender;
import com.e106.reco.domain.artist.user.entity.User;
import com.e106.reco.domain.artist.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AuthService implements ReactiveUserDetailsService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final CrewUserRepository crewUserRepository;

    @Value("${spring.mail.username}")
    private String configEmail;
    @Value("${spring.image.profile.user}")
    private String configProfile;

    //TODO : 나중에 지우기 마스터 계정 생성용임
    @PostConstruct
    public void INIT(){
        if(userRepository.existsByEmail("aaaa@ssafy.com")) return;
        userRepository.save(User.builder()
                        .seq(1L)
                        .email("aaaa@ssafy.com")
                        .password(bCryptPasswordEncoder.encode("1111"))
                        .nickname("king")
                        .name("master")
                        .gender(Gender.ETC)
                        .birth(LocalDate.now())
                        .position(Position.BASE)
                        .genre(Genre.BALAD)
                        .region(Region.BS)
                        .content("Hi everyone")
                        .profileImage(configProfile)
                .build());
    }
    @Override
    public Mono<UserDetails> findByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
        List<Long> crews = crewUserRepository.findPk_CrewSeqByPk_userSeq(user.getSeq());
        return Mono.just(CustomUserDetails.builder()
                .seq(user.getSeq())
                .nickname(user.getNickname())
                .password(user.getPassword())
                .email(user.getEmail())
                .genre(user.getGenre())
                .year(String.valueOf(user.getBirth().getYear()))
                .position(user.getPosition())
                .region(user.getRegion())
                .gender(user.getGender())
                .crews(crews)
                .role(null)
                .build());

    }
}



