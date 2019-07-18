package com.pccw.assessment.resource;


import com.pccw.assessment.entity.User;
import com.pccw.assessment.manager.UserManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
public class UserEndpointImpl {

    @Autowired
    private UserManager userManager;

    @ApiOperation(value = "Register user")
    @PostMapping(value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<String> register(@RequestBody @NotNull User user) {
        user.setId(UUID.randomUUID().toString());
        boolean success = userManager.register(user);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(String.format("Register for %s successfully.", user.getName()));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(String.format("Register for %s unsuccessfully.", user.getName()));
        }
    }

    @ApiOperation(value = "Register user")
    @PostMapping(value = "/async",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<String> registerAsync(@RequestBody @NotNull User user) {
        user.setId(UUID.randomUUID().toString());
        boolean success = userManager.register(user, false);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(String.format("Register for %s successfully.", user.getName()));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(String.format("Register for %s unsuccessfully.", user.getName()));
        }
    }

    @ApiOperation(value = "Completely edit a user")
    @PutMapping(value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<String> put(@RequestBody @NotNull User user) {
        if (user.getId() == null || user.getId().trim().length() == 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Id is missing");
        }

        boolean success = userManager.put(user);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(String.format("Edit for %s successfully.", user.getName()));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(String.format("Edit for %s unsuccessfully.", user.getName()));
        }
    }

    @ApiOperation(value = "Partially edit a user")
    @PatchMapping(value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<String> patch(@RequestBody @NotNull User user) {
        if (user.getId() == null || user.getId().trim().length() == 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Id is missing");
        }

        boolean success = userManager.patch(user);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(String.format("Edit for %s successfully.", user.getName()));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(String.format("Edit for %s unsuccessfully.", user.getName()));
        }
    }

    @ApiOperation(value = "Get a user")
    @GetMapping(value = "/{id}",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<User> read(@NotNull @PathVariable(value = "id") String id) {

        User user = userManager.read(id);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(user);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(user);
        }
    }

    @ApiOperation(value = "Delete a user")
    @DeleteMapping(value = "/{id}",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<User> delete(@NotNull @PathVariable(value = "id") String id) {

        User user = userManager.delete(id);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(user);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(user);
        }
    }
}
