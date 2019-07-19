package com.pccw.assessment.resource;


import com.pccw.assessment.entity.User;
import com.pccw.assessment.exception.ExceptionEnum;
import com.pccw.assessment.exception.UserException;
import com.pccw.assessment.manager.UserManager;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

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
    public ResponseEntity<User> register(@RequestBody @Valid User user) {
        user.setId(UUID.randomUUID().toString());
        userManager.register(user);

        log.info("Register {} successfully.", user.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }

    @ApiOperation(value = "Register user")
    @PostMapping(value = "/async",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<User> registerAsync(@RequestBody @Valid User user) {
        user.setId(UUID.randomUUID().toString());
        userManager.register(user, false);

        log.info("Register {} successfully.", user.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }

    @ApiOperation(value = "Completely edit a user")
    @PutMapping(value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<User> put(@RequestBody @NotNull User user) {
        if (user.getId() == null || user.getId().trim().length() == 0) {
            throw new UserException(ExceptionEnum.NOT_FOUND);
        }

        userManager.put(user);
        log.info("Edit {} successfully.", user.getName());
        return ResponseEntity.status(HttpStatus.OK)
                .body(user);
    }

    @ApiOperation(value = "Partially edit a user")
    @PatchMapping(value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<User> patch(@RequestBody @NotNull User user) {
        if (user.getId() == null || user.getId().trim().length() == 0) {
            throw new UserException(ExceptionEnum.NOT_FOUND);
        }

        userManager.patch(user);
        log.info("Edit {} successfully.", user.getName());
        return ResponseEntity.status(HttpStatus.OK)
                .body(user);

    }

    @ApiOperation(value = "Get a user")
    @GetMapping(value = "/{id}",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<User> read(@NotNull(message = "Id must not be empty")
                                     @PathVariable(value = "id") String id) {

        User user = userManager.read(id);
        if (user != null) {
            log.info("Read {} successfully.", user.getName());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(user);
        } else {
            throw new UserException(ExceptionEnum.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Delete a user")
    @DeleteMapping(value = "/{id}",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<User> delete(@NotNull(message = "Id must not be empty")
                                       @PathVariable(value = "id") String id) {

        User user = userManager.delete(id);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(user);
        } else {
            throw new UserException(ExceptionEnum.NOT_FOUND);
        }
    }
}
