package space.borisgk98.kubedom.api.model.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String login;
    private String password;
}
