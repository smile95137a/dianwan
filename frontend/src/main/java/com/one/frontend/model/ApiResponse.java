package com.one.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;
    private int code;
    private String message;
    private boolean success;
    private T data;
}