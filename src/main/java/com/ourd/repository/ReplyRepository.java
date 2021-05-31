package com.ourd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ourd.domain.Reply;

import java.util.List;
import java.util.Optional;


public interface ReplyRepository extends JpaRepository<Reply, Long> {
  
	  List<Reply> findAllByBoard(long board_id);

	  Optional<Reply> findByReplyId(long replyId);
	  
	  
	  void deleteByReplyIdAndParentAndWriter(long replyId, long parent, long memberId);

	  Optional<Reply> findByReplyIdAndParentAndWriter(long replyId, long parent, long memberId);

	  List<Reply> findAllByParent(long parent);

	  Optional<Reply> findByReplyIdAndParent(long replyId, long parent);
}
