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
import java.util.StringTokenizer;

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

        //프론트엔드에서 데이터를 공백으로 입력할 경우 포함하지 않기 위해 if문 추가
        if(!inputMessageDTO.sender().isEmpty()) contentData = contentData.concat("이름 : " + inputMessageDTO.sender()+"\n");
        if(!inputMessageDTO.receiver().isEmpty()) contentData = contentData.concat("교수님 성함 : " + inputMessageDTO.receiver()+"\n");
        if(!inputMessageDTO.department().isEmpty()) contentData = contentData.concat("학부 : " + inputMessageDTO.department()+"\n");
        if(!inputMessageDTO.studentId().isEmpty()) contentData = contentData.concat("학번 : " + inputMessageDTO.studentId()+"\n");
        if(!inputMessageDTO.subject().isEmpty()) contentData = contentData.concat("과목명 : " + inputMessageDTO.subject()+"\n");
        if(!inputMessageDTO.content().isEmpty()) contentData = contentData.concat(inputMessageDTO.content()+"\n");
        //title과 content 분리의 편의성 위해 추가
        contentData = contentData.concat("제목 앞에는 '제목 : '을 붙여 주고, 제목과 내용은 엔터로 구분지어 주세요.");

        //범위 : 0 ~ 1, 선택 확률 값이 높은 토큰을 순서대로 나열한 후 설정한 누적 확률 값에 포함되지 않는 토큰을 제거할 때 사용하는 기준값, 보통 0.8 ~ 1로 설정
        double topP = 0.8;
        //범위 : 0 ~ 128, 확률 값이 가장 높은 K개 중에서 하나를 선택할 때 사용하는 기준 값, 특수한 경우 아닐 경우 0으로 설정
        int topK = 0;
        //범위 : 0 ~ 4096, Maximum tokens는 결괏값을 생성할 때 사용할 최대 토큰 수, 토큰 수를 높게 설정할 수록 긴 결괏값을 출력, 보통 300 ~ 500으로 설정
        int maxTokens = 350;
        //범위 : 0 ~ 1, 낮으면 정형화된 응답, 높으면 다양한 문장을 만들지만 문장의 질이 떨어질 수 있음, Top P를 고정한 채로 변경하는 것을 추천,
        double temperature = 0.7;
        //범위 : 0 ~ 10, 반복적인 결괏값을 생성하지 않도록 반복되는 토큰에는 감점 요소를 부여하는 값
        double repeatPenalty = 5;

        String inputData = String.format("""
                {
                  "messages" : [ {
                    "role" : "system",
                    "content" : ""
                  }, {
                    "role" : "user",
                    "content" : "%s"
                  } ],
                  "topP" : %f,
                  "topK" : %d,
                  "maxTokens" : %d,
                  "temperature" : %f,
                  "repeatPenalty" : %f,
                  "stopBefore" : [ ],
                  "includeAiFilters" : true,
                  "seed" : 0
                }""", contentData, topP, topK, maxTokens, temperature, repeatPenalty);

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
        String result = jsonObject.getJSONObject("result").getJSONObject("message").getString("content");
        
        //title과 content '\n'으로 분리
        StringTokenizer st = new StringTokenizer(result,"\n");
        //title의 경우 '제목 : '을 제거하기 위해 substring(5) 로 지정
        String title = st.nextToken().substring(5);

        //content 내용 추가
        String content = "";
        while(st.hasMoreTokens()) {
            content = content.concat(st.nextToken() + "\n");
        }

        return new OutputMessageDTO(title ,content);
    }
}

