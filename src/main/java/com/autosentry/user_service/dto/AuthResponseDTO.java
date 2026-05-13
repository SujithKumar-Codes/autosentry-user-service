package com.autosentry.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String token; //Once user registers, JWT is generated and sent to the client-side so that it can be sent back in the headers of the future requests.
    private Long userId;
    private String name;
}