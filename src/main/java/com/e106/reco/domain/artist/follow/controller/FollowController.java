package com.e106.reco.domain.artist.follow.controller;

import com.e106.reco.domain.artist.follow.dto.FollowRequest;
import com.e106.reco.domain.artist.follow.dto.FollowResponse;
import com.e106.reco.domain.artist.follow.service.FollowService;
import com.e106.reco.global.auth.token.service.TokenService;
import com.e106.reco.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
	private final FollowService followService;

	// 팔로우 추가
	@PostMapping
	public ResponseEntity<CommonResponse> addFollow(@RequestBody @Valid FollowRequest followRequest) {
//		Long tokenFanSeq = tokenService.getUserDto(request).getSeq();
//
//		// 토큰의 사용자 ID와 요청의 fanSeq 비교
//		if (!tokenFanSeq.equals(fanSeq)) {
//			return ResponseEntity.status(403).body(new CommonResponse("팔로우 권한이 없습니다."));
//		}
		return ResponseEntity.ok(followService.addFollow(followRequest));
	}

	@DeleteMapping
	public ResponseEntity<CommonResponse> removeFollow(@RequestParam Long targetSeq) {
		return ResponseEntity.ok(followService.removeFollow(targetSeq));
	}
	@GetMapping("/target")
	public ResponseEntity<List<FollowResponse>> getTarget(){
		return ResponseEntity.ok(followService.getMyTarget());
	}

	@GetMapping("/fan")
	public ResponseEntity<List<FollowResponse>> getFan(){
		return ResponseEntity.ok(followService.getMyFan());
	}

	// 팔로우 취소
//	@DeleteMapping("")
//	public ResponseEntity<CommonResponse> removeFollow(@RequestParam Long targetSeq,
//		@RequestParam Long fanSeq
//		//                                                       @Aut`henticationPrincipal UserDetails userDetails,
////                                                       HttpServletRequest request) {
//		//        Long tokenFanSeq = Long.parseLong(userDetails.getUsername());
////		Long tokenFanSeq = tokenService.getUserDto(request).getSeq();
//
//		// 토큰의 사용자 ID와 요청의 fanSeq 비교
//		if (!tokenFanSeq.equals(fanSeq)) {
//			return ResponseEntity.status(403).body(new CommonResponse("팔로우 권한이 없습니다."));
//		}
////		        return ResponseEntity.noContent().build();
//		return ResponseEntity.ok(followService.removeFollow(targetSeq, fanSeq));
//	}

	// 팔로우 관계 조회 (팔로워와 팔로잉 조회 구분)
//	@GetMapping("")
//	public ResponseEntity<Optional<Follow>> getFollow(@RequestParam Long targetSeq,
//		@RequestParam Long fanSeq
//                                                      @RequestParam String followType,   // "follower" 또는 "following"
//		//                                                      @AuthenticationPrincipal UserDetails userDetails,
//		HttpServletRequest request) {
//		//        Long tokenUserSeq = Long.parseLong(userDetails.getUsername());
//		Long tokenUserSeq = tokenService.getUserDto(request).getSeq();
//
//
//		// followType 에 따라 토큰의 사용자 Seq와 요청의 targetSeq 또는 fanSeq 비교
//		if (("follower".equalsIgnoreCase(followType) && !tokenUserSeq.equals(targetSeq)) ||
//			("following".equalsIgnoreCase(followType) && !tokenUserSeq.equals(fanSeq))) {
//			return ResponseEntity.status(403).body(Optional.empty());
//		}
//
//		// 조건이 충족되면 팔로워, 팔로잉 관계 조회
//		Optional<Follow> follow = followService.getFollow(targetSeq, fanSeq);
//		return ResponseEntity.ok(follow);
//	}

}