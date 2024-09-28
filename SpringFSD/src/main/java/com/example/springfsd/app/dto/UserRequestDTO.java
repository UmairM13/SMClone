package com.example.springfsd.app.dto;

public record UserRequestDTO(
        String firstName,
        String lastName,
        String username,
        String password)
{

}