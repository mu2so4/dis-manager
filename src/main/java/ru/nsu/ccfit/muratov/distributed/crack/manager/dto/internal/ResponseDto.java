package ru.nsu.ccfit.muratov.distributed.crack.manager.dto.internal;

import lombok.Data;

@Data
public class ResponseDto {
    private String requestId;
    private String[] data;
}
