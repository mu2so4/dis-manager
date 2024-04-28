package ru.nsu.ccfit.muratov.distributed.crack.manager.service;

public interface CrackService {
    String createCrackRequest(String hash, int maxLength);

    Request getCrackStatus(String requestId);
}
