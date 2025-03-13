package com.jian.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException implements Serializable {

    private final Integer code = -1;
    private final String msg;
}
