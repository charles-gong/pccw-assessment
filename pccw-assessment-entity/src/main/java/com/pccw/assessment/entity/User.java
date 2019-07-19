package com.pccw.assessment.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class User {

    private String id;

    @NotNull(message = "Name must not be empty")
    private String name;

    @NotNull(message = "Age must not be empty")
    @Min(value = 0, message = "Age must be greater than 0")
    private Integer age;

    private String nation;

    @NotNull(message = "Email must not be empty")
    private String email;

    @JsonIgnore
    private Boolean isDeleted;
}
