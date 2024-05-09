package ru.nsu.ccfit.muratov.distributed.crack.manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CrackRequestDto {
    @Schema(description = "hash to be cracked", example = "1edd5f66067229b7d2411cb1658074d6")
    private String hash;



    @Schema(description = "max length of words which may match the hash", example = "4")
    private int maxLength;
}
