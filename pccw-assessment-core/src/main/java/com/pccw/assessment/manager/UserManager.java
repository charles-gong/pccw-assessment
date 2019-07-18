package com.pccw.assessment.manager;

import com.pccw.assessment.database.FakeDatabase;
import com.pccw.assessment.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserManager {

    @Autowired
    private FakeDatabase fakeDatabase;


    public boolean register(User user) {
        return fakeDatabase.register(user);
    }

    public boolean put(User user) {
        return fakeDatabase.edit(user);
    }

    public boolean patch(User user) {
        User exist = fakeDatabase.read(user.getId());
        Optional.ofNullable(user.getName()).ifPresent(exist::setName);
        Optional.ofNullable(user.getAge()).ifPresent(exist::setAge);
        Optional.ofNullable(user.getNation()).ifPresent(exist::setNation);
        return fakeDatabase.edit(user);
    }

    public User read(String id) {
        return fakeDatabase.read(id);
    }

    public User delete(String id) {
        return fakeDatabase.delete(id, true);
    }
}
