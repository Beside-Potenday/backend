package alphamail.com.backend.common.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ClovaRequest {
    private List<ClovaTask> messages;
    private final double topP = 0.8;
    private final int topK = 0;
    private final int maxTokens = 350;
    private final double temperature = 0.7;
    private final double repeatPenalty = 5;
    private final List<String> stopBefore = new ArrayList<>();
    private final boolean includeAiFilters = true;
    private int seed;
}
