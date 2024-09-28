package com.example.springfsd.app.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String token;

    public UserResponseDTO(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public UserResponseDTO(Long id, String firstName, String lastName, String username) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }
}
