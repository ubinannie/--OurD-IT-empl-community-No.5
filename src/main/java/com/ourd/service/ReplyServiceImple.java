package com.ourd.service;

import java.text.SimpleDateFormat;


import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.ourd.domain.Reply;
import com.ourd.domain.Replylist;
import com.ourd.repository.ReplyListRepository;
import com.ourd.repository.ReplyRepository;
import com.ourd.util.Conversion;



@Service
public class ReplyServiceImple implements ReplyService {
    private final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final int JUST_ONE_REPLY = 1;
    private final int JUST_TWO_REPLY = 2;
    @Autowired
    private ReplyListRepository replylistRepository;

    @Autowired
    private ReplyRepository replyRepository;


    @Override
    public List<Replylist> getRepliesByBoardId(long boardId) {
        List<Replylist> replies = replylistRepository.findAllByBoardId(boardId);
        replies.stream()
              .peek(Conversion::convertContent)
              .forEach(Conversion::convertDateFormatForArticle);
        return replies;
    }

    @Override
    public boolean writeReply(long boardId,long parent, String content, String member, String parentWriter) {
//        if (Objects.isNull(member)) {
//            return false;
//        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String date = sdf.format(new Date());
        
     
        Reply reply = Reply.builder()
                .parent(parent)
                .parentWriter(parentWriter)
                .board(boardId)
                .writer(member)
                .content(content)
                .date(date)
                .build();

        reply = replyRepository.save(reply);
        if (parent == 0) {
           reply.setParent(reply.getReplyId());
            reply = replyRepository.save(reply);
        }
        return true;
    }
    
	@Override
	public boolean modifyReply(long replyId, String content) {
		// TODO Auto-generated method stub
		Optional<Reply> resReply = replyRepository.findByReplyId(replyId);
		
		if (!resReply.isPresent()) {
            return false;
        }
		
		Reply reply = resReply.get();
        reply.setContent(content);
        replyRepository.save(reply);
        return true;
		
	}
	
	public String findParentContent(long parent) {
		
		
		return DATE_FORMAT;
	}
    @Override
    public boolean deleteReply(long replyId, long parent, String member, String parentWriter) {
        if (Objects.isNull(member)) {
            return false;
        }
        Optional<Reply> resReply = replyRepository.findByReplyIdAndParent(replyId, parent);
        Optional<Reply> resReply1 = replyRepository.findByReplyIdAndParent(parent, parent);
        Reply reply1 = resReply1.get();
        if (!resReply.isPresent()) {
            return false;
        }
        System.out.println("부모댓글내용:" + reply1.getContent());
        

//        if (resReply.get().getWriter() != member.getMemberId() 
//        			&& member.getMemberId() != ADMIN_ID ) 
//        {
//            return false;
//        }
//        
        List<Reply> replies = replyRepository.findAllByParent(parent);
        if ( (replies.size() > JUST_ONE_REPLY) &&
                (parent == replyId) ) {
            Reply reply = resReply.get();
            reply.setContent("삭제된 댓글입니다");
           
            replyRepository.save(reply);
            return true;
        }
        
		
        if ( (replies.size() == JUST_TWO_REPLY) && 
				(reply1.getContent().equals("삭제된 댓글입니다")) ) {
	                    
        	 replyRepository.deleteById(parent);
			 replyRepository.deleteById(replyId); 
		                                                             
			 return true;
       }
	
		 replyRepository.deleteById(replyId); 

        return !replyRepository.findById(replyId).isPresent();
    }

    @Override
    public int getListByMemberId(long memberId, int page, int size, ModelAndView mav) {
		
//        PageRequest pageRequest = PageRequest.of(page, size);
//        Page<Replylist> replylistPage = replylistRepository.findAllByMemberId(memberId, pageRequest);
//        List<Replylist> replies = replylistPage.getContent();
//        replies.forEach(Conversion::convertDateFormatForArticleList);
//        Conversion.convertContentLength(replies);
//        int totalPages = replylistPage.getTotalPages();
//        mav.addObject("replies", replies);
//        mav.addObject("totalPages", totalPages);
//        return replies.size();
    	return size;
    }


}
