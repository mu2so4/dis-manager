package ru.nsu.ccfit.muratov.distributed.crack.manager.service;

import ru.nsu.ccfit.muratov.distributed.crack.manager.repository.Worker;

public interface WorkerService {
    boolean addWorker(String hostname);
    Worker deleteWorker(String hostname);

    Worker getIdleWorker();
    Worker getWorker(String hostname);
    void freeWorker(String hostname);
}
