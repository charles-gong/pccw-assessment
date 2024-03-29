package com.pccw.assessment.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.valueextraction.UnwrapByDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class User {

    private String id;

    private String name;

    private Integer age;

    private String nation;

    private String email;

    private boolean deleted;
}
