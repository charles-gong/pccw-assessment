package com.pccw.assessment.database;

import com.pccw.assessment.entity.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FakeDatabase {

    /**
     * Fake database map
     * <p>
     * store all the registered user information
     */
    private static final Map<String, User> activeUserMap = new HashMap<>();
    private static final Map<String, User> deletedUserMap = new HashMap<>();

    /**
     * Register user
     *
     * @param user {@link User} passed user info
     * @return true|false
     */
    public boolean register(User user) {
        if (activeUserMap.containsKey(user.getId())) {
            return false;
        } else {
            activeUserMap.put(user.getId(), user);
            return true;
        }
    }

    /**
     * Edit user
     *
     * @param user {@link User} passed user info
     * @return true|false
     */
    public boolean edit(User user) {
        if (activeUserMap.containsKey(user.getId())) {
            activeUserMap.put(user.getId(), user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Read user by id
     *
     * @param id User id
     * @return User info
     */
    public User read(String id) {
        return activeUserMap.get(id);
    }

    /**
     * Delete user by id
     *
     * @param id     User id
     * @param isSoft if this delete action is soft delete for physical delete
     * @return User info
     */
    public User delete(String id, boolean isSoft) {
        if (isSoft) {
            User user = activeUserMap.get(id);
            if (user != null) {
                user.setDeleted(true);
            } else {
                return null;
            }
            activeUserMap.remove(id);
            deletedUserMap.put(id, user);
            return user;
        } else {
            User user = activeUserMap.remove(id);
            return user;
        }
    }
}
