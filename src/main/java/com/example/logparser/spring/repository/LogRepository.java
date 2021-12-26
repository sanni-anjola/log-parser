package com.example.logparser.spring.repository;

import com.example.logparser.spring.dto.LogFileRequest;
import com.example.logparser.spring.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {

}
