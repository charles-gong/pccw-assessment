package com.pccw.assessment.database;

import com.pccw.assessment.entity.User;
import com.pccw.assessment.exception.ExceptionEnum;
import com.pccw.assessment.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FakeDatabase {


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Register user
     *
     * @param user {@link User} passed user info
     * @return true|false
     */
    public User register(User user) {
        boolean exist = redisTemplate.hasKey(user.getId());
        if (exist) {
            throw new UserException(ExceptionEnum.EXIST);
        } else {
            redisTemplate.opsForValue().set(user.getId(), user);
            return user;
        }
    }

    /**
     * Edit user
     *
     * @param user {@link User} passed user info
     * @return true|false
     */
    public User edit(User user) {
        boolean exist = redisTemplate.hasKey(user.getId());
        if (exist) {
            redisTemplate.opsForValue().set(user.getId(), user);
            return user;
        } else {
            throw new UserException(ExceptionEnum.NOT_FOUND);
        }
    }

    /**
     * Read user by id
     *
     * @param id User id
     * @return User info
     */
    public User read(String id) {
        boolean exist = redisTemplate.hasKey(id);
        if (exist) {
            User user = (User) redisTemplate.opsForValue().get(id);
            return user;
        } else {
            throw new UserException(ExceptionEnum.NOT_FOUND);
        }
    }

    /**
     * Delete user by id
     *
     * @param id     User id
     * @param isSoft if this delete action is soft delete for physical delete
     * @return User info
     */
    public User delete(String id, boolean isSoft) {
        boolean exist = redisTemplate.hasKey(id);
        if (!exist) {
            throw new UserException(ExceptionEnum.NOT_FOUND);
        }
        User user = (User) redisTemplate.opsForValue().get(id);
        if (isSoft) {
            user.setIsDeleted(true);
            redisTemplate.opsForValue().set(id, user);
            return user;
        } else {
            redisTemplate.delete(id);
            return user;
        }
    }
}
