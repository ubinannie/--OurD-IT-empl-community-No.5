package com.ourd.controller;

import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ourd.config.auth.PrincipalDetails;
import com.ourd.model.User;
import com.ourd.repository.UserRepository;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class IndexController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

//	@GetMapping({"" , "/", "index"})
//	public @ResponseBody String index() {
//		return "index";
//	}

	
	@GetMapping("/")
	public String FirstPage() {
		return "home";
	}
	
	// 구글 컨트롤러 도메인 : http://localhost:9090/login/oauth2/code/google
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principal) {
		System.out.println("Principal : " + principal);
		// iterator 순차 출력 해보기
		Iterator<? extends GrantedAuthority> iter = principal.getAuthorities().iterator();
		while (iter.hasNext()) {
			GrantedAuthority auth = iter.next();
			System.out.println(auth.getAuthority());
		}

		return "user";
	}

	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "어드민 페이지입니다.";
	}
	
	//@PostAuthorize("hasRole('ROLE_MANAGER')")
	//@PreAuthorize("hasRole('ROLE_MANAGER')")
	@Secured("ROLE_MANAGER")
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "매니저 페이지입니다.";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
		
	}
	
	
	@GetMapping("/loginSuccess")
	public String loginSuccess() {
		return "loginSuccess";
	}
	
	
	@GetMapping("/logout")
		public String logout(HttpSession session) {
	        session.removeAttribute("user");
	        
	        return "redirect:/login";
	    }

	
	
	@GetMapping("/join")
	@PostMapping("/join")
	public String join() {
		return "join";
	}

	
	@PostMapping("/joinProc")
	public String joinProc(User user) {
		System.out.println("회원가입 진행 : " + user);
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRole("ROLE_USER");
		userRepository.save(user);
		return "redirect:/home";
		
	}
	
	
	@GetMapping("/home")
	public String home() {
		return "home";
	}
	
	
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	
	
	@GetMapping("/board")
	public String board() {
		return "board";
		
		
		
	}
}