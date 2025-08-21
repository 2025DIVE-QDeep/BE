package org.dive2025.qdeep.common.infra.gpt.dto;

import lombok.Getter;

import java.util.List;

public record GptResponse(List<Choice> choices) {

    public record Choice(int index,Message message){

    }
}
