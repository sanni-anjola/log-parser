package com.example.logparser.spring.controller;

import com.example.logparser.spring.dto.LogFileRequest;
import com.example.logparser.spring.service.LogService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class UploadController {
    private final LogService logService;
    @PostMapping("/upload-access-log")
    public ResponseEntity<?> uploadAccessLog(@RequestParam(value = "file", required = false) MultipartFile file) {
        if(file == null){
            try {
                File file1 = ResourceUtils.getFile("classpath:access.log");
                return saveFileToDB(file1);
            }catch (FileNotFoundException ex){
                log.error("No file found");
                return ResponseEntity.ok().body("Please load a file");
            }
        }
        return saveFileToDB(file);
    }

    private ResponseEntity<String> saveFileToDB(MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            return getStringResponseEntity(reader);
        }catch (IOException ex ){
            return ResponseEntity.unprocessableEntity().body("An error occurred while processing the CSV file");
        }
    }

    private ResponseEntity<String> saveFileToDB(File file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
            return getStringResponseEntity(reader);
        }catch (IOException ex ){
            return ResponseEntity.unprocessableEntity().body("An error occurred while processing the CSV file");
        }
    }

    private ResponseEntity<String> getStringResponseEntity(Reader reader) {
        CsvToBean<LogFileRequest> csvToBean = new CsvToBeanBuilder<LogFileRequest>(reader)
                .withSeparator('|')
                .withType(LogFileRequest.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        List<LogFileRequest> logs = csvToBean.parse();
        logService.saveAll(logs);
        return ResponseEntity.ok().body("File uploaded successfully");
    }
}
