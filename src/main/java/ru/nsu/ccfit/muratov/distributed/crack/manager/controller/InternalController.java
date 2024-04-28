package ru.nsu.ccfit.muratov.distributed.crack.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.internal.ResponseDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.CrackService;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.Request;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.RequestStatus;

@RestController
@RequestMapping(value="/internal/api/manager/hash/crack")
public class InternalController {
    @Autowired
    private CrackService service;


    @PatchMapping(value = "/request")
    public void getResult(@RequestBody ResponseDto response) {
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
