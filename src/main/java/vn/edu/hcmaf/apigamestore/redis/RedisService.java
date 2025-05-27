package vn.edu.hcmaf.apigamestore.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;


    public boolean lockProduct(Long productId, Long userId) {
        String key = "lock:product:" + productId;
        return Boolean.TRUE.equals(redisTemplate.opsForValue()
                .setIfAbsent(key, userId.toString(), Duration.ofMinutes(5)));
    }

    public boolean isProductLocked(Long productId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("lock:product:" + productId));
    }

    public void unlockProduct(Long productId) {
        redisTemplate.delete("lock:product:" + productId);
    }

    public void clearAllProductLocks() {
        Set<String> keys = redisTemplate.keys("lock:product:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
