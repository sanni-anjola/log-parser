package com.example.logparser.spring.service;

import com.example.logparser.spring.dto.LogFileRequest;
import com.example.logparser.spring.model.Log;
import com.example.logparser.spring.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService{
    private final LogRepository logRepository;

    @Override
    public Log save(LogFileRequest logFileRequest) {
        return null;
    }

    @Override
    public List<Log> saveAll(List<LogFileRequest> logFileRequests) {
        return logRepository.saveAll(logFileRequests.stream()
                            .map(logFileRequest -> Log
                                    .builder()
                                    .date(logFileRequest.getDate())
                                    .ip(logFileRequest.getIp())
                                    .method(logFileRequest.getMethod())
                                    .response(logFileRequest.getResponse())
                                    .userAgent(logFileRequest.getUserAgent())
                                    .build())
                            .collect(Collectors.toList()));
    }
}
