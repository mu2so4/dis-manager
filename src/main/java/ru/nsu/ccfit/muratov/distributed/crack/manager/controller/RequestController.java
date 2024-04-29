package ru.nsu.ccfit.muratov.distributed.crack.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackRequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackResponseDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.StatusDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.internal.RequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.repository.Worker;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.Request;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.RequestStatus;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.CrackService;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.WorkerService;

@RestController
@RequestMapping(value = "/api/hash")
public class RequestController {
    @Autowired
    private CrackService service;

    @Autowired
    private WorkerService workers;

    private void assignTask(RequestDto dto) {
        Worker worker = workers.getIdleWorker();
        String uriTemplate = "http://%s/internal/api/worker/hash/crack/task";
        String uri = String.format(uriTemplate, worker.getHostname());
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<RequestDto> request = new HttpEntity<>(dto);
        restTemplate.postForLocation(uri, request, RequestDto.class);
    }

    @PostMapping(value = "/crack", consumes = "application/json", produces = "application/json")
    public CrackResponseDto createRequest(@RequestBody CrackRequestDto request) {
        String hash = request.getHash();
        int maxLength = request.getMaxLength();
        String id = service.createCrackRequest(hash, maxLength);

        RequestDto internalDto = new RequestDto(id, hash, maxLength);
        assignTask(internalDto);

        //todo send id immediately generating
        return new CrackResponseDto(id);
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
