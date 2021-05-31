package com.ourd.service;

import org.springframework.data.domain.Page;

import org.springframework.web.servlet.ModelAndView;

import com.ourd.domain.Replylist;

import java.util.List;

public interface ReplyService {
    List<Replylist> getRepliesByBoardId(long boardId);

    boolean writeReply(long boardId,long parent,String content, String member,String parentWriter);

    boolean modifyReply(long replyId,String content);
    
    boolean deleteReply(long replyId, long parent, String member, String writer);

    int getListByMemberId(long memberId, int page, int size, ModelAndView mav);
}