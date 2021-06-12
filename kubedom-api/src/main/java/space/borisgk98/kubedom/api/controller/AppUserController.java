package space.borisgk98.kubedom.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.borisgk98.kubedom.api.cosnt.AppConst;
import space.borisgk98.kubedom.api.cosnt.AppUrls;
import space.borisgk98.kubedom.api.mapping.AppUserFullMapper;
import space.borisgk98.kubedom.api.model.dto.rest.AppUserFullDto;
import space.borisgk98.kubedom.api.security.SecurityService;

@RestController
@RequestMapping(AppConst.SERVER_PREFIX + AppUrls.APP_USER)
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserFullMapper appUserFullMapper;
    private final SecurityService securityService;

    @GetMapping(AppUrls.CURRENT)
    public AppUserFullDto getCurrAppUser() {
        return appUserFullMapper.map(securityService.getCurrAppUser());
    }
}
