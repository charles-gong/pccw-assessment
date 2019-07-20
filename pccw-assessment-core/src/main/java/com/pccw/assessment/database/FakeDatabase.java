package com.pccw.assessment.database;

import com.pccw.assessment.entity.User;
import com.pccw.assessment.exception.ExceptionEnum;
import com.pccw.assessment.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        User exist = (User) redisTemplate.opsForHash().get("users", user.getId());
        if (exist != null) {
            throw new UserException(ExceptionEnum.EXIST);
        } else {
            redisTemplate.opsForHash().put("users", user.getId(), user);
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
        if (!checkExist(user.getId())) {
            throw new UserException(ExceptionEnum.NOT_FOUND);
        }
        redisTemplate.opsForHash().put("users", user.getId(), user);
        return user;
    }

    /**
     * Read user by id
     *
     * @param id User id
     * @return User info
     */
    public User read(String id) {
        if (!checkExist(id)) {
            throw new UserException(ExceptionEnum.NOT_FOUND);
        }

        return (User) redisTemplate.opsForHash().get("users", id);
    }

    /**
     * Delete user by id
     *
     * @param id     User id
     * @param isSoft if this delete action is soft delete for physical delete
     * @return User info
     */
    public User delete(String id, boolean isSoft) {
        if (!checkExist(id)) {
            throw new UserException(ExceptionEnum.NOT_FOUND);
        }
        User user = (User) redisTemplate.opsForHash().get("users", id);
        if (isSoft) {
            user.setDeleted(true);
            redisTemplate.opsForHash().put("users", user.getId(), user);
            return user;
        } else {
            redisTemplate.opsForHash().delete(user, user.getId());
            return user;
        }
    }

    /**
     * List users
     *
     * @return user list
     */
    public List<User> list() {
        Map<String, User> map = redisTemplate.opsForHash().entries("users");
        List<User> users = new ArrayList<>();
        map.forEach((s, u) -> {
            if (!u.isDeleted()) {
                users.add(u);
            }
        });

        return users;
    }


    /**
     * Check if the user is deleted or not exist
     *
     * @param id user id
     * @return true|false
     */
    private boolean checkExist(String id) {
        User exist = (User) redisTemplate.opsForHash().get("users", id);
        if (exist != null && !exist.isDeleted()) {
            return true;
        } else {
            return false;
        }
    }
}
