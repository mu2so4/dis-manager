package ru.nsu.ccfit.muratov.distributed.crack.manager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.ccfit.muratov.distributed.crack.manager.service.Request;

@Repository
public interface RequestRepository extends MongoRepository<Request, String> { }
