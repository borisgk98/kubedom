package space.borisgk98.kubedom.api.model.dto.rest;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String login;
    private String password;
}
