package ru.nsu.ccfit.muratov.distributed.crack.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackRequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.CrackResponseDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.StatusDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.dto.internal.RequestDto;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.Request;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.RequestStatus;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.CrackService;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.Sender;

import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/hash")
@Tag(name = "Crack requests")
public class RequestController {
    @Autowired
    private CrackService service;

    @Autowired
    private Sender<RequestDto> sender;

    private static final Logger logger = Logger.getLogger(RequestController.class.getCanonicalName());

    @Operation(
            summary = "Create a new request to crack an MD5 hash",
            description = "Creates a new request to crack an MD5 hash. " +
                    "Note if there is a request with the same hash and equal or greater max length, " +
                    "the ID of that request will be returned."
    )
    @ApiResponses(
            @ApiResponse(
                    description = "Success",
                    responseCode = "201"
            )
    )
    @PostMapping(value = "/crack", consumes = "application/json", produces = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CrackResponseDto createRequest(@RequestBody CrackRequestDto request) {
        logger.info("got request " + request.toString());
        String hash = request.getHash();
        int maxLength = request.getMaxLength();

        Request requestRecord = service.createCrackRequest(hash, maxLength);
        String requestId = requestRecord.getRequestId();
        RequestDto internalDto = new RequestDto(requestId, hash, maxLength);
        if(service.isRequestNew()) {
            sender.sendMessage(internalDto);
        }

        return new CrackResponseDto(requestId);
    }


    @Operation(
            summary = "Get status of the crack request",
            description = "Gets status of the crack request by its ID"
    )
    @Parameter(
            name = "requestId",
            description = "ID of request created"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Success",
                    responseCode = "200"
            ),
            @ApiResponse(
                    description = "No request found",
                    responseCode = "404"
            )
    })
    @GetMapping(value = "/status", produces = "application/json")
    public StatusDto getStatus(@RequestParam(name="requestId") String id) {
        Request request = service.getRequest(id);
        if(request == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no request found");
        }
        StatusDto dto = new StatusDto();
        RequestStatus status = request.getStatus();
        dto.setStatus(request.getStatus().toString());
        if(status == RequestStatus.READY) {
            dto.setData(request.getWords());
        }
        return dto;
    }

    @Operation(
            summary = "Delete all request from the database",
            description = "Deletes all request from the database except in progress ones. This method is used in development purposes."
    )
    @ApiResponse(
            description = "Success",
            responseCode = "204"
    )
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/crack", produces = "text/plain")
    public String deleteDatabase() {
        service.deleteAll();
        logger.info("DB data cleared");
        return "Database data were deleted!";
    }
}
