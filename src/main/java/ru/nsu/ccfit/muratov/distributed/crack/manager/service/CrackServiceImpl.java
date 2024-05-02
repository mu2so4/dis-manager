package ru.nsu.ccfit.muratov.distributed.crack.manager.service;

import com.mongodb.MongoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.muratov.distributed.crack.manager.repository.RequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@EnableScheduling
public class CrackServiceImpl implements CrackService {
    private List<Request> database = new ArrayList<>();

    private boolean isRequestNew = true;

    Logger logger = Logger.getLogger(CrackServiceImpl.class.getCanonicalName());

    @Autowired
    private RequestRepository repository;

    @Scheduled(fixedRate = 1000)
    private synchronized void syncData() {
        try {
            database = repository.findAll();
        }
        catch(MongoException e) {
            logger.severe(e::getMessage);
        }
    }

    @Override
    public synchronized Request createCrackRequest(String hash, int maxLength) {
        for(Request request: database) {
            if(maxLength <= request.getMaxLength() && hash.equals(request.getHash())) {
                isRequestNew = false;
                return request;
            }
        }

        isRequestNew = true;
        Request request = new Request();
        request.setRequestId(UUID.randomUUID().toString());
        request.setHash(hash);
        request.setStatus(RequestStatus.IN_PROGRESS);
        request.setMaxLength(maxLength);
        database.add(request);
        return repository.save(request);
    }

    @Override
    public synchronized Request getRequest(String requestId) {
        for(Request request: database) {
            if(requestId.equals(request.getRequestId())) {
                return request;
            }
        }
        return null;
    }

    @Override
    public synchronized void updateRequest(String requestId, String[] data) {
        Request request = getRequest(requestId);
        if(data != null) {
            request.setWords(data);
            request.setStatus(RequestStatus.READY);
        }
        else {
            request.setStatus(RequestStatus.ERROR);
        }
        repository.save(request);
    }

    @Override
    public boolean isRequestNew() {
        return isRequestNew;
    }

    @Override
    public synchronized void deleteAll() {
        repository.deleteAll();
    }
}
