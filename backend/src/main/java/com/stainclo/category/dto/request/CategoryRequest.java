package com.stainclo.category.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank
        String categoryName
) {
}
