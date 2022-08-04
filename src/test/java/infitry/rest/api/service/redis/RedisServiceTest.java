package infitry.rest.api.service.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional(readOnly = true)
class RedisServiceTest {
    @Autowired
    RedisService redisService;

    @BeforeEach
    void 레디스_저장() {
        //given
        redisService.setValue("test", "testValue", 1L, TimeUnit.MINUTES);
        redisService.setValue("test1", "testValue1", 1L, TimeUnit.MINUTES);
    }

    @Test
    @Transactional
    public void 레디스_가져오기() {
        //when
        String testValue = redisService.getValue("test", String.class);
        //then
        assertEquals("testValue", testValue);
    }

    @Test
    @Transactional
    public void 레디스_제거() {
        //when
        redisService.removeValue("test");
        String testValue = redisService.getValue("test", String.class);
        //then
        assertNull(testValue);
    }

    @Test
    public void 레디스_여러값제거() {
        //when
        redisService.removeValues(Arrays.asList("test", "test1"));
        String testValue = redisService.getValue("test", String.class);
        String test1Value = redisService.getValue("test1", String.class);
        //then
        assertNull(testValue);
        assertNull(test1Value);
    }
}