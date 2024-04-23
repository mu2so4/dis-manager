package ru.nsu.ccfit.muratov.distributed.crack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nsu.ccfit.muratov.distributed.crack.dto.CrackRequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.dto.CrackResponseDto;
import ru.nsu.ccfit.muratov.distributed.crack.dto.StatusDto;
import ru.nsu.ccfit.muratov.distributed.crack.service.CrackService;

@RestController
public class RequestController {
    @Autowired
    private CrackService service;

    @PostMapping(value = "/api/hash/crack", consumes = "application/json", produces = "application/json")
    public CrackResponseDto createRequest(@RequestBody CrackRequestDto request) {
        return service.createCrackRequest(request);
    }


    @GetMapping(value = "/api/hash/status", consumes = "application/json", produces = "application/json")
    public StatusDto getStatus(@RequestParam(name="requestId") String id) {
        return service.getCrackStatus(id);
    }
}
