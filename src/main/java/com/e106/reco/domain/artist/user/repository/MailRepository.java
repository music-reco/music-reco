package com.e106.reco.domain.artist.user.repository;

import com.e106.reco.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

import static com.e106.reco.global.error.errorcode.AuthErrorCode.BAD_USER;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MailRepository {
    private final String PREFIX_EMAIL = "email:"; // key값이 중복되지 않도록 상수 선언
    private final String PREFIX_EMAIL_COUNT = "count:"; // key값이 중복되지 않도록 상수 선언
    private final int LIMIT_TIME = 5*60; // 인증번호 유효 시간

    private final StringRedisTemplate stringRedisTemplate;

    // 발급 후 Redis 저장
    public void createEmailCode(String email, String code) {
        int count;

        if (!stringRedisTemplate.hasKey(PREFIX_EMAIL + email)){
            stringRedisTemplate.opsForValue()
                    .set(PREFIX_EMAIL_COUNT + email, "1", Duration.ofSeconds(LIMIT_TIME));
        }else{
            count = Integer.parseInt(stringRedisTemplate.opsForValue().get(PREFIX_EMAIL_COUNT + email));

            if (count>4) {
                stringRedisTemplate.opsForValue()
                        .set(PREFIX_EMAIL_COUNT + email, String.valueOf(count), Duration.ofSeconds(LIMIT_TIME*12*5));
                throw new BusinessException(BAD_USER);
            }else{
                stringRedisTemplate.opsForValue()
                        .set(PREFIX_EMAIL_COUNT + email, String.valueOf(count+1), Duration.ofSeconds(LIMIT_TIME));
            }
        }
        stringRedisTemplate.opsForValue()
                .set(PREFIX_EMAIL + email, code, Duration.ofSeconds(LIMIT_TIME));
    }
    // id에 해당하는 token 불러오기
    public String getEmailCode(String email) {
        return stringRedisTemplate.opsForValue().get(PREFIX_EMAIL + email);
    }
    // id에 해당하는 token 불러오기
    public boolean isEmailCodeValid(String email, String code) {
        return stringRedisTemplate.hasKey(PREFIX_EMAIL + email) &&
                getEmailCode(email).equals(code);
    }

    public void createEmailSuccess(String email) {
        stringRedisTemplate.opsForValue()
                .set(PREFIX_EMAIL_COUNT + email, "success", Duration.ofSeconds(LIMIT_TIME*5));
    }
    public boolean isEmailValid(String email) {
        return stringRedisTemplate.hasKey(PREFIX_EMAIL_COUNT + email) &&
                stringRedisTemplate.opsForValue().get(PREFIX_EMAIL_COUNT + email).equals("success");
    }
}
