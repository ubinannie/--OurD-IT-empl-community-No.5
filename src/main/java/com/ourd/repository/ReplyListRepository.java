package com.ourd.repository;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ourd.domain.Replylist;

import java.util.List;

public interface ReplyListRepository extends JpaRepository<Replylist, Long> {
    List<Replylist> findAllByBoardId(long board_id);

//    Page<Replylist> findAllByMemberId(long memberId, Pageable pageRequest);
}