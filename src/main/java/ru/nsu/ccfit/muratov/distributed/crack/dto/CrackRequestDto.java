package ru.nsu.ccfit.muratov.distributed.crack.dto;

import lombok.Data;

@Data
public class CrackRequestDto {
    private String hash;
    private int maxLength;
}
