package ru.nsu.ccfit.muratov.distributed.crack.manager.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CrackServiceImpl implements CrackService {
    Map<String, Request> database = new HashMap<>();

    @Override
    public String createCrackRequest(String hash, int maxLength) {
        //todo insert search of already requested later hashes
        String requestId = UUID.randomUUID().toString();
        Request request = new Request();
        request.setHash(hash);
        request.setStatus(RequestStatus.IN_PROGRESS);
        request.setMaxLength(maxLength);
        database.put(requestId, request);
        return requestId;
    }

    @Override
    public Request getCrackStatus(String requestId) {
        return database.get(requestId);
    }
}
