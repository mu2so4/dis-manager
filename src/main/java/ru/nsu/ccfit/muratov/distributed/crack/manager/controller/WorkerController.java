package ru.nsu.ccfit.muratov.distributed.crack.manager.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.PortDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.WorkerIdDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.repository.Worker;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.WorkerService;

import java.util.logging.Logger;

@RestController
@RequestMapping(value="/internal/api/manager/accounting")
public class WorkerController {
    private static final Logger logger = Logger.getLogger(WorkerController.class.getCanonicalName());

    @Autowired
    private WorkerService service;


    @PostMapping
    public WorkerIdDto greet(HttpServletRequest request, @RequestBody PortDto dto) {
        String hostname = request.getRemoteHost();
        String fullHostname = hostname + ':' + dto.getPort();

        if(service.addWorker(fullHostname)) {
            logger.info("joined host " + fullHostname);
        }
        String id = service.getWorker(fullHostname).getId();
        return new WorkerIdDto(id);
    }

    @DeleteMapping("/{workerId}")
    public void fare(@PathVariable(name = "workerId") String workerId) {
        Worker deletedWorker = service.deleteWorker(workerId);

        logger.info("left host " + deletedWorker.getHostname());
    }
}
