package ru.nsu.ccfit.muratov.distributed.crack.manager.repository;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class WorkerPool {
    private final List<Worker> workers = new ArrayList<>();

    public void addWorker(String hostname) {
        workers.add(new Worker(hostname));
    }

    public Worker removeWorker(String hostname) {
        for(int index = 0; index < workers.size(); index++) {
            Worker worker = workers.get(index);
            if(worker.getHostname().equals(hostname)) {
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
