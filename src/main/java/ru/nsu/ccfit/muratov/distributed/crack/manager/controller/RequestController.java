package ru.nsu.ccfit.muratov.distributed.crack.manager.controller;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackRequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackResponseDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.StatusDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.internal.RequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.Request;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.RequestStatus;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.CrackService;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/hash")
@EnableScheduling
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

    private final Queue<RequestDto> unsentRequests = new LinkedList<>();

    @PostMapping(value = "/crack", consumes = "application/json", produces = "application/json")
    public CrackResponseDto createRequest(@RequestBody CrackRequestDto request) {
        logger.info("got request " + request.toString());
        String hash = request.getHash();
        int maxLength = request.getMaxLength();

        Request requestRecord = service.createCrackRequest(hash, maxLength);
        String requestId = requestRecord.getRequestId();
        RequestDto internalDto = new RequestDto(requestId, hash, maxLength);
        if(service.isRequestNew()) {
            sendMessage(internalDto, true);
        }

        return new CrackResponseDto(requestId);
    }

    private boolean sendMessage(RequestDto dto, boolean isFirstTry) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingJsonKey, dto);
        }
        catch (AmqpException e) {
            logger.severe(() -> "failed to send request: " + e.getMessage());
            if(isFirstTry) {
                synchronized (unsentRequests) {
                    unsentRequests.add(dto);
                }
            }
            return false;
        }
        return true;
    }

    @Scheduled(fixedRate = 10000)
    private void resendMessages() {
        while(!unsentRequests.isEmpty()) {
            RequestDto request = unsentRequests.peek();
            if(sendMessage(request, false)) {
                logger.info(() -> "request " + request + " resent successfully");
                unsentRequests.remove();
            }
            else {
                break;
            }
        }
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

    @DeleteMapping(value = "/crack")
    public void deleteDatabase() {
        service.deleteAll();
    }
}
