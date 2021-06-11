package space.borisgk98.kubedom.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.borisgk98.kubedom.api.cosnt.AppConst;
import space.borisgk98.kubedom.api.cosnt.AppUrls;
import space.borisgk98.kubedom.api.model.dto.rest.AuthenticationRequest;
import space.borisgk98.kubedom.api.model.entity.AppUser;
import space.borisgk98.kubedom.api.security.JwtTokenProvider;
import space.borisgk98.kubedom.api.service.AppUserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(AppConst.SERVER_PREFIX)
@RequiredArgsConstructor
public class SecurityController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppUserService appUserService;

    @PostMapping(AppUrls.LOGIN)
    public ResponseEntity login(@RequestBody AuthenticationRequest data, HttpServletResponse response) throws Throwable {
        try {
            String login = data.getLogin();
            String password = data.getPassword();
            AppUser appUser = appUserService.findByLogin(login)
                    .orElseThrow(() -> new BadCredentialsException("Invalid username/password supplied"));
            if (!appUser.getPassHash().equals(password)) {
                throw new BadCredentialsException("Invalid username/password supplied");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
            String token = jwtTokenProvider.createToken(
                    login,
//                    this.users.findByLogin(login)
//                            .orElseThrow(() -> new UsernameNotFoundException("Username " + login + "not found"))
//                            .getRoles()
//                            .stream().map(UserRole::getRole).collect(Collectors.toList())
                    Arrays.asList("none")
            );
            Map<Object, Object> model = new HashMap<>();
            model.put("login", login);
            model.put("token", token);
            response.addCookie(new Cookie(AppConst.AUTH_HEADER, token));
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
        catch (Throwable e) {
            throw e;
        }
    }

    @PostMapping(AppUrls.REGISTER)
    public ResponseEntity register(@RequestBody AuthenticationRequest data, HttpServletResponse response) throws Throwable {
        try {
            AppUser user = new AppUser();
            if (!appUserService.existByLogin(data.getLogin())) {
                user.setLogin(data.getLogin());
                user.setPassHash(data.getPassword());
                appUserService.create(user);
            }
            else {
                throw new IllegalArgumentException();
            }
            String username = data.getLogin();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(
                    username,
                    Arrays.asList("none")
            );
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            response.addCookie(new Cookie(AppConst.AUTH_HEADER, token));
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
        catch (Throwable e) {
            throw e;
        }
    }

    @PostMapping(AppUrls.LOGOUT)
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, null, null);
        response.addCookie(new Cookie(AppConst.AUTH_HEADER, ""));
        return ResponseEntity.ok("{}");
    }

    @GetMapping(AppUrls.CHECK)
    public ResponseEntity check() {
        return ResponseEntity.ok("");
    }

}
