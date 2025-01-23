package com.example.camel_sql.entity;

public class Response {

    private String status;
    private String message;
    private Object data;
    private ErrorResponse error;

    public Response(String status, String message, Object data, ErrorResponse error) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public static Response success(String message, Object data) {
        return new Response("success", message, data, null);
    }

    public static Response fail(String message, String code, String details) {
        return new Response("fail", message, null, new ErrorResponse(code, details));
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    public static class ErrorResponse {
        private String code;
        private String details;

        public ErrorResponse(String code, String details) {
            this.code = code;
            this.details = details;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }
    }
}
