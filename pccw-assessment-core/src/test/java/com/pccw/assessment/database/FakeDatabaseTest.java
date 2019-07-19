package com.pccw.assessment.database;

import com.pccw.assessment.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FakeDatabaseTest {

    @InjectMocks
    private FakeDatabase fakeDatabase;

    @Mock
    private RedisTemplate redisTemplate;

    @Mock
    private ValueOperations valueOperations;

    @Before
    public void beforeTest() {
        User user = new User();
        user.setId("user");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(anyString(), any(User.class));
        when(valueOperations.get(anyString())).thenReturn(user);
    }

    @Test
    public void register() {
        when(redisTemplate.hasKey(anyString())).thenReturn(false);
        User user = new User();
        user.setId("user");
        User rUser = fakeDatabase.register(user);
        Assert.assertEquals(user.getId(), rUser.getId());
    }

    @Test
    public void edit() {
        when(redisTemplate.hasKey(anyString())).thenReturn(true);
        User user = new User();
        user.setId("user");
        User rUser = fakeDatabase.edit(user);
        Assert.assertEquals(user.getId(), rUser.getId());
    }

    @Test
    public void read() {
        when(redisTemplate.hasKey(anyString())).thenReturn(true);
        User user = new User();
        user.setId("user");
        User rUser = fakeDatabase.read("user");
        Assert.assertEquals(user.getId(), rUser.getId());
    }

    @Test
    public void delete() {
        when(redisTemplate.hasKey(anyString())).thenReturn(true);
        User rUser = fakeDatabase.delete("user", true);
        Assert.assertEquals("user", rUser.getId());
    }
}
