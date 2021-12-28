package com.example.logparser.batch;

import com.example.logparser.spring.dto.LogFileRequest;
import com.example.logparser.spring.model.Log;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class Processor implements ItemProcessor<LogFileRequest, Log> {
    @Override
    public Log process(LogFileRequest logFileRequest) throws Exception {

        return Log
                .builder()
                .date(logFileRequest.getDate())
                .ip(logFileRequest.getIp())
                .method(logFileRequest.getMethod())
                .response(logFileRequest.getResponse())
                .userAgent(logFileRequest.getUserAgent())
                .build();
    }
}
