package com.example.springfsd.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class PostResponseDTO {
    private Long id;
    private String text;
    private Long authorId;
    private AuthorDTO author;
    private List<LikeDTO> likes;
    private long epochSecond;

    public PostResponseDTO(Long id, String text, Long authorId) {
        this.id = id;
        this.text = text;
        this.authorId = authorId;
    }

    public PostResponseDTO(Long id, long epochSecond, AuthorDTO authorDTO, List<LikeDTO> likes) {
        this.id = id;
        this.epochSecond = epochSecond;
        this.author = authorDTO;
        this.likes = likes;
    }

    public PostResponseDTO(Long id, long epochSecond, String text, AuthorDTO authorDTO, List<LikeDTO> likes) {
        this.id = id;
        this.epochSecond = epochSecond;
        this.text = text;
        this.author = authorDTO;
        this.likes = likes;
    }

    @Data
    @AllArgsConstructor
    public static class AuthorDTO {
        private Long userId;
        private String firstName;
        private String lastName;
        private String username;
    }

    @Data
    @AllArgsConstructor
    public static class LikeDTO {
        private Long userId;
        private String firstName;
        private String lastName;
        private String username;
    }
}
