package alphamail.com.backend.university.controller;

import alphamail.com.backend.university.model.UniversityRequest;
import alphamail.com.backend.university.model.UniversityResponse;
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

    @CrossOrigin("https://alpamail-frontend.vercel.app/")
    @PostMapping("/univ")
    public ResponseEntity<?> getUniv(@RequestBody UniversityRequest universityRequest) throws Exception{
        UniversityResponse response = universityService.getResult(universityRequest);

        return ResponseEntity.ok(response);
    }
}
