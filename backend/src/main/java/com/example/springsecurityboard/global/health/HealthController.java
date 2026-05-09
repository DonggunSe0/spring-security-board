package com.example.springsecurityboard.global.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//RestController는 문자열이나 JSON 데이터를 바로 응답으로 돌려줄 때 사용해.
@RestController
public class HealthController {

    @GetMapping("api/v1/health")
    public String health() {
        return "OK";
    }
}
