package com.shopflow.exception;

import java.util.List;

public record StandardErrorDTO(
    int status,
    String message,
    List<String> errors
) {}
