package ru.nsu.ccfit.muratov.distributed.crack.manager.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.internal.ResponseDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.CrackService;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.Request;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.RequestStatus;

import java.util.logging.Logger;

@Service
public class InternalController {
    @Autowired
    private CrackService service;

    private static final Logger logger = Logger.getLogger(InternalController.class.getCanonicalName());


    @RabbitListener(queues = "${rabbitmq.response.queue.name}")
    public void getResult(@RequestBody ResponseDto response) {
        logger.info(() -> "got response for request " + response.getRequestId());
        Request request = service.getCrackStatus(response.getRequestId());
        String[] data = response.getData();
        if(data != null) {
            request.setWords(data);
            request.setStatus(RequestStatus.READY);
        }
        else {
            request.setStatus(RequestStatus.ERROR);
        }
    }
}
