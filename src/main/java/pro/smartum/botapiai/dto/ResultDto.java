package pro.smartum.botapiai.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResultDto {

    private final String resolvedQuery;
    private final List<ContextsDto> contexts;
}
