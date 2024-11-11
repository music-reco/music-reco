package com.e106.reco.domain.workspace.controller;

import com.e106.reco.domain.workspace.dto.TreeResponse;
import com.e106.reco.domain.workspace.dto.WorkspaceDetailResponse;
import com.e106.reco.domain.workspace.dto.WorkspaceRequest;
import com.e106.reco.domain.workspace.dto.WorkspaceResponse;
import com.e106.reco.domain.workspace.dto.midify.ModifyPoint;
import com.e106.reco.domain.workspace.dto.midify.ModifyState;
import com.e106.reco.domain.workspace.service.WorkspaceService;
import com.e106.reco.global.alarm.dto.FcmSendDto;
import com.e106.reco.global.alarm.service.FcmService;
import com.e106.reco.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class WorkspaceController {
    private final WorkspaceService workspaceService;
    private final FcmService fcmService;

    @PostMapping("/workspaces")
    public ResponseEntity<Long> createWorkspace(@RequestBody @Valid WorkspaceRequest workspaceRequest) {
        return ResponseEntity.ok(workspaceService.create(workspaceRequest));
    }


    @PostMapping("/workspace/divide")
    public ResponseEntity<CommonResponse> divideAudio(
            @RequestPart(value = "workspaceRequest") @Valid WorkspaceRequest workspaceRequest,
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "stemList") List<String> stemList,
            @RequestParam(value = "splitter", defaultValue = "phoenix") String splitter,
            @RequestParam(value = "fcmToken", required = false) String fcmToken){
        log.info("변환시작합니다.");
        log.info("file = {}", file.getOriginalFilename());
        log.info("file = {}", file.getContentType());
        workspaceService.divide(workspaceRequest, file, stemList, splitter)
                .thenAccept(results -> {
                    // FCM 알림 전송 등 모든 작업이 완료된 후의 처리
                    log.info("변환이 끝났습니다.");
                    fcmService.sendMessageTo(new FcmSendDto(fcmToken, "변환이 끝났어요.","워크 스페이스에서 확인하세요."));
                });

        return ResponseEntity.ok(new CommonResponse("Processing started"));
    }

    @PostMapping(value = "/workspaces/{workspaceSeq}/session")
    public ResponseEntity<CommonResponse> sessionCreate(@PathVariable Long workspaceSeq,
                                                        @RequestPart(value = "session") MultipartFile session){
        return ResponseEntity.ok(workspaceService.sessionCreate(workspaceSeq, session));
    }

    @DeleteMapping("/workspaces/{workspaceSeq}/session/{sessionSeq}")
    public ResponseEntity<CommonResponse> sessionDelete(@PathVariable Long workspaceSeq,
                                                        @PathVariable Long sessionSeq){
        return ResponseEntity.ok(workspaceService.sessionDelete(workspaceSeq, sessionSeq));
    }

    @PostMapping("/workspaces/{workspaceSeq}/session/{sessionSeq}")
    public ResponseEntity<CommonResponse> sessionImport(@PathVariable Long workspaceSeq,
                                                        @PathVariable Long sessionSeq){
        return ResponseEntity.ok(workspaceService.sessionImport(workspaceSeq, sessionSeq));
    }

    @GetMapping("/workspaces/{workspaceSeq}/tree")
    public ResponseEntity<List<TreeResponse>> getTree(@PathVariable Long workspaceSeq){
        return ResponseEntity.ok(workspaceService.getTree(workspaceSeq));
    }

    @GetMapping("/artists/{artistSeq}/workspaces")
    public ResponseEntity<List<WorkspaceResponse>> getAllWorkspaces(@PathVariable Long artistSeq,
                                                                    @PageableDefault(direction = Sort.Direction.DESC, sort = "seq") Pageable pageable) {
        return ResponseEntity.ok(workspaceService.getWorkspaceList(artistSeq, pageable));
    }

    @GetMapping("/workspaces/{workspaceSeq}")
    public ResponseEntity<WorkspaceDetailResponse> getWorkspaces(@PathVariable Long workspaceSeq) {
        return ResponseEntity.ok(workspaceService.getWorkspaceDetail(workspaceSeq));
    }

    @GetMapping("/workspaces/{workspaceSeq}/fork")
    public ResponseEntity<Long> fork(@PathVariable Long workspaceSeq) {
        return ResponseEntity.ok(workspaceService.fork(workspaceSeq));
    }


    @PostMapping("/workspaces/{workspaceSeq}/thumbnail")
    public ResponseEntity<CommonResponse> modifyThumbnail(@PathVariable Long workspaceSeq,
                                                          @RequestPart(value = "file") MultipartFile file) {
        return ResponseEntity.ok(workspaceService.modifyThumbnail(workspaceSeq, file));
    }

    @PostMapping("/workspaces/{workspaceSeq}/point")
    public ResponseEntity<CommonResponse> modifyPoint(@PathVariable Long workspaceSeq,
                                                      @RequestPart(value = "sessions") List<ModifyPoint> modifyPoints) {
        return ResponseEntity.ok(workspaceService.modifyPoint(workspaceSeq, modifyPoints));
    }
    
    @PostMapping("/workspaces/{workspaceSeq}/state")
    public ResponseEntity<CommonResponse> modifyState(@PathVariable Long workspaceSeq,
                                                      @RequestBody ModifyState modifyState) {
        return ResponseEntity.ok(workspaceService.modifyState(workspaceSeq, modifyState));
    }

}
