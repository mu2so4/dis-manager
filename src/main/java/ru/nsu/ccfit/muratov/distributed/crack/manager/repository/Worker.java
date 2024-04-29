package ru.nsu.ccfit.muratov.distributed.crack.manager.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Worker {
    private final String hostname;
    @Setter
    private WorkerStatus status = WorkerStatus.IDLE;

    public Worker(String hostname) {
        this.hostname = hostname;
    }
}
