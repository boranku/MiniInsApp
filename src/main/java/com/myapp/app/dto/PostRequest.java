package com.myapp.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class PostRequest {
    @NotBlank(message = "Description is required")
    @Size(min = 1, max = 2000, message = "Description must be between 1 and 2000 characters")
    private String description;

    @URL(message = "Image URL must be valid")
    private String imageUrl;
}
