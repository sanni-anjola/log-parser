package com.example.logparser.config;

import com.example.logparser.spring.dto.LogFileRequest;
import com.example.logparser.spring.model.Log;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.ClassPathResource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<LogFileRequest> itemReader,
                   ItemProcessor<LogFileRequest, Log> itemProcessor,
                   ItemWriter<Log> itemWriter) {
        Step step = stepBuilderFactory.get("Access-log-load")
                .<LogFileRequest, Log>chunk(10000)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        return jobBuilderFactory.get("Access-Log")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public FlatFileItemReader<LogFileRequest> itemReader(){

        return new FlatFileItemReaderBuilder<LogFileRequest>()
                .name("personItemReader")
                .resource(new ClassPathResource("access.log"))
                .delimited()
                .names("date", "ip", "method", "response", "userAgent")
                .lineMapper(linemapper())
                .build();
    }

    @Bean
    public LineMapper<LogFileRequest> linemapper() {
        DefaultLineMapper<LogFileRequest> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("|");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("date", "ip", "method", "response", "userAgent");
        BeanWrapperFieldSetMapper<LogFileRequest> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(LogFileRequest.class);
        fieldSetMapper.setConversionService(createConversionService());
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

    public ConversionService createConversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        DefaultConversionService.addDefaultConverters(conversionService);
        conversionService.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String text) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                return LocalDateTime.parse(text, formatter);
            }
        });
        return conversionService;
    }
}
