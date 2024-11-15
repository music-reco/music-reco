package com.e106.reco.domain.artist.follow.service;

import com.e106.reco.domain.artist.follow.dto.FollowRequest;
import com.e106.reco.domain.artist.follow.dto.FollowResponse;
import com.e106.reco.domain.artist.follow.entity.Follow;
import com.e106.reco.domain.artist.follow.repository.FollowRepository;
import com.e106.reco.global.common.CommonResponse;
import com.e106.reco.global.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

	private final FollowRepository followRepository;

	public CommonResponse addFollow(FollowRequest followRequest) {
		Long mySeq = AuthUtil.getCustomUserDetails().getSeq();
		Long targetSeq = followRequest.getTargetSeq();

		if(followRepository.existsByFollow(targetSeq, mySeq))
			throw new RuntimeException();

		// ver 1
//		PK followId = new PK(targetSeq, fan.getId());
//		Follow follow = new Follow(followId, targetType, fan);

//		Follow follow = new Follow();
//		Follow.PK pk = new Follow.PK(targetSeq, mySeq);
//		follow.setPk(pk);
//		follow.setFanUser(new User());


		// ver 2
//		Follow follow = Follow.builder()
//				.pk(Follow.PK.builder()
//						.targetSeq(targetSeq)
//						.fanSeq(mySeq)
//						.build())
//				.fanUser(userRepository.findBySeq(mySeq).orElseThrow(() -> new BusinessException(ArtistErrorCode.ARTIST_NOT_FOUND)))
//				.targetUser(Artist.builder()
//								.seq(targetSeq)
//								.build())
//				.build();

		Follow follow = Follow.of(mySeq, targetSeq);
		followRepository.save(follow);

		return new CommonResponse("팔로우가 완료 되었습니다.");
	}
	// 팔로우 취소
	public CommonResponse removeFollow(Long targetSeq) {
		Long mySeq = AuthUtil.getCustomUserDetails().getSeq();
		Follow follow = followRepository.findByPk_TargetSeqAndPk_FanSeq(targetSeq, mySeq)
				.orElseThrow(RuntimeException::new);
		followRepository.delete(follow);
//		followRepository.deleteAllById(Pk);
		return new CommonResponse("삭제가 완료 되었습니다.");
	}
//
//	// 팔로우 관계 조회
//	public Optional<Follow> getFollow(Long targetSeq, Long fanSeq) {
//		return followRepository.findById(new PK(targetSeq, fanSeq));
//	}

	public List<FollowResponse> getMyTarget() {
		Long userSeq = AuthUtil.getCustomUserDetails().getSeq();

//		List<Follow> byPkFanSeq = followRepository.findByPk_fanSeq(userSeq);
//		옛날 식으로 하면
//				List<FollowResponse> followResponses = new ArrayList<>();
//				for (Follow follow : byPkFanSeq) {
//					FollowResponse followResponse = new FollowResponse();
//					followRespon.setPk(sdasdas);
//					followRespon.setfollow(sdasdas);
//					followRespon.set(sdasdas);
//				followResponses.add(followResponse)
//				}

		// 조금 깔끔하게 바꾸면
		return followRepository.findByPk_fanSeq(userSeq)
				.stream()
				.map(this::toTargetResponse)
				.toList();
	}
	public List<FollowResponse> getMyFan() {
		Long userSeq = AuthUtil.getCustomUserDetails().getSeq();

//		List<Follow> byPkFanSeq = followRepository.findByPk_fanSeq(userSeq);
//		옛날 식으로 하면
//				List<FollowResponse> followResponses = new ArrayList<>();
//				for (Follow follow : byPkFanSeq) {
//					FollowResponse followResponse = new FollowResponse();
//					followRespon.setPk(sdasdas);
//					followRespon.setfollow(sdasdas);
//					followRespon.set(sdasdas);
//				followResponses.add(followResponse)
//				}

		// 조금 깔끔하게 바꾸면
		return followRepository.findByPk_targetSeq(userSeq)
				.stream()
				.map(this::toFanResponse)
				.toList();
	}
	private FollowResponse toTargetResponse(Follow follow) {
		return FollowResponse.builder()
				.artistSeq(follow.getPk().getTargetSeq())
				.nickname(follow.getTargetUser().getNickname())
				.thumbnail(follow.getTargetUser().getProfileImage())
				.build();
	}

	private FollowResponse toFanResponse(Follow follow) {
		return FollowResponse.builder()
				.artistSeq(follow.getPk().getFanSeq())
				.nickname(follow.getFanUser().getNickname())
				.thumbnail(follow.getFanUser().getProfileImage())
				.build();
	}
}