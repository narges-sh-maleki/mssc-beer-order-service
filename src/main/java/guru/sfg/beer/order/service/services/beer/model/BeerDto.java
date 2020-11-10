package guru.sfg.beer.order.service.services.beer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto implements Serializable{


        static final long serialVersionUID = -7550131241292947998L;

        @JsonProperty("beerId")
        @Null
        private UUID id;
        @Null
        private Integer version;
        @Null
        @JsonFormat(shape =   JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
        private OffsetDateTime createdDate;
        @Null
        @JsonFormat(shape =   JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
        private OffsetDateTime lastModifiedDate;
        @NotBlank
        private String beerName;
        @NotNull
        private BeerStyleEnum beerStyle;

        @NotNull
        private String upc;
        @Positive
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private BigDecimal price;
        private Integer quantityOnHand;
    }


