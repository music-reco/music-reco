package com.e106.reco.global.auth.token.service;

import com.e106.reco.global.auth.dto.UserDto;
import com.e106.reco.global.auth.jwt.JwtUtil;
import com.e106.reco.global.auth.token.repository.TokenRepository;
import com.e106.reco.global.error.exception.BusinessException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.e106.reco.global.error.errorcode.AuthErrorCode.TOKEN_EXPIRED;
import static com.e106.reco.global.error.errorcode.AuthErrorCode.TOKEN_INVALID;
import static com.e106.reco.global.error.errorcode.AuthErrorCode.TOKEN_NOT_EXIST;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class TokenService {
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public void createRefreshToken(Long id, String refreshToken) {
        tokenRepository.createRefreshToken(id, refreshToken);
    }

    public UserDto getUserDto(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String token;
        if (authorization == null || !authorization.startsWith("Bearer ")) throw new BusinessException(TOKEN_NOT_EXIST);
        else token = authorization.split(" ")[1];
        if(jwtUtil.isExpired(token)){
            throw new BusinessException(TOKEN_EXPIRED, "토큰이 만료되었습니다");
        }else if(!jwtUtil.getCategory(token).equals("access")){
            throw new BusinessException(TOKEN_INVALID, "유효하지 않은 토큰입니다");
        }

        return UserDto.builder()
                .seq(jwtUtil.getSeq(token))
                .nickname(jwtUtil.getNickname(token))
                .build();
    }
    // access 만료 후 refresh 통한 access 요청시 refresh 함께 새로 내림
    public void reissueProcess(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = findRefreshToken(request);
        String accessToken = request.getHeader("Authorization");

        if (accessToken == null || !accessToken.startsWith("Bearer ")|| refreshToken == null) {
            //response state code
            throw new BusinessException(TOKEN_NOT_EXIST, "요청에 토큰이 입력되지 않았습니다");
        }else if(jwtUtil.isExpired(refreshToken)){
            throw new BusinessException(TOKEN_EXPIRED, "토큰이 만료되었습니다");
        }else if(!jwtUtil.getCategory(refreshToken).equals("refresh")){
            log.info("category : {}", jwtUtil.getCategory(refreshToken));
            throw new BusinessException(TOKEN_INVALID, "유효하지 않은 토큰입니다");
        }else if(!tokenRepository.isRefreshTokenVaild(jwtUtil.getSeq(accessToken.split(" ")[1]), refreshToken)){
            log.info("access token id : {}", jwtUtil.getSeq(accessToken.split(" ")[1]));
            log.info("refresh token id : {}", refreshToken);
            throw new BusinessException(TOKEN_INVALID, "유효하지 않은 토큰입니다");
        }
        String email = jwtUtil.getEmail(refreshToken);
        String nickname = jwtUtil.getNickname(refreshToken);
        Long userId = jwtUtil.getSeq(refreshToken);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", nickname, userId, email, 86400000L);
        String newRefresh = jwtUtil.createJwt("refresh", nickname, userId, email, 864000000L);

        //response
        tokenRepository.createRefreshToken(userId, newRefresh);
        response.addHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

    }
    private String findRefreshToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                return cookie.getValue();
            }
        }
        return null;
    }
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
