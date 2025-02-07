package umc7th.bulk.record.gpt.service;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import umc7th.bulk.record.gpt.dto.request.ChatGPTRequest;
import umc7th.bulk.record.gpt.dto.response.ChatGPTResponse;

@Service
@RequiredArgsConstructor
public class AiCallService {
    @Value("${openai.model}")
    private String apiModel;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate template;

    public ChatGPTResponse requestTextAnalysis(String requestText) {
        ChatGPTRequest request = ChatGPTRequest.createTextRequest(apiModel, 500, "user", requestText);
        return template.postForObject(apiUrl, request, ChatGPTResponse.class);
    }

    public ChatGPTResponse requestImageAnalysis(MultipartFile image, String requestText) throws IOException {
        String base64Image = Base64.encodeBase64String(image.getBytes());
        String imageUrl = "data:image/jpeg;base64," + base64Image;
        ChatGPTRequest request = ChatGPTRequest.createImageRequest(apiModel, 500, "user", requestText, imageUrl);
        return template.postForObject(apiUrl, request, ChatGPTResponse.class);
    }
}