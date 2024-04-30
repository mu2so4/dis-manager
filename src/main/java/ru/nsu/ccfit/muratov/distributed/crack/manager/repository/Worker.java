package ru.nsu.ccfit.muratov.distributed.crack.manager.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Worker {
    private final String id;
    private final String hostname;
    @Setter
    private WorkerStatus status = WorkerStatus.IDLE;

    public Worker(String id, String hostname) {
        this.id = id;
        this.hostname = hostname;
    }
}
