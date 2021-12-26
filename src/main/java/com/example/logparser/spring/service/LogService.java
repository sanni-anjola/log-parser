package com.example.logparser.spring.service;

import com.example.logparser.spring.dto.LogFileRequest;
import com.example.logparser.spring.model.Log;

import java.util.List;

public interface LogService {
    Log save(LogFileRequest logFileRequest);
    List<Log> saveAll(List<LogFileRequest> logFileRequests);
}
