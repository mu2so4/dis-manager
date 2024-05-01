package ru.nsu.ccfit.muratov.distributed.crack.manager.service;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Request")
public class Request {
    @Id
    private String requestId;
    private String hash;
    private int maxLength;
    private RequestStatus status;
    private String[] words;
}
