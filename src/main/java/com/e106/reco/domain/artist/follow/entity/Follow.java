package com.e106.reco.domain.artist.follow.entity;

import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.artist.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "follows")
public class Follow {

	@EmbeddedId
	private PK pk;   // 복합 키 필드

	@MapsId("targetSeq")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "target_seq")
	private Artist targetUser;   // 팔로우를 요청한 USER

	@MapsId("fanSeq")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fan_seq")
	private User fanUser;

	@Column(name = "target_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private TargetType targetType;   // 팔로우 대상의 유형 (USER 또는 CREW)

	@Embeddable
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode
	@Builder
	public static class PK implements Serializable {

		@Column(name = "tartget_seq")
		private Long targetSeq;

		@Column(name = "fan_seq")
		private Long fanSeq;
	}

	public static Follow of(Long mySeq, Long targetSeq){
		return Follow.builder()
				.pk(Follow.PK.builder()
						.targetSeq(targetSeq)
						.fanSeq(mySeq)
						.build())
				.fanUser(User.builder()
						.seq(mySeq)
						.build())
				.targetUser(Artist.builder()
						.seq(targetSeq)
						.build())
				.build();
	}
}