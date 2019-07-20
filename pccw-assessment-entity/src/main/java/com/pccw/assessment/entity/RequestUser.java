package com.pccw.assessment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
public class RequestUser {

    private String id;

    @NotNull(message = "Name must not be empty")
    private String name;

    @NotNull(message = "Age must not be empty")
    @Min(value = 1, message = "Age must be greater or equal to 1")
    private Integer age;

    private String nation;

    private String email;
}
