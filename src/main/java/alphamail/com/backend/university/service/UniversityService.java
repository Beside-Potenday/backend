package alphamail.com.backend.university.service;

import alphamail.com.backend.university.model.InputMessageDTO;
import alphamail.com.backend.university.model.OutputMessageDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class UniversityService {
    @Value("${UniversityApiUrl}")
    private String univApiUrl;

    @Value("${ApiGatewayKey}")
    private String univApiGatewayKey;

    @Value("${clovaStudioApiKey}")
    private String clovaStudioApiKey;


    public OutputMessageDTO getResult(InputMessageDTO inputMessageDTO) throws Exception{
        //전송 url 설정
        URL url = new URL(univApiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //post 요청 설정
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        //헤더에 추가할 데이터 저장
        connection.setRequestProperty("X-NCP-CLOVASTUDIO-API-KEY", clovaStudioApiKey);
        connection.setRequestProperty("X-NCP-APIGW-API-KEY", univApiGatewayKey);
        connection.setRequestProperty("Content-Type", "application/json");
        //보낼 데이터 정의
        String contentData = "";

        if(!inputMessageDTO.sender().isEmpty()) contentData = contentData.concat("이름 : " + inputMessageDTO.sender()+"\n");
        if(!inputMessageDTO.receiver().isEmpty()) contentData = contentData.concat("교수님 성함 : " + inputMessageDTO.receiver()+"\n");
        if(!inputMessageDTO.department().isEmpty()) contentData = contentData.concat("학부 : " + inputMessageDTO.department()+"\n");
        if(!inputMessageDTO.studentId().isEmpty()) contentData = contentData.concat("학번 : " + inputMessageDTO.studentId()+"\n");
        if(!inputMessageDTO.subject().isEmpty()) contentData = contentData.concat("과목명 : " + inputMessageDTO.subject()+"\n");
        if(!inputMessageDTO.content().isEmpty()) contentData = contentData.concat(inputMessageDTO.content()+"\n");

        String inputData = String.format("""
                {
                  "messages" : [ {
                    "role" : "system",
                    "content" : ""
                  }, {
                    "role" : "user",
                    "content" : "%s"
                  } ],
                  "topP" : 0.8,
                  "topK" : 0,
                  "maxTokens" : 256,
                  "temperature" : 0.5,
                  "repeatPenalty" : 5.0,
                  "stopBefore" : [ ],
                  "includeAiFilters" : true,
                  "seed" : 0
                }""", contentData);

        //데이터 전송
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = inputData.getBytes(StandardCharsets.UTF_8); // JSON 문자열을 바이트 배열로 변환
            os.write(input, 0, input.length); // 변환된 바이트 배열을 출력 스트림을 통해 전송
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); // 서버로부터의 응답을 읽기 위한 BufferedReader를 생성
        String inputLine;
        StringBuffer response = new StringBuffer(); // 서버 응답을 저장할 StringBuffer 객체를 생성

        while ((inputLine = in.readLine()) != null) { // 서버 응답의 끝까지 한 줄씩 읽어 들임
            response.append(inputLine); // 읽은 데이터를 StringBuffer 객체에 추가
        }
        in.close(); // BufferedReader를 닫아 리소스를 해제

        JSONObject jsonObject = new JSONObject(response.toString());
        String content = jsonObject.getJSONObject("result").getJSONObject("message").getString("content");

        return new OutputMessageDTO(contentData ,content);
    }
}

