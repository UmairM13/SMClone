package com.example.springfsd.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PostResponseDTO {
    private Long id;
    private String text;
    private Long authorId;
}
