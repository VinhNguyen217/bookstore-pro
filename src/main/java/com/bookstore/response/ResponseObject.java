package com.bookstore.response;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
public class ResponseObject {

    private Integer status;
    private String message;
    private Object data;
    private static final String SUCCESS = "Successfully";
    private static final String FAILED = "Failed";
    private static final String NOT_FOUND = "Not Found";

    public ResponseObject(HttpStatus status, String message, Object data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

    public ResponseObject(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }

    public static ResponseEntity<ResponseObject> success() {
        return ResponseEntity.ok().body(new ResponseObject(HttpStatus.OK, SUCCESS));
    }

    public static ResponseEntity<ResponseObject> success(Object data) {
        return ResponseEntity.ok().body(new ResponseObject(HttpStatus.OK, SUCCESS, data));
    }

    public static ResponseEntity<ResponseObject> loginSuccess(Object data) {
        return ResponseEntity.ok().body(new ResponseObject(HttpStatus.OK, ResponseMessage.LOGIN_SUCCESS, data));
    }

    public static ResponseEntity<ResponseObject> logoutSuccess() {
        return ResponseEntity.ok().body(new ResponseObject(HttpStatus.OK, ResponseMessage.LOGOUT_SUCCESS));
    }

    public static ResponseEntity<ResponseObject> build(HttpStatus status, String message, Object data) {
        return ResponseEntity.status(status).body(new ResponseObject(status, message, data));
    }

}
