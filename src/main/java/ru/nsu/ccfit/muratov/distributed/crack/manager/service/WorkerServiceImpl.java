package ru.nsu.ccfit.muratov.distributed.crack.manager.service;

import org.springframework.stereotype.Service;
import ru.nsu.ccfit.muratov.distributed.crack.manager.repository.Worker;
import ru.nsu.ccfit.muratov.distributed.crack.manager.repository.WorkerPool;
import ru.nsu.ccfit.muratov.distributed.crack.manager.repository.WorkerStatus;

import java.util.NoSuchElementException;

@Service
public class WorkerServiceImpl implements WorkerService {
    private final WorkerPool pool = new WorkerPool();

    @Override
    public synchronized boolean addWorker(String hostname) {
        for(Worker worker: pool.getWorkers()) {
            if(worker.getHostname().equals(hostname)) {
                return false;
            }
        }
        pool.addWorker(hostname);
        return true;
    }

    @Override
    public synchronized Worker deleteWorker(String hostname) {
        //todo redirecting tasks from deleted worker
        return pool.removeWorker(hostname);
    }

    @Override
    public synchronized Worker getIdleWorker() {
        for(Worker worker: pool.getWorkers()) {
            if(worker.getStatus() == WorkerStatus.IDLE) {
                worker.setStatus(WorkerStatus.BUSY);
                return worker;
            }
        }
        return null;
    }

    @Override
    public synchronized void freeWorker(String hostname) {
        Worker worker = pool.getWorker(hostname);
        if(worker == null) {
            throw new NoSuchElementException("worker " + hostname + " not found");
        }
        worker.setStatus(WorkerStatus.IDLE);
    }
}
