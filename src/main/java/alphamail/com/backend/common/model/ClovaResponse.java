package alphamail.com.backend.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClovaResponse {

    @JsonProperty
    private Result result;
}
