package com.pccw.assessment.manager;

import com.pccw.assessment.database.FakeDatabase;
import com.pccw.assessment.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserManager {

    @Autowired
    private FakeDatabase fakeDatabase;

    @Value("${pccw.assessment.notification.service.url}")
    private String notificationUrl;

    private RestTemplate restTemplate = new RestTemplate();

    private static final String URL_SUFFIX = "/notification";

    private static final ExecutorService service = Executors.newFixedThreadPool(2);

    /**
     * Default register function
     *
     * @param user {@link User}
     * @return User
     */
    public User register(User user) {
        return register(user, true);
    }

    /**
     * Register user info and sending the notification message
     *
     * @param user   user
     * @param isSync need asynchronous sending email or not
     * @return User
     */
    public User register(User user, boolean isSync) {
        Future future = service.submit(() -> sendNotification(user));
        if (isSync) {
            try {
                future.get();
            } catch (InterruptedException e) {
                log.error("UserManager :: register :: interruption exception :: {}", e);
            } catch (ExecutionException e) {
                log.error("UserManager :: register :: execution exception :: {}", e);
            }

            log.info("After receiving the notification response...");
        }
        return fakeDatabase.register(user);
    }

    /**
     * Send request to the notification service
     */
    private void sendNotification(User user) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(notificationUrl + URL_SUFFIX,
                    HttpMethod.POST, new HttpEntity<>(user.getEmail(), new HttpHeaders()), String.class);
        } catch (RestClientException e) {
            log.error("UserManager :: register :: sending the notification service :: {}", e);
        }
        if (responseEntity.getStatusCodeValue() == 200) {
            log.info("UserManager :: register :: sending the notification service :: {}", responseEntity.getBody());
        }
    }

    /**
     * Update the whole user info
     *
     * @param user {@link User}
     * @return User
     */
    public User put(User user) {
        return fakeDatabase.edit(user);
    }

    /**
     * Update partial user info
     *
     * @param user {@link User}
     * @return User
     */
    public User patch(User user) {
        User exist = fakeDatabase.read(user.getId());
        Optional.ofNullable(user.getName()).ifPresent(exist::setName);
        Optional.ofNullable(user.getAge()).ifPresent(exist::setAge);
        Optional.ofNullable(user.getNation()).ifPresent(exist::setNation);
        Optional.ofNullable(user.getEmail()).ifPresent(exist::setEmail);
        return fakeDatabase.edit(exist);
    }

    /**
     * Get user info by id
     *
     * @param id user id
     * @return {@link User}
     */
    public User read(String id) {
        return fakeDatabase.read(id);
    }

    /**
     * Delete the user by id
     *
     * @param id user id
     * @return {@link User}
     */
    public User delete(String id) {
        return fakeDatabase.delete(id, true);
    }
}
