package com.example.logparser.batch;

import com.example.logparser.spring.model.Log;
import com.example.logparser.spring.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DBWriter implements ItemWriter<Log> {
    private final LogRepository logRepository;
    @Override
    public void write(List<? extends Log> list) {
        log.info("Data saved to Logs: {}", list );
        logRepository.saveAll(list);
    }
}
