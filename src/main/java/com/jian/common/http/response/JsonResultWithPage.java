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
public class JsonResultWithPage<T> {

    private String requestId = UUID.randomUUID().toString();

    private Boolean success;

    private String message;

    private Integer code;

    private T data;

    private Long totalRows;

    private Integer totalPages;

    private Integer pageNumber;

    private Integer pageSize;

}
