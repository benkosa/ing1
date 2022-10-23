package org.example.Shared;

/**
 * codes:
 *  0 - success
 *  1 - error
 * @param <T> type of response data
 */
public class Response<T> {

    public int code;
    public String message;
    public T data;

    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
