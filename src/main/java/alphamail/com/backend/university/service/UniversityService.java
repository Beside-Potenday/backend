package alphamail.com.backend.university.service;

import alphamail.com.backend.business.model.BusinessMailRequest;
import alphamail.com.backend.common.model.ClovaRequest;
import alphamail.com.backend.common.model.ClovaResponse;
import alphamail.com.backend.common.model.ClovaTask;
import alphamail.com.backend.university.model.UniversityRequest;
import alphamail.com.backend.university.model.UniversityResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class UniversityService {
    @Value("${UniversityApiUrl}")
    private String univApiUrl;

    @Value("${ApiGatewayKey}")
    private String univApiGatewayKey;

    @Value("${clovaStudioApiKey}")
    private String clovaStudioApiKey;

    private final RestTemplate restTemplate;

    public UniversityService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }


    public UniversityResponse getResult(UniversityRequest universityRequest) throws Exception{
        //헤더에 추가할 데이터 저장
        HttpHeaders headers = setHeaders();
        //바디에 추가할 데이터 저장
        ClovaRequest clovaRequest = getBody(universityRequest);
        //RequestEntity 생성
        RequestEntity<ClovaRequest> request = new RequestEntity<>(clovaRequest, headers, HttpMethod.POST, URI.create(univApiUrl));
        //restTemplate를 통해 responseEntity 생성
        ResponseEntity<ClovaResponse> response = restTemplate.exchange(request, ClovaResponse.class);

        String result = response.getBody().getResult().getMessage().getContent();
        
        //title과 content '\n'으로 분리
        StringTokenizer st = new StringTokenizer(result,"\n");
        //title의 경우 '제목 : '을 제거하기 위해 substring(5) 로 지정
        String title = st.nextToken().substring(5);

        //content 내용 추가
        StringBuilder contentBuilder = new StringBuilder();
        while(st.hasMoreTokens()) {
            contentBuilder.append(st.nextToken() + "\n");
        }
        String content = contentBuilder.toString();

        return new UniversityResponse(title ,content);
    }

    private HttpHeaders setHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-NCP-CLOVASTUDIO-API-KEY", clovaStudioApiKey);
        httpHeaders.add("X-NCP-APIGW-API-KEY", univApiGatewayKey);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    private ClovaRequest getBody(UniversityRequest universityRequest) {
        ClovaTask systemTask = new ClovaTask("system", "");
        ClovaTask userTask = new ClovaTask("user", setContent(universityRequest));
        List<ClovaTask> tasks = new ArrayList<>();
        tasks.add(systemTask);
        tasks.add(userTask);
        return ClovaRequest.builder()
                .messages(tasks)
                .seed(0)
                .build();
    }

    private String setContent(UniversityRequest universityRequest){
        //프론트엔드에서 데이터를 공백으로 입력할 경우 포함하지 않기 위해 if문 추가
        StringBuilder sb = new StringBuilder();

        if(!universityRequest.sender().isEmpty()) sb.append("이름 : " + universityRequest.sender()+"\n");
        if(!universityRequest.receiver().isEmpty()) sb.append("교수님 성함 : " + universityRequest.receiver()+"\n");
        if(!universityRequest.department().isEmpty()) sb.append("학부 : " + universityRequest.department()+"\n");
        if(!universityRequest.studentId().isEmpty()) sb.append("학번 : " + universityRequest.studentId()+"\n");
        if(!universityRequest.subject().isEmpty()) sb.append("과목명 : " + universityRequest.subject()+"\n");
        if(!universityRequest.content().isEmpty()) sb.append(universityRequest.content()+"\n");
        //title과 content 분리의 편의성 위해 추가
        sb.append("제목 앞에는 '제목 : '을 붙여 주고, 제목과 내용은 엔터로 구분지어 주세요. 문단 별로 엔터 두 번을 이용하여 구분지어 작성해 주세요.");

        //보낼 데이터 정의
        String contentData = sb.toString();

        return contentData;
    }
}

