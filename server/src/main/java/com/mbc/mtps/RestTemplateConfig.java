package com.mbc.mtps;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// Gemini API처럼 외부 서버에 HTTP 요청을 보낼 때 쓰는 RestTemplate을
// 스프링 빈으로 등록해서, 다른 클래스에서 주입받아 재사용할 수 있게 함.
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}