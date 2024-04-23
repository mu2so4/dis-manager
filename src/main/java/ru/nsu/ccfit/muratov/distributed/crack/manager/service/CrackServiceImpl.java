package ru.nsu.ccfit.muratov.distributed.crack.manager.service;

import org.springframework.stereotype.Service;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackRequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackResponseDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.StatusDto;

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
