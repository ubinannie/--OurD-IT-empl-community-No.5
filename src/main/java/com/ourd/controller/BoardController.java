package com.ourd.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ourd.config.auth.PrincipalDetails;
import com.ourd.constant.Method;
import com.ourd.domain.AttachDTO;
import com.ourd.domain.BoardDTO;
import com.ourd.domain.Replylist;
import com.ourd.repository.ChatRoomRepository;
import com.ourd.service.BoardService;
import com.ourd.service.ChatService;
import com.ourd.service.JwtTokenProvider;
import com.ourd.service.ReplyService;
import com.ourd.util.UiUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Controller
public class BoardController extends UiUtils {

	@Autowired
	private BoardService boardService;
	
	 @Autowired
	private ReplyService replyService;
	
    private final JwtTokenProvider jwtTokenProvider;
    
    
	@GetMapping(value = "/board/write.do")
	public String openBoardWrite(@ModelAttribute("params") BoardDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			model.addAttribute("board", new BoardDTO());
		} else {
			BoardDTO board = boardService.getBoardDetail(idx);
			if (board == null || "Y".equals(board.getDeleteYn())) {
				return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
			}
			model.addAttribute("board", board);

			List<AttachDTO> fileList = boardService.getAttachFileList(idx);
			model.addAttribute("fileList", fileList);
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        System.out.println(name);
        if(name.equals("anonymousUser")) {
           System.out.println("비로그인 유저입니다");
        }else {
           System.out.println("로그인 유저입니다");
       
        String token = jwtTokenProvider.generateToken(name);
        String nickname = jwtTokenProvider.getUserNameFromJwt(token);
      
        System.out.println(token);
        System.out.println("nickname : " + nickname);
        model.addAttribute("nickname", nickname);
        }

		return "board/write";
	}

	@PostMapping(value = "/board/register.do")
	public String registerBoard(final BoardDTO params, final MultipartFile[] files, Model model) {
		Map<String, Object> pagingParams = getPagingParams(params);
		try {
			boolean isRegistered = boardService.registerBoard(params, files);
			if (isRegistered == false) {
				return showMessageWithRedirect("게시글 등록에 실패하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
		}

		return showMessageWithRedirect("게시글 등록이 완료되었습니다.", "/board/list.do", Method.GET, pagingParams, model);
	}

	@GetMapping(value = "/board/list.do")
	   public String openBoardList(@ModelAttribute("params") BoardDTO params, Model model) {
	      List<BoardDTO> boardList = boardService.getBoardList(params);
	      model.addAttribute("boardList", boardList);
	      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String name = auth.getName();
	        System.out.println(name);
	        if(name.equals("anonymousUser")) {
	           System.out.println("비로그인 유저입니다");
	        }else {
	           System.out.println("로그인 유저입니다");
	       
	        String token = jwtTokenProvider.generateToken(name);
	        String nickname = jwtTokenProvider.getUserNameFromJwt(token);
	      
	        System.out.println(token);
	        System.out.println("nickname : " + nickname);
	        model.addAttribute("nickname", nickname);
	        }
	      return "board/list";
	   }
	

	@GetMapping(value = "/board/view.do")
	public String openBoardDetail(@ModelAttribute("params") BoardDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list.do", Method.GET, null, model);
		}

		BoardDTO board = boardService.getBoardDetail(idx);
		if (board == null || "Y".equals(board.getDeleteYn())) {
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
		}
		model.addAttribute("board", board);

		List<AttachDTO> fileList = boardService.getAttachFileList(idx); // 추가된 로직
		model.addAttribute("fileList", fileList); // 추가된 로직

		List<Replylist> replies = replyService.getRepliesByBoardId(idx);

	    model.addAttribute("replies", replies);
	    
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        System.out.println(name);
        if(name.equals("anonymousUser")) {
           System.out.println("비로그인 유저입니다");
        }else {
           System.out.println("로그인 유저입니다");
       
        String token = jwtTokenProvider.generateToken(name);
        String nickname = jwtTokenProvider.getUserNameFromJwt(token);
      
        System.out.println(token);
        System.out.println("nickname : " + nickname);
        model.addAttribute("nickname", nickname);
        }
		
		return "board/view";
	}

	@PostMapping(value = "/board/delete.do")
	public String deleteBoard(@ModelAttribute("params") BoardDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list.do", Method.GET, null, model);
		}

		Map<String, Object> pagingParams = getPagingParams(params);
		try {
			boolean isDeleted = boardService.deleteBoard(idx);
			if (isDeleted == false) {
				return showMessageWithRedirect("게시글 삭제에 실패하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
		}

		return showMessageWithRedirect("게시글 삭제가 완료되었습니다.", "/board/list.do", Method.GET, pagingParams, model);
	}

	@GetMapping("/board/download.do")
	public void downloadAttachFile(@RequestParam(value = "idx", required = false) final Long idx, Model model, HttpServletResponse response) {

		if (idx == null) throw new RuntimeException("올바르지 않은 접근입니다.");

		AttachDTO fileInfo = boardService.getAttachDetail(idx);
		if (fileInfo == null || "Y".equals(fileInfo.getDeleteYn())) {
			throw new RuntimeException("파일 정보를 찾을 수 없습니다.");
		}

		String uploadDate = fileInfo.getInsertTime().format(DateTimeFormatter.ofPattern("yyMMdd"));
		String uploadPath = Paths.get("C:", "develop", "upload", uploadDate).toString();

		String filename = fileInfo.getOriginalName();
		File file = new File(uploadPath, fileInfo.getSaveName());

		try {
			byte[] data = FileUtils.readFileToByteArray(file);
			response.setContentType("application/octet-stream");
			response.setContentLength(data.length);
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(filename, "UTF-8") + "\";");

			response.getOutputStream().write(data);
			response.getOutputStream().flush();
			response.getOutputStream().close();

		} catch (IOException e) {
			throw new RuntimeException("파일 다운로드에 실패하였습니다.");

		} catch (Exception e) {
			throw new RuntimeException("시스템에 문제가 발생하였습니다.");
		}
	}
	
	
	
    @PostMapping("/board/replyWrite")
	@ResponseBody
    public String writeReply(@RequestParam int parent,
                           @RequestParam String content,
                           @RequestParam int boardId,
                           @RequestParam(required=false) String parentWriter,
                           @RequestParam String nickname
                           ) {
    	System.out.println("ajax 시작");

	    replyService.writeReply(boardId, parent, content, nickname, parentWriter);
			 
   
    	
   
      
        return "redirect:/board/view.do?idx="+boardId;        
    }
    
    @PostMapping("/board/replyModify")
    public String modifyReply(@RequestParam int replyId,
    		                  @RequestParam String content,
    		                  @RequestParam int boardId
    		) {
    	
    	
    	
    	replyService.modifyReply(replyId, content);
    	
    	return "redirect:/board/view.do?idx="+boardId; 
    }


    @PostMapping("/board/replyDelete")
    public String deleteReply(@RequestParam int replyId,
                                    @RequestParam int parent,
                                   @RequestParam int boardId,
                                   @RequestParam(required=false) String parentWriter,
                                   @AuthenticationPrincipal PrincipalDetails principal,
                                   HttpServletRequest request) {
    	String member = principal.getUsername();
      
    
       replyService.deleteReply(replyId, parent, member, parentWriter);


        return "redirect:/board/view.do?idx="+boardId;  
    }
	
}