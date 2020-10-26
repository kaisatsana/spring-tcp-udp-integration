package edu.exam.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Histories extends Content {
    private final List<History> histories = new ArrayList<>();

    public void add(History history) {
        histories.add(history);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return histories.isEmpty();
    }
}
