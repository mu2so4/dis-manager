package ru.nsu.ccfit.muratov.distributed.crack.manager.service;

import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackRequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackResponseDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.StatusDto;

public interface CrackService {
    CrackResponseDto createCrackRequest(CrackRequestDto request);

    StatusDto getCrackStatus(String requestId);
}
