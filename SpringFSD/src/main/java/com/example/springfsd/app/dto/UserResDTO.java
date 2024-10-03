package com.example.springfsd.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String token;

    private List<UserDTO> followers;
    private List<UserDTO> following;
    private List<PostResponseDTO> posts;

    // Constructor without token
    public UserResDTO(Long id, String firstName, String lastName, String username, List<UserDTO> followers, List<UserDTO> following, List<PostResponseDTO> posts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.followers = followers;
        this.following = following;
        this.posts = posts;
    }
}