package com.pccw.assessment.database;

import com.pccw.assessment.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private HashOperations hashOperations;

    @Before
    public void beforeTest() {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        doNothing().when(hashOperations).put(anyString(), anyString(), any(User.class));
    }

    @Test
    public void register() {
        when(hashOperations.get(anyString(), anyString())).thenReturn(null);
        User user = new User();
        user.setId("user");
        User rUser = fakeDatabase.register(user);
        Assert.assertEquals(user.getId(), rUser.getId());
    }

    @Test
    public void edit() {
        User user = new User();
        user.setId("user");
        when(hashOperations.get(anyString(), anyString())).thenReturn(user);
        User rUser = fakeDatabase.edit(user);
        Assert.assertEquals(user.getId(), rUser.getId());
    }

    @Test
    public void read() {

        User user = new User();
        user.setId("user");
        when(hashOperations.get(anyString(), anyString())).thenReturn(user);
        User rUser = fakeDatabase.read("user");
        Assert.assertEquals(user.getId(), rUser.getId());
    }

    @Test
    public void delete() {
        User user = new User();
        user.setId("user");
        when(hashOperations.get(anyString(), anyString())).thenReturn(user);
        User rUser = fakeDatabase.delete("user", true);
        Assert.assertEquals("user", rUser.getId());
    }

    @Test
    public void list() {
        User user = new User();
        user.setId("user");
        Map<String, User> map = new HashMap<>();
        map.put(user.getId(), user);
        when(hashOperations.entries(anyString())).thenReturn(map);
        List<User> users = fakeDatabase.list();
        Assert.assertEquals(users.size(), 1);
    }
}
