package ru.nsu.ccfit.muratov.distributed.crack.service;

import org.springframework.stereotype.Service;
import ru.nsu.ccfit.muratov.distributed.crack.dto.CrackRequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.dto.CrackResponseDto;
import ru.nsu.ccfit.muratov.distributed.crack.dto.StatusDto;

@Service
public class CrackServiceImpl implements CrackService {
    @Override
    public CrackResponseDto createCrackRequest(CrackRequestDto request) {
        return null;
    }

    @Override
    public StatusDto getCrackStatus(String requestId) {
        return null;
    }
}
