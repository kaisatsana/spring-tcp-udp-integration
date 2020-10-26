package edu.exam.common.models;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonTypeName("error")
@EqualsAndHashCode(callSuper = true)
public class Error extends Content {
    @NonNull
    private String errorMessage;
}