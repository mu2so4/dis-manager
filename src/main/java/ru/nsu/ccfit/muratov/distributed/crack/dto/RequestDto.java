package ru.nsu.ccfit.muratov.distributed.crack.dto;

import lombok.Data;

@Data
public class RequestDto {
    private String hash;
    private int maxLength;
}
