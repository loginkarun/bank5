package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detailed validation error information.
 * Maps to ErrorDetail schema in OpenAPI.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {

    private String field;
    private String issue;
}
