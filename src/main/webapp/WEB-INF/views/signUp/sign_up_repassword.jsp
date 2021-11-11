<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>카카오계정 만들기</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/sign_up.css">
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>

<body>
    <div class="container">
        <div class="inner_container">
            <jsp:include page="../include/sign_up_include/sign_up_header.jsp"></jsp:include>
            <main>
                <div class="warp_form">
                    <form action="signUp" method="post">
                    	<input type="hidden" name="submit_status" value="repassword">
                    	<input type="hidden" name="id" value="<%= request.getParameter("id")%>">
                    	<input type="hidden" name="password" id="password" value="<%= request.getParameter("password")%>">
                        <div class="navigation_wrap">
                            <progress class="bar_navigation" value="60" max="100"></progress>
                        </div>
                        <h2>카카오계정 가입을 위해<br>
                            비밀번호를 확인해 주세요.</h2>
                        <div class="item_tf">
                            <input type="password" class="item_ip" name="repassword" placeholder="비밀번호 확인" autofocus="autofocus">
                        </div>
                        <div class="item_msg">
                        	<span class="msg1">필수 항목입니다.</span>
                        	<span class="msg2">비밀번호가 일치하지 않습니다.</span>
                        </div>
                        <div class="confirm_btn">
                            <button type="button" class="btn_g">다음</button>
                        </div>
                    </form>
                </div>
            </main>
            <jsp:include page="../include/sign_up_include/sign_up_footer.jsp"></jsp:include>
        </div>
    </div>
    <script type="text/javascript" src="js/sign_up_repassword.js"></script>
</body>

</html>