package org.dive2025.qdeep.common.infra.gpt.dto;

import java.util.ArrayList;
import java.util.List;


public record GptRequest(String model, List<Message> messages) {

    public GptRequest(String model,String prompt){
        this(model,new ArrayList<>(List.of(new Message("user",prompt))));
    }

}
