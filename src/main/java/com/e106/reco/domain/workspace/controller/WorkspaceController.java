package com.e106.reco.domain.workspace.controller;

import com.e106.reco.domain.workspace.dto.WorkspaceDetailResponse;
import com.e106.reco.domain.workspace.dto.WorkspaceRequest;
import com.e106.reco.domain.workspace.dto.WorkspaceResponse;
import com.e106.reco.domain.workspace.dto.midify.ModifyPoint;
import com.e106.reco.domain.workspace.dto.midify.ModifyState;
import com.e106.reco.domain.workspace.service.WorkspaceService;
import com.e106.reco.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @PostMapping("/artists/{artistSeq}/workspaces")
    public ResponseEntity<Long> createWorkspace(@PathVariable Long artistSeq,
                                                @RequestBody WorkspaceRequest workspaceRequest) {
        return ResponseEntity.ok(workspaceService.create(artistSeq, workspaceRequest));
    }

    @PostMapping("/artists/{artistSeq}/workspaces/divide")
    public ResponseEntity<Long> divide(@PathVariable Long artistSeq,
                                       @RequestBody WorkspaceRequest workspaceRequest,
                                       @RequestParam(name = "sound") MultipartFile sound) {
        return ResponseEntity.ok(workspaceService.divide(artistSeq, workspaceRequest, sound));
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

    @GetMapping("/workspaces/{workspaceSeq}")
    public ResponseEntity<Long> fork(@PathVariable Long workspaceSeq) {
        return ResponseEntity.ok(workspaceService.fork(workspaceSeq));
    }


    @PostMapping("/workspaces/{workspaceSeq}/thumbnail")
    public ResponseEntity<CommonResponse> modifyThumbnail(@PathVariable Long workspaceSeq,
                                                          @RequestParam(value = "file", required = false) MultipartFile file) {
        return workspaceService.modifyThumbnail(workspaceSeq, file);
    }

    @PostMapping("/workspaces/{workspaceSeq}/thumbnail")
    public ResponseEntity<CommonResponse> modifyPoint(@PathVariable Long workspaceSeq,
                                                      @RequestBody List<ModifyPoint> modifyPoints) {
        return ResponseEntity.ok(workspaceService.modifyPoint(workspaceSeq, modifyPoints));
    }

    @PostMapping("/workspaces/{workspaceSeq}/thumbnail")
    public ResponseEntity<CommonResponse> modifyState(@PathVariable Long workspaceSeq,
                                                      @RequestBody ModifyState modifyState) {
        return ResponseEntity.ok(workspaceService.modifyState(workspaceSeq, modifyState));
    }

}
