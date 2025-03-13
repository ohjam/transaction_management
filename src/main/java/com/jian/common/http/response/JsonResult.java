package com.jian.common.http.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class JsonResult<T> {

    private String requestId = UUID.randomUUID().toString();

    private Boolean success;

    private String message;

    private Integer code;

    private T data;
}
