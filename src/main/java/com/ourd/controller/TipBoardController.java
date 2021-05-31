package com.ourd.controller;

import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ourd.constant.Method;
import com.ourd.domain.TipBoardDTO;
import com.ourd.service.JwtTokenProvider;
import com.ourd.service.TipBoardService;
import com.ourd.util.UiUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class TipBoardController extends UiUtils {

	@Autowired
	private TipBoardService boardService;
	
	private final JwtTokenProvider jwtTokenProvider;

	@GetMapping(value = "/tipBoard/write.do")
	public String openBoardWrite(@ModelAttribute("params") TipBoardDTO params,
			@RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			model.addAttribute("board", new TipBoardDTO());
		} else {
			TipBoardDTO board = boardService.getBoardDetail2(idx);
			if (board == null || "Y".equals(board.getDeleteYn())) {
				return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/tipBoard/list.do", Method.GET, null,
						model);
			}
			model.addAttribute("board", board);

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
        
		return "tipBoard/write";
}

	@PostMapping(value = "/tipBoard/register.do")
	public String registerBoard(final TipBoardDTO params, Model model) {
		Map<String, Object> pagingParams = getPagingParams(params);
		try {
			boolean isRegistered = boardService.registerBoard2(params);
			if (isRegistered == false) {
				return showMessageWithRedirect("게시글 등록에 실패하였습니다.", "/tipBoard/list.do", Method.GET, pagingParams,
						model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/tipBoard/list.do", Method.GET, pagingParams,
					model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/tipBoard/list.do", Method.GET, pagingParams, model);
		}

		return showMessageWithRedirect("게시글 등록이 완료되었습니다.", "/tipBoard/list.do", Method.GET, pagingParams, model);
	}

	@GetMapping(value = "/tipBoard/list.do")
	public String openBoardList(@ModelAttribute("params") TipBoardDTO params, Model model) {
		List<TipBoardDTO> boardList = boardService.getBoardList2(params);
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
		return "tipBoard/list";
	}

	@GetMapping(value = "/tipBoard/view.do")
	public String openBoardDetail(@ModelAttribute("params") TipBoardDTO params,
			@RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/tipBoard/list.do", Method.GET, null, model);
		}

		TipBoardDTO board = boardService.getBoardDetail2(idx);
		if (board == null || "Y".equals(board.getDeleteYn())) {
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/tipBoard/list.do", Method.GET, null, model);
		}
		model.addAttribute("board", board);
		
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

		return "tipBoard/view";
	}

	@PostMapping(value = "/tipBoard/delete.do")
	public String deleteBoard(@ModelAttribute("params") TipBoardDTO params,
			@RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/tipBoard/list.do", Method.GET, null, model);
		}

		Map<String, Object> pagingParams = getPagingParams(params);
		try {
			boolean isDeleted = boardService.deleteBoard2(idx);
			if (isDeleted == false) {
				return showMessageWithRedirect("게시글 삭제에 실패하였습니다.", "/tipBoard/list.do", Method.GET, pagingParams,
						model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/tipBoard/list.do", Method.GET, pagingParams,
					model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/tipBoard/list.do", Method.GET, pagingParams, model);
		}

		return showMessageWithRedirect("게시글 삭제가 완료되었습니다.", "/tipBoard/list.do", Method.GET, pagingParams, model);
	}

	
	/*
	 * @GetMapping("/tipBoard/download.do") public void
	 * downloadAttachFile(@RequestParam(value = "idx", required = false) final Long
	 * idx, Model model, HttpServletResponse response) {
	 * 
	 * if (idx == null) throw new RuntimeException("올바르지 않은 접근입니다.");
	 * 
	 * 
	 * }
	 */
	 

}
