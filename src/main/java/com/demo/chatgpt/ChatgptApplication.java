package com.demo.chatgpt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ChatgptApplication {


    @Value("${openai.key}")
    private String openAiKey;

    public static void main(String[] args) {
		SpringApplication.run(ChatgptApplication.class, args);
	}
// HTTP 요청시 RestTemplate 설정 요청마다 API KEY 인증정보 추가
    @Bean
    public RestTemplate restTemplate()
    {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getInterceptors().add(((request, body, execution) ->
        {
                request.getHeaders().add("Authorization",
                        "Bearer "+openAiKey);
            return execution.execute(request,body);
         }));
        return  restTemplate;
    }
}

