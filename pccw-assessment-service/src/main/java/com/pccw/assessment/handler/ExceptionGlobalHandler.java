package com.pccw.assessment.handler;

import com.pccw.assessment.exception.ExceptionEnum;
import com.pccw.assessment.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
    public ResponseEntity<ErrorInfo> handler(Exception e) {
        if (e instanceof UserException) {
            UserException userException = (UserException) e;
            if (((UserException) e).getCode() == -1) {
                return ResponseEntity.status(500).body(toErrorInfo(userException));
            } else {
                return ResponseEntity.status(((UserException) e).getCode()).body(toErrorInfo(userException));
            }
        } else if (e instanceof MethodArgumentNotValidException) {
            List<ObjectError> errors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
            if (!CollectionUtils.isEmpty(errors)) {
                String message = errors.get(0).getDefaultMessage();
                return ResponseEntity.status(400).body(toErrorInfo(new UserException(400, message)));
            } else {
                log.error("Oops, there is something unknown happens...{}", e);
                return ResponseEntity.status(500).body(toErrorInfo(new UserException(ExceptionEnum.UNKONW_ERROR)));
            }
        } else {
            log.error("Oops, there is something unknown happens...{}", e);
            return ResponseEntity.status(500).body(toErrorInfo(new UserException(ExceptionEnum.UNKONW_ERROR)));
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
