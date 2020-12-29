package guru.sfg.brewery.common.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateBeerOrderResult {
    private boolean validated;
    private UUID orderId;
}
