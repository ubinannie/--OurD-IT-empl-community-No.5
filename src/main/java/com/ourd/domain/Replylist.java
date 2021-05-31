package com.ourd.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamicUpdate
@Builder
public class Replylist implements Serializable, Article{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id", nullable = false)
    private long replyId;

    @Column(nullable = false)
    private long parent;

    @Column
    private String parentWriter;
    
    @Column(nullable = false)
    private String content;

    @Column
    private String date;

    @Column(nullable = false)
    private String username;

    @Column(name = "board", nullable = false)
    private long boardId;
}