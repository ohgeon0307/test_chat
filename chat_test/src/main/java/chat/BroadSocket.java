package chat;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import java.util.*;

@ServerEndpoint("/chat")

public class BroadSocket {
// 유저 닉네임
	private static final String USER_PREFIX = "User"; // 모든 유저 prefix
	

// BraodSocket 클래스 필드 변수
	private String nickName; // instance 생성시, 유저 별명 만들어짐
	private Session session; // 세션정보

	private static Set<BroadSocket> connections = Collections.synchronizedSet(new HashSet<BroadSocket>()); // 유저 Set

// 생성자
	public BroadSocket() {
		nickName = USER_PREFIX; // 유저 별명 만들어짐
	}

// 소켓이 오픈될 때
	@OnOpen
	public void onOpen(Session session) {
// 세션 정보 입력
		this.session = session;

// 유저 Set에 자기 자신도 추가
		connections.add(this);

// 새로은 유저가 추가되었다는 메세지를 쏨
		sendMsg(nickName + " has joined.");
	}

// 소켓이 닫혔을때,
	@OnClose
	public void onClose() {
// 채팅 참여자 set에 인스턴스 뺌
		connections.remove(this);

// 연결이 끊겼다는 메세지를 쏨
		sendMsg(String.format("[!] %s %s", nickName, "has been disconnected."));
	}

// error 발생시
	@OnError
	public void onError(Throwable t) {

	}

// 메세지 날릴때,
	@OnMessage
	public void onMessage(String message) {
// 메세지 쏘는 메소드에 자기 별명 담아서
		sendMsg(nickName + ": " + message.toString());
	}

// 유저들에게 메세지 날리는 메소드
	private void sendMsg(String message) {
// 유저 Set에서 한 사람식 불러옴
		for (BroadSocket bs : connections) {
			try {
				synchronized (bs) {
// 각 유저의 세션정보 가져와, 메세지를 쏨
					bs.session.getBasicRemote().sendText(message);
				}
			}
// 익셉션이 발생할 경우,
			catch (Exception e) {

// 해당 유저를 참여자 리스트에서 제거
				connections.remove(bs);

				try {
// 해당 생성자의 세션을 닫음
					if (bs.session != null)
						bs.session.close();
				} catch (Exception e1) {
				}

// exception이 발생된 유저의 연결이 끊어졌다는 메세지를 담아 다시 메세지를 쏨
				sendMsg(String.format("[!] %s %s", bs.nickName, "has been disconnected."));
			}
		}
	}
}