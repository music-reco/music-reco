package com.e106.reco.domain.workspace.service;

import com.e106.reco.domain.workspace.dto.divide.CancelResponse;
import com.e106.reco.domain.workspace.dto.divide.CheckResponse;
import com.e106.reco.domain.workspace.dto.divide.FileResult;
import com.e106.reco.domain.workspace.dto.divide.LimitsResponse;
import com.e106.reco.domain.workspace.dto.divide.SplitParams;
import com.e106.reco.domain.workspace.dto.divide.SplitResponse;
import com.e106.reco.domain.workspace.dto.divide.TaskResult;
import com.e106.reco.domain.workspace.dto.divide.UploadResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LalalAiClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${spring.lalal.ai.license-key}")
    private String licenseKey;

    public UploadResponse uploadFile(File file) {
        log.debug("Uploading file: {}", file.getName());
        return restClient.post()
                .uri("/api/upload/")
                .header("Content-Disposition", "attachment; filename=" + file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(file))
                .retrieve()
                .body(UploadResponse.class);
    }

    public SplitResponse splitAudio(List<SplitParams> params) {
        try {
            log.debug("Requesting audio split for {} files", params.size());
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("params", objectMapper.writeValueAsString(params));

            return restClient.post()
                    .uri("/api/split/")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(SplitResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize split parameters", e);
        }
    }

    public CheckResponse checkStatus(List<String> fileIds) {
        log.debug("Checking status for files: {}", fileIds);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("id", String.join(",", fileIds));

        return restClient.post()
                .uri("/api/check/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(CheckResponse.class);
    }
    public FileResult splitAndWait(File file, SplitParams params,
                                   Duration checkInterval, Duration timeout) {
        log.info("Starting audio split process for file: {}", file.getName());
        Instant startTime = Instant.now();

        UploadResponse uploadResult = uploadFile(file);
        if (!"success".equals(uploadResult.getStatus())) {
            throw new RuntimeException("Upload failed: " + uploadResult.getError());
        }

        params.setId(uploadResult.getId());
        SplitResponse splitResult = splitAudio(Collections.singletonList(params));
        if (!"success".equals(splitResult.getStatus())) {
            throw new RuntimeException("Split failed: " + splitResult.getError());
        }

        return waitForCompletion(uploadResult.getId(), startTime, checkInterval, timeout);
    }
    public CancelResponse cancelTask(List<String> fileIds) {
        log.debug("Cancelling tasks for files: {}", fileIds);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("id", String.join(",", fileIds));

        return restClient.post()
                .uri("/api/cancel/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(CancelResponse.class);
    }

    public LimitsResponse getLimits() {
        log.debug("Fetching account limits");
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/billing/get-limits/")
                        .queryParam("key", licenseKey)
                        .build())
                .retrieve()
                .body(LimitsResponse.class);
    }

    private FileResult waitForCompletion(String fileId, Instant startTime,
                                         Duration checkInterval, Duration timeout) {
        while (true) {
            if (Duration.between(startTime, Instant.now()).compareTo(timeout) > 0) {
                throw new RuntimeException("Processing timeout exceeded");
            }

            CheckResponse status = checkStatus(Collections.singletonList(fileId));
            if (!"success".equals(status.getStatus())) {
                throw new RuntimeException("Status check failed: " + status.getError());
            }

            FileResult fileResult = status.getResult().get(fileId);
            if (fileResult != null && fileResult.getTask() != null) {
                TaskResult task = fileResult.getTask();

                switch (task.getState()) {
                    case "success":
                        log.info("Processing completed successfully");
                        return fileResult;
                    case "error":
                        throw new RuntimeException("Processing failed: " + task.getError());
                    case "progress":
                        log.debug("Processing progress: {}%", task.getProgress());
                        break;
                    case "cancelled":
                        throw new RuntimeException("Processing was cancelled");
                }
            }

            try {
                Thread.sleep(checkInterval.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Processing interrupted", e);
            }
        }

    }
}