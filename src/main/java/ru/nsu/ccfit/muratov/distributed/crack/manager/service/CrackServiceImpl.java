package ru.nsu.ccfit.muratov.distributed.crack.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.muratov.distributed.crack.manager.repository.RequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CrackServiceImpl implements CrackService {
    private final List<Request> database = new ArrayList<>();

    private boolean isRequestNew = true;

    @Autowired
    private RequestRepository repository;

    @Override
    public Request createCrackRequest(String hash, int maxLength) {
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
    public Request getRequest(String requestId) {
        for(Request request: database) {
            if(requestId.equals(request.getRequestId())) {
                return request;
            }
        }
        return null;
    }

    @Override
    public void updateRequest(String requestId, String[] data) {
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
    public void deleteAll() {
        repository.deleteAll();
    }
}
