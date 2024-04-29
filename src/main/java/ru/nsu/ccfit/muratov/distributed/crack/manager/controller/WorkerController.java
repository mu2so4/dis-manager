package ru.nsu.ccfit.muratov.distributed.crack.manager.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.PortDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.WorkerService;

import java.util.logging.Logger;

@RestController
@RequestMapping(value="/internal/api/manager/accounting")
public class WorkerController {
    private static final Logger logger = Logger.getLogger(WorkerController.class.getCanonicalName());

    @Autowired
    private WorkerService service;


    @PostMapping
    public void greet(HttpServletRequest request, @RequestBody PortDto dto) {
        String hostname = request.getRemoteHost();
        String fullHostname = hostname + ':' + dto.getPort();

        if(service.addWorker(fullHostname)) {
            logger.info("joined host " + fullHostname);
        }
        //todo sign in of worker
    }

    @DeleteMapping
    public void fare(HttpServletRequest request, @RequestBody PortDto dto) {
        String hostname = request.getRemoteHost();
        String fullHostname = hostname + ':' + dto.getPort();

        service.deleteWorker(fullHostname);

        logger.info("left host " + fullHostname);
        //todo sign out of worker
    }
}
