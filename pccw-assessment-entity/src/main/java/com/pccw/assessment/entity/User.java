package com.pccw.assessment.entity;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private String id;

    @NotNull
    private String name;

    private Integer age;

    private String nation;

    private String email;

    private boolean isDeleted;
}
