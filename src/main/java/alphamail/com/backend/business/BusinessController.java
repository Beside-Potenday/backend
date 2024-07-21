package alphamail.com.backend.business;

import alphamail.com.backend.business.model.BusinessMailRequest;
import alphamail.com.backend.business.model.BusinessMailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping("/api/mails/business")
    public ResponseEntity<BusinessMailResponse> createBusinessMail(
        @RequestBody BusinessMailRequest businessMailRequest) {
        return ResponseEntity.ok(businessService.getBusinessMail(businessMailRequest));
    }
}
