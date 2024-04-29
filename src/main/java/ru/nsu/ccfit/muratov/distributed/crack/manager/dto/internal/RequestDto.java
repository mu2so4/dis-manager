package ru.nsu.ccfit.muratov.distributed.crack.manager.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestDto {
    private String requestId;
    private String hash;
    private int maxLength;
}
