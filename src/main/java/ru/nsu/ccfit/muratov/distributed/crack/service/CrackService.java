package ru.nsu.ccfit.muratov.distributed.crack.service;

import ru.nsu.ccfit.muratov.distributed.crack.dto.CrackRequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.dto.CrackResponseDto;
import ru.nsu.ccfit.muratov.distributed.crack.dto.StatusDto;

public interface CrackService {
    CrackResponseDto createCrackRequest(CrackRequestDto request);

    StatusDto getCrackStatus(String requestId);
}
