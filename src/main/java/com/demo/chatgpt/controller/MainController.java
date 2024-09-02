//package com.demo.chatgpt.controller;
//
//
//import com.demo.chatgpt.model.ChatRequest;
//import com.demo.chatgpt.model.ChatResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//@Controller
//public class MainController {
//    @Autowired
//    RestTemplate restTemplate;
//
//    @GetMapping("/")
//    public String mainPage() {
//        return "main";
//    }
//    @GetMapping("/chat")
//    public String chatPage(Model model) {
//        // 필요한 경우 여기에 모델 속성을 추가할 수 있습니다.
//        return "chat";
//    }
//    @GetMapping("/menu")
//    public String indexPage() {
//        // 필요한 경우 여기에 모델 속성을 추가할 수 있습니다.
//        return "menu";
//    }
//    @GetMapping("/menu-details")
//    public String menuDetailsPage(@RequestParam String cuisine, Model model) {
//        model.addAttribute("cuisine", cuisine);
//        return "menuDetail";
//    }
//    @PostMapping("/hitOpenAiApi")
//    @ResponseBody
//    public String getOpenAiResponse(@RequestBody String prompt) {
//        ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", prompt);
//
//        ChatResponse response =
//                restTemplate.postForObject("https://api.openai.com/v1/chat/completions", chatRequest, ChatResponse.class);
//        return response.getChoices().get(0).getMessage().getContent();
//    }
//}

package com.demo.chatgpt.controller;

import com.demo.chatgpt.dto.RecommendationRequest;
import com.demo.chatgpt.dto.RecommendationResponse;
import com.demo.chatgpt.model.ChatRequest;
import com.demo.chatgpt.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@Controller
public class MainController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/chat")
    public String chatPage(Model model) {
        return "chat";
    }

    @GetMapping("/menu")
    public String indexPage() {
        return "menu";
    }

    @GetMapping("/menu-details")
    public String menuDetailsPage(@RequestParam String cuisine, Model model) {
        model.addAttribute("cuisine", cuisine);
        return "menuDetail";
    }

    @GetMapping("/recommendation")
    public String recommendationPage(@RequestParam String cuisine, @RequestParam String requirements, Model model) {
        model.addAttribute("cuisine", cuisine);
        model.addAttribute("requirements", requirements);
        return "recommendation";
    }

    @PostMapping("/getRecommendation")
    @ResponseBody
    public ResponseEntity<?> getRecommendation(@RequestBody RecommendationRequest request) {
        try {
            String prompt = String.format(
                    "당신은 전문 요리사이자 AI 메뉴 추천 시스템입니다. 다음 조건에 맞는 %s 요리 메뉴를 1개 추천해주세요. 추가 요구사항: %s\n\n" +
                            "응답 형식:\n" +
                            "추천 메뉴: [메뉴 이름]\n" +
                            "추천 이유: [2-3문장으로 이 메뉴를 추천한 이유를 설명해주세요. 요청한 요구사항과 연관지어 설명하고, AI다운 통찰력있는 코멘트를 추가해주세요.]",
                    request.getCuisine(), request.getRequirements());

            ChatRequest chatRequest = new ChatRequest("gpt-4o-mini", prompt);
            ChatResponse response = restTemplate.postForObject("https://api.openai.com/v1/chat/completions", chatRequest, ChatResponse.class);

            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                String recommendation = response.getChoices().get(0).getMessage().getContent();
                return ResponseEntity.ok(new RecommendationResponse(recommendation));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("GPT 응답이 비어있습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 추천 중 오류 발생: " + e.getMessage());
        }
    }
}
//    @PostMapping("/hitOpenAiApi")
//    @ResponseBody
//    public String getOpenAiResponse(@RequestBody String prompt) {
//        ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", prompt);
//
//        ChatResponse response =
//                restTemplate.postForObject("https://api.openai.com/v1/chat/completions", chatRequest, ChatResponse.class);
//        return response.getChoices().get(0).getMessage().getContent();
//    }
//}