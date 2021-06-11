package space.borisgk98.kubedom.api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import space.borisgk98.kubedom.api.model.entity.Provider;
import space.borisgk98.kubedom.api.service.ProviderService;

@Component
@RequiredArgsConstructor
public class SecurityService {
    private final ProviderService providerService;

    public Provider getProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        String login = authentication.getName();
        if (login != null) {
            return providerService.findByLogin(login).orElse(null);
        }
        return null;
    }
}
