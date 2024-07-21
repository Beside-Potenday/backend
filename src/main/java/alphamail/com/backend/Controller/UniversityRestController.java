package alphamail.com.backend.Controller;

import alphamail.com.backend.Service.UniversityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnivRestController {
    private final UniversityService universityService;

    public UnivRestController(UniversityService universityService){
        this.universityService = universityService;
    }

    @GetMapping
    public ResponseEntity<?> test(){

    }
}
