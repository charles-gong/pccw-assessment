package com.pccw.assessment.resource;


import com.pccw.assessment.entity.RequestUser;
import com.pccw.assessment.entity.User;
import com.pccw.assessment.exception.ExceptionEnum;
import com.pccw.assessment.exception.UserException;
import com.pccw.assessment.manager.UserManager;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserEndpointImpl {

    @Autowired
    private UserManager userManager;

    @ApiOperation(value = "Register user")
    @PostMapping(value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<RequestUser> register(@RequestBody @Valid RequestUser requestUser) {
        requestUser.setId(UUID.randomUUID().toString());
        User user = toUser(requestUser);
        userManager.register(user);
        log.info("Register {} successfully.", user.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(requestUser);
    }

    @ApiOperation(value = "Register user")
    @PostMapping(value = "/async",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<RequestUser> registerAsync(@RequestBody @Valid RequestUser requestUser) {
        requestUser.setId(UUID.randomUUID().toString());
        User user = toUser(requestUser);
        userManager.register(user, false);

        log.info("Register {} successfully.", user.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(requestUser);
    }

    @ApiOperation(value = "Completely edit a user")
    @PutMapping(value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<RequestUser> put(@RequestBody @Valid RequestUser requestUser) {
        if (requestUser.getId() == null || requestUser.getId().trim().length() == 0) {
            throw new UserException(ExceptionEnum.NOT_FOUND);
        }
        User user = toUser(requestUser);
        userManager.put(user);
        log.info("Edit {} successfully.", user.getName());
        return ResponseEntity.status(HttpStatus.OK)
                .body(requestUser);
    }

    @ApiOperation(value = "Partially edit a user")
    @PatchMapping(value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<RequestUser> patch(@RequestBody @NotNull RequestUser requestUser) {
        if (requestUser.getId() == null || requestUser.getId().trim().length() == 0) {
            throw new UserException(ExceptionEnum.NOT_FOUND);
        }

        if (requestUser.getName() == null || requestUser.getName().trim().length() == 0) {
            throw new UserException(400, "Name must not be empty");
        }

        if (requestUser.getEmail() == null || requestUser.getEmail().trim().length() == 0) {
            throw new UserException(400, "Email must not be empty");
        }
        User user = toUser(requestUser);
        user = userManager.patch(user);
        requestUser = toRequestUser(user);
        log.info("Edit {} successfully.", user.getName());
        return ResponseEntity.status(HttpStatus.OK)
                .body(requestUser);

    }

    @ApiOperation(value = "Get a user")
    @GetMapping(value = "/{id}",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<RequestUser> read(@NotNull(message = "Id must not be empty")
                                            @PathVariable(value = "id") String id) {

        User user = userManager.read(id);
        RequestUser requestUser = toRequestUser(user);
        log.info("Read {} successfully.", user.getName());
        return ResponseEntity.status(HttpStatus.OK)
                .body(requestUser);
    }

    @ApiOperation(value = "Delete a user")
    @DeleteMapping(value = "/{id}",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<RequestUser> delete(@NotNull(message = "Id must not be empty")
                                              @PathVariable(value = "id") String id) {

        User user = userManager.delete(id);
        RequestUser requestUser = toRequestUser(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(requestUser);
    }

    @ApiOperation(value = "List users")
    @GetMapping(value = "",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<RequestUser>> list() {

        List<User> userList = userManager.list();
        List<RequestUser> vals = userList.stream().map(this::toRequestUser).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK)
                .body(vals);
    }

    private static final BeanCopier toUserCopier = BeanCopier.create(RequestUser.class, User.class, false);
    private static final BeanCopier toRequestUserCopier = BeanCopier.create(User.class, RequestUser.class, false);

    /**
     * @param requestUser
     * @return
     */
    private User toUser(RequestUser requestUser) {
        User user = new User();
        toUserCopier.copy(requestUser, user, null);
        return user;
    }

    /**
     * @param user
     * @return
     */
    private RequestUser toRequestUser(User user) {
        RequestUser requestUser = new RequestUser();
        toRequestUserCopier.copy(user, requestUser, null);
        return requestUser;
    }
}
