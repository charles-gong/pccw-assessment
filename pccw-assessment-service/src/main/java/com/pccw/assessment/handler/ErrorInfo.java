package com.pccw.assessment.handler;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class ErrorInfo {

    private int code;

    private String message;
}
