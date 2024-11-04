package com.e106.reco.domain.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Sources")
public class Source {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_seq")
    private Long seq;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "board_seq", nullable = false)
    private Board board;
}
