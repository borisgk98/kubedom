package space.borisgk98.kubedom.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.borisgk98.kubedom.api.cosnt.AppConst;
import space.borisgk98.kubedom.api.cosnt.AppUrls;
import space.borisgk98.kubedom.api.model.dto.rest.CurrentUserDto;
import space.borisgk98.kubedom.api.service.AppUserService;

@RestController
@RequestMapping(AppConst.SERVER_PREFIX + AppUrls.APP_USER)
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping(AppUrls.CURRENT)
    public CurrentUserDto getCurrAppUser() {
        return appUserService.getCurrentUserDto();
    }
}
