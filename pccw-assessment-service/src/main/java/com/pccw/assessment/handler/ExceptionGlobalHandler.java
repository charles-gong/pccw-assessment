package com.pccw.assessment.handler;

import com.pccw.assessment.entity.User;
import com.pccw.assessment.exception.ExceptionEnum;
import com.pccw.assessment.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ExceptionGlobalHandler {

    /**
     * Catch all the exception during dealing requests
     *
     * @param e exception
     * @return error info
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorInfo handler(Exception e) {
        if (e instanceof UserException) {
            UserException userException = (UserException) e;
            return toErrorInfo(userException);
        } else {
            log.error("Oops, there is something unknown happens...{}", e);
            return toErrorInfo(new UserException(ExceptionEnum.UNKONW_ERROR));
        }
    }

    /**
     * Convert {@link UserException} to {@link ErrorInfo}
     *
     * @param userException UserException
     * @return ErrorInfo
     */
    private ErrorInfo toErrorInfo(UserException userException) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setCode(userException.getCode());
        errorInfo.setMessage(userException.getMessage());
        return errorInfo;
    }
}
