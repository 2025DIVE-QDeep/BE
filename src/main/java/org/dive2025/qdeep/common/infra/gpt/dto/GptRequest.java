package org.dive2025.qdeep.common.infra.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public record GptRequest(String model,
                         String input) {


}