package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dbconn.DbConn;
import dto.ChatDto;

public class UserDao {
	private Connection conn;
	private PreparedStatement pstmt;

	public UserDao() {
		DbConn dbconn = new DbConn();
		this.conn = dbconn.getConnection();

	}

	public int userLogin(String userId, String userPwd) {

		int value = 0;

		String sql = "select uidx from users where userId=? and userPwd = ?";
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, userPwd);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				value = rs.getInt("uidx");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return value;

	} public void saveChatMessage(ChatDto chatDto) {
	    String sql = "INSERT INTO chat_messages (uidx, sender, message) VALUES (?, ?, ?)";

	    try {
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, chatDto.getUidx());
	        pstmt.setString(2, chatDto.getSender()); // 추가: sender 정보 저장
	        pstmt.setString(3, chatDto.getMessage());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public List<ChatDto> getRecentChatMessages(int count) {
	    List<ChatDto> chatMessages = new ArrayList<>();
	    String sql = "SELECT u.userId, c.message FROM chat_messages c " +
	                 "JOIN users u ON c.uidx = u.uidx ORDER BY c.timestamp DESC LIMIT ?";

	    try {
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, count);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            String sender = rs.getString("userId");
	            String message = rs.getString("message");

	            ChatDto chatDto = new ChatDto(0, sender, message);
	            chatMessages.add(chatDto);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    Collections.reverse(chatMessages);
	    return chatMessages;
	}
	
}
