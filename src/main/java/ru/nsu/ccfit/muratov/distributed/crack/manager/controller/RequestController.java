package ru.nsu.ccfit.muratov.distributed.crack.manager.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackRequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackResponseDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.StatusDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.internal.RequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.Request;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.RequestStatus;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.CrackService;

import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/hash")
public class RequestController {
    @Autowired
    private CrackService service;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.request.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.request.routing.key}")
    private String routingJsonKey;

    private static final Logger logger = Logger.getLogger(RequestController.class.getCanonicalName());

    @PostMapping(value = "/crack", consumes = "application/json", produces = "application/json")
    public CrackResponseDto createRequest(@RequestBody CrackRequestDto request) {
        logger.info("got request " + request.toString());
        String hash = request.getHash();
        int maxLength = request.getMaxLength();

        Request requestRecord = service.createCrackRequest(hash, maxLength);
        String requestId = requestRecord.getRequestId();
        RequestDto internalDto = new RequestDto(requestId, hash, maxLength);
        rabbitTemplate.convertAndSend(exchange, routingJsonKey, internalDto);

        return new CrackResponseDto(requestId);
    }


    @GetMapping(value = "/status", consumes = "application/json", produces = "application/json")
    public StatusDto getStatus(@RequestParam(name="requestId") String id) {
        Request request = service.getRequest(id);
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
