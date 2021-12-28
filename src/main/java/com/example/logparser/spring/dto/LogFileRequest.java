package com.example.logparser.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogFileRequest {
    @CsvBindByPosition(position = 0)
    @CsvDate("yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime date;
    @CsvBindByPosition(position = 1)
    private String ip;
    @CsvBindByPosition(position = 2)
    private String method;
    @CsvBindByPosition(position = 3)
    private String response;
    @CsvBindByPosition(position = 4)
    private String userAgent;
}
