package com.e106.reco.domain.workspace.service;

import com.e106.reco.domain.workspace.dto.divide.AccountLimitsResponse;
import com.e106.reco.domain.workspace.dto.divide.AudioDivideResponse;
import com.e106.reco.domain.workspace.dto.divide.FileResult;
import com.e106.reco.domain.workspace.dto.divide.LimitsResponse;
import com.e106.reco.domain.workspace.dto.divide.SplitParams;
import com.e106.reco.domain.workspace.entity.converter.SplitterType;
import com.e106.reco.domain.workspace.entity.converter.StemType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class DivideService {
    private final LalalAiClient lalalAiClient;
    private Path tempDir;

    @PostConstruct
    void init() {
        try {
            tempDir = Files.createTempDirectory("audio_divide_");
            log.info("Created temporary directory: {}", tempDir);
            log.info("tempDir: {} ", System.getProperty("java.io.tmpdir"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp directory", e);
        }
    }

    @Async(value = "asyncExecutor1")
    public CompletableFuture<AudioDivideResponse> divideAudioFile(
            File file, String contentType, String stemValue, String splitterValue) {
        log.info("Processing file: {} with contentType: {}", file.getName(), contentType);
//        validateAudioFile(file, contentType);
        validateStemAndSplitter(stemValue, splitterValue);

        try {
            SplitParams params = SplitParams.builder()
                    .stem(stemValue)
                    .splitter(splitterValue)
                    .enhancedProcessingEnabled(true)
                    .build();

            FileResult result = lalalAiClient.splitAndWait(
                    file,
                    params,
                    Duration.ofSeconds(2),
                    Duration.ofMinutes(5)
            );

            return CompletableFuture.completedFuture(convertToResponse(result));
        } catch (Exception e) {
            log.error("Error processing file: {}", file.getName(), e);
            throw new RuntimeException("Failed to process audio file: " + e.getMessage());
        }
    }

    public AccountLimitsResponse getAccountLimits() {
        LimitsResponse limits = lalalAiClient.getLimits();
        return AccountLimitsResponse.builder()
                .option(limits.getOption())
                .email(limits.getEmail())
                .totalDurationLimit(limits.getProcessDurationLimit())
                .usedDuration(limits.getProcessDurationUsed())
                .remainingDuration(limits.getProcessDurationLeft())
                .build();
    }

    private void validateStemAndSplitter(String stemValue, String splitterValue) {
        StemType stem = StemType.fromString(stemValue);
        SplitterType splitter = SplitterType.valueOf(splitterValue.toUpperCase());

        if (!stem.isSupportedBy(splitter)) {
            throw new RuntimeException(
                    String.format("Stem type '%s' is not supported by splitter '%s'",
                            stem.getValue(), splitter.name().toLowerCase())
            );
        }
    }

    private void validateAudioFile(File file, String contentType) {
        if (!file.exists() || file.length() == 0) {
            throw new RuntimeException("Empty file provided");
        }

        // 파일 크기 체크
        long maxSize = 2L * 1024 * 1024 * 1024; // 2GB
        if (file.length() > maxSize) {
            throw new RuntimeException("File size exceeds maximum limit of 2GB");
        }
        log.info("contentType : {}", contentType);
        // Content-Type이 없는 경우 파일 확장자로 체크
        if (contentType == null) {
            String fileName = file.getName().toLowerCase();
            boolean isAudioFile = fileName.endsWith(".mp3") ||
                    fileName.endsWith(".wav") ||
                    fileName.endsWith(".m4a") ||
                    fileName.endsWith(".aac") ||
                    fileName.endsWith(".wma") ||
                    fileName.endsWith(".ogg");

            if (!isAudioFile) {
                throw new RuntimeException("Invalid file type. Only audio files are allowed");
            }
        } else {
            // Content-Type으로 체크
            if (!contentType.startsWith("audio_")) {
                throw new RuntimeException("Invalid content type. Only audio files are allowed");
            }
        }
    }

    private File createTempFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ?
                originalFilename.substring(originalFilename.lastIndexOf(".")) : ".tmp";

        File tempFile = tempDir.resolve(UUID.randomUUID() + extension).toFile();
        file.transferTo(tempFile);
        return tempFile;
    }

    private AudioDivideResponse convertToResponse(FileResult result) {
        if (result.getSplit() == null) {
            throw new RuntimeException("Split result not available");
        }

        return AudioDivideResponse.builder()
                .fileName(result.getName())
                .duration(result.getSplit().getDuration())
                .stemTrackUrl(result.getSplit().getStemTrack())
                .stemTrackSize(result.getSplit().getStemTrackSize())
                .backTrackUrl(result.getSplit().getBackTrack())
                .backTrackSize(result.getSplit().getBackTrackSize())
                .build();
    }
}