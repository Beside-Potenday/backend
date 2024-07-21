package alphamail.com.backend.university.controller;

import alphamail.com.backend.university.model.InputMessageDTO;
import alphamail.com.backend.university.model.OutputMessageDTO;
import alphamail.com.backend.university.service.UniversityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mails")
public class UniversityRestController {
    private final UniversityService universityService;

    public UniversityRestController(UniversityService universityService){
        this.universityService = universityService;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() throws Exception{
        InputMessageDTO inputMessageDTO = new InputMessageDTO("곽두팔",
                "",
                "컴퓨터학부",
                "2020000000",
                "대화형 프로그래밍 기초",
                "교수님꼐 성적 정정을 요청하는 메일을 작성해주세요.");

        OutputMessageDTO response = universityService.getResult(inputMessageDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/univ")
    public ResponseEntity<?> getUniv(@RequestBody InputMessageDTO inputMessageDTO) throws Exception{
        OutputMessageDTO response = universityService.getResult(inputMessageDTO);

        return ResponseEntity.ok(response);
    }
}
