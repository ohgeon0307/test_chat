<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="dao.UserDao"%>

<%
    String userId = (String) session.getAttribute("userId");
    String logoutLink = (userId != null) ? ("<a href='" + request.getContextPath() + "/user/userLogout.do'>로그아웃</a>") : "";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>Chat Room</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</head>
<body>
    <%= logoutLink %>
    <a href="<%=request.getContextPath()%>/user/userLogin.do">로그인</a>
    <textarea id="textarea_chat" rows="10" cols="50" readonly="true"></textarea>
    <br />
    <form id="chatForm">
        <input id="input_msg" type="text" name="message" /> <input
            type="button" value="send" onclick="send(event)" />
    </form>
    <p>
        Welcome,
        <%= userId %>!
    </p>
    <script type="text/javascript">
        var textarea_chat = document.getElementById('textarea_chat');
        var input_msg = document.getElementById('input_msg');
        var userId = "<%= userId %>";

        // EventSource for receiving SSE messages
        var eventSource = new EventSource('<%= request.getContextPath() %>/ChatSSEServlet');

        eventSource.onmessage = function (event) {
            var message = event.data;
            appendMessage(message);
        };

        function appendMessage(message) {
        	var chatTextArea = document.getElementById('textarea_chat');
            // 현재 텍스트 에어리어의 내용을 가져옴
            var currentContent = chatTextArea.value;

            // 세션에 존재하는 유저 아이디
            var sessionUserId = "<%= userId %>";

            // 새로운 메시지를 추가
            var updatedContent = currentContent + message + '\n';

            // 텍스트 에어리어에 업데이트된 내용을 설정
            chatTextArea.value = updatedContent;

            // 스크롤을 가장 아래로 이동하여 최신 메시지가 보이도록 함
            chatTextArea.scrollTop = chatTextArea.scrollHeight;
        }

        function send(event) {
            var message = document.getElementById('input_msg').value;

            $.ajax({
                type: "POST",
                url: "<%= request.getContextPath() %>/user/saveMessage.do",
                data: {
                    message: message,
                    userId: userId
                },
                success: function (response) {
                    console.log(response);
                },
                error: function (error) {
                    console.log(error);
                }
            });
        }
    </script>
</body>
</html>