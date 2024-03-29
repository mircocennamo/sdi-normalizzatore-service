package it.interno.normalizzatore.dto.divitech;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ListRoad {

    @JsonProperty("RoadDetails")
    private List<RoadDetails> roadDetails;

}
