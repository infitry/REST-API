package infitry.rest.api.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> template;

    public synchronized <T> T getValue(final String key, Class<T> clazz) {
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return objectMapper.registerModule(new JavaTimeModule()).convertValue(template.opsForValue().get(key), clazz);
    }

    public void setValue(final String key, final Object value, long timeout, TimeUnit timeUnit) {
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        template.opsForValue().set(key, value);
        template.expire(key, timeout, timeUnit);
    }

    public Boolean removeValue(String key) {
        return template.delete(key);
    }

    public Boolean removeValues(List<String> keys) {
        Long result = template.delete(keys);
        return result != null && result > 0;
    }
}

