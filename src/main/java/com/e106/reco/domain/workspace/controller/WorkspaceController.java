package com.e106.reco.domain.workspace.controller;

import com.e106.reco.domain.workspace.dto.TreeResponse;
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
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @PostMapping("/workspaces")
    public ResponseEntity<Long> createWorkspace(@RequestBody WorkspaceRequest workspaceRequest) {
        return ResponseEntity.ok(workspaceService.create(workspaceRequest));
    }


    @PostMapping("/workspaces/divide")
    public ResponseEntity<CommonResponse> divide(@RequestBody WorkspaceRequest workspaceRequest,
                                                 @RequestParam(name = "sound") MultipartFile sound) {
        return ResponseEntity.ok(workspaceService.divide(workspaceRequest, sound));
    }

    @PostMapping("/workspaces/{workspaceSeq}/session")
    public ResponseEntity<CommonResponse> sessionCreate(@PathVariable Long workspaceSeq,
                                                        @RequestParam MultipartFile session){
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
                                                          @RequestParam(value = "file") MultipartFile file) {
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
