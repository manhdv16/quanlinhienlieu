package com.duymanh.quanlinhienlieu.exception;

import com.duymanh.quanlinhienlieu.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException
{
    /**
     * Function is used to solve AppException
     * @param e AppException
     * @return request by API response form
     */
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException e)
    {
        ErrorCode errorCode = e.getErrorCode();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(errorCode.getCode());
        apiResponse.setMessage(e.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    /**
     * Function is used to solve ValidateException
     * @param e MethodArgumentNotValidException
     * @return request by API response form
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException e)
    {
        // Get default message from error field in request class and get error code follow mes
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }
}