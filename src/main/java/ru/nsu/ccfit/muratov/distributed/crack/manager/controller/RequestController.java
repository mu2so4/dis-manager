package ru.nsu.ccfit.muratov.distributed.crack.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackRequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackResponseDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.StatusDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.Request;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.RequestStatus;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.CrackService;

@RestController
@RequestMapping(value = "/api/hash")
public class RequestController {
    @Autowired
    private CrackService service;

    @PostMapping(value = "/crack", consumes = "application/json", produces = "application/json")
    public CrackResponseDto createRequest(@RequestBody CrackRequestDto request) {
        String id = service.createCrackRequest(request.getHash(), request.getMaxLength());
        CrackResponseDto dto = new CrackResponseDto();
        dto.setRequestId(id);
        return dto;
    }


    @GetMapping(value = "/status", consumes = "application/json", produces = "application/json")
    public StatusDto getStatus(@RequestParam(name="requestId") String id) {
        Request request = service.getCrackStatus(id);
        if(request == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no request found");
        }
        StatusDto dto = new StatusDto();
        RequestStatus status = request.getStatus();
        dto.setStatus(request.getStatus().toString());
        if(status == RequestStatus.READY) {
            dto.setData(request.getWords());
        }
        return dto;
    }
}
