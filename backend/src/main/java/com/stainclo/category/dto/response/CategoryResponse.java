package com.stainclo.category.dto.response;

import com.stainclo.category.enums.CategoryStatus;

import java.time.LocalDateTime;

public record CategoryResponse(
        Long categoryId,
        String categoryName,
        CategoryStatus categoryStatus,
        LocalDateTime createdAt
) {
}
