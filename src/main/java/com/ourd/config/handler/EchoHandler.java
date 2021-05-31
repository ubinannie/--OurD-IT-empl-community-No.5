package com.ourd.config.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// 댓글 알림부문 -> 구현

public class EchoHandler extends TextWebSocketHandler{
	
	// 로그인한 전체
	List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
	
	// 로그인 개별 유저
	Map<String, WebSocketSession> userSessionMap = new HashMap<String, WebSocketSession>();
	
	// 서버 접속이 성공 했을 때
	// @Override
	public void afterConnectionExstablished(WebSocketSession session) throws Exception{
		String senderId = getMemberId(session);
		if(senderId!=null) {
			// 로그인 값이 있는 경우
			log(senderId + "연결 됨 ");
			// 로그인 중 개별 유저 저장
			userSessionMap.put(senderId, session);
		}
		
	}
	

	// 소켓에 메세지를 보냈을 때
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
		String senderId = getMemberId(session);
		// 댓글 정보 불러오기
		String msg = message.getPayload();
		if(msg != null) {
			String[] strs = msg.split(",");
			log(strs.toString());
			if(strs != null && strs.length == 4) {
				String type = strs[0];
				// 맴버 아이디
				String target = strs[1];
				String content = strs[2];
				String url = strs[3];
				// 메시지를 받을 세션 조회하는 거 
				WebSocketSession targetSession = userSessionMap.get(target);
				
				// 실시간으로 접속시
				// 이부분 변경이 필요함 일단 패스 
				if("reply".equals(type) && targetSession != null) {
				TextMessage tmpMsg = new TextMessage(target + "님이 " + 
						"<a type='external' href='/mentor/menteeboard/menteeboardView?seq="+url+"&pg=1'>" + url + "</a> 번 게시글에 댓글을 남겼습니다.");
				targetSession.sendMessage(tmpMsg);
				}
				
			}
		}
	
		
	}
	
	// 연결 해제될때
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
		String senderId = getMemberId(session);
		if(senderId!=null) {	// 로그인 값이 있는 경우만
			log(senderId + "연결 종료됨");
			userSessionMap.remove(senderId);
			sessions.remove(session);
		}
		
	}
	
	// 에러 발생시
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception{
		log(session.getId() + "exception 발생 : " + exception.getMessage());
	}
	
	// 로그 메시지
	private void log(String logmsg) {
		System.out.println(new Date() + " : " + logmsg);
	}
	
	// 웹소켓에 id 가져오기
    // 접속한 유저의 http세션을 조회하여 id를 얻는 함수
	private String getMemberId(WebSocketSession session) {
		Map<String, Object> httpSession = session.getAttributes();
		String m_id = (String) httpSession.get("m_id"); // 세션에 저장된 m_id 기준 조회
		return m_id==null? null: m_id;
	}
	
}
