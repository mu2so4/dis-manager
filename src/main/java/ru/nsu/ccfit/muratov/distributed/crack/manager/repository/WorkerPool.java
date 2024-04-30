package ru.nsu.ccfit.muratov.distributed.crack.manager.repository;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class WorkerPool {
    private final List<Worker> workers = new ArrayList<>();

    public void addWorker(String hostname) {
        workers.add(new Worker(UUID.randomUUID().toString(), hostname));
    }

    public Worker removeWorker(String id) {
        for(int index = 0; index < workers.size(); index++) {
            Worker worker = workers.get(index);
            if(worker.getId().equals(id)) {
                return workers.remove(index);
            }
        }
        return null;
    }

    public Worker getWorker(String hostname) {
        for(Worker worker: workers) {
            if(worker.getHostname().equals(hostname)) {
                return worker;
            }
        }
        return null;
    }
}
