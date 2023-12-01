<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
    <h2>Login</h2>
    <form action="<%=request.getContextPath()%>/user/userLoginAction.do" method="post">
        <label for="username">유저아이디:</label>
        <input type="text" id="userId" name="userId" required>
        <br>
        <label for="password">유저비밀번호:</label>
        <input type="password" id="userPwd" name="userPwd" required>
        <br>
        <input type="submit" value="Login">
    </form>
</body>
</html>