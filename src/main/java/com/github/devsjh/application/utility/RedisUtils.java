package com.github.devsjh.application.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.Duration;

/**
 * @memo: Redis 유틸리티 클래스.
 * (Redis는 Key:Value 구조를 갖는다.)
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisUtils {

    private final StringRedisTemplate stringRedisTemplate;

    // 데이터 추출. (클래스 타입을 지정할 때)
    // (https://mongsil-jeong.tistory.com/25)
    public <T> T getData(String key, Class<T> clazz) throws Exception {
        String jsonResult = stringRedisTemplate.opsForValue().get(key);

        if (StringUtils.isEmpty(jsonResult)) {
            return null;

        } else {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonResult, clazz);
        }
    }

    // 데이터 추출.
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();

        return valueOperations.get(key);
    }

    // 데이터 저장.
    public void setData(String key, String value) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    // 데이터 저장. (유효 시간 지정)
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(duration);

        long start = System.currentTimeMillis();
        valueOperations.set(key, value, expireDuration);
        long end = System.currentTimeMillis();
        log.info("set() : " + (end - start) / 1000.0 + "초"); // 첫 번째 로그인은 느리지만, 두 번째 로그인부터 빠르다.
    }

    // 데이터 삭제.
    public void deleteData(String key) {
        try {
            if (stringRedisTemplate.opsForValue().get(key) != null) {
                // 리프레시 토큰을 삭제한다.
                stringRedisTemplate.delete(key);
            }
        } catch (IllegalArgumentException e) {
            log.error("[ERROR] Key does not exist.");
        }
    }
}