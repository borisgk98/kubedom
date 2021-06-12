package space.borisgk98.kubedom.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.borisgk98.kubedom.api.cosnt.AppConst;

@RestController
@RequestMapping(AppConst.SERVER_PREFIX + "/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping
    public ResponseEntity check() {
        return ResponseEntity.ok("");
    }
}
