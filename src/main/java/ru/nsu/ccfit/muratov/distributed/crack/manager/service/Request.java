package ru.nsu.ccfit.muratov.distributed.crack.manager.service;

import lombok.Data;

@Data
public class Request {
    private String hash;
    private int maxLength;
    private RequestStatus status;
    private String[] words;
}
