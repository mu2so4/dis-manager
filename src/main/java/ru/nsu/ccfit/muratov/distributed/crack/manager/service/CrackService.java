package ru.nsu.ccfit.muratov.distributed.crack.manager.service;

public interface CrackService {
    Request createCrackRequest(String hash, int maxLength);

    Request getRequest(String requestId);

    void updateRequest(String requestId, String[] data);
}
