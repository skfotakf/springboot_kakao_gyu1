<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>
    <div class="nav">
        <a href="index" class="brand_logo"><h1>kakao</h1></a>
        <ul class="nav_item">
            <a href=""><li>카카오</li></a>
            <a href="/notice/list/1"><li>뉴스</li></a>
            <a href=""><li>기술과 서비스</li></a>
            <a href=""><li>약속과 책임</li></a>
        </ul>
        <c:set var="emailAddress" value="@kakao.com"></c:set>
        
        <c:choose>
        	<c:when test="${empty login_user }">
        		<ul class="nav_user">
            		<a href="/sign-in"><li><i class="fas fa-user"></i></li></a>
            		<a href="/sign-up"><li><i class="fas fa-user-plus"></i></li></a>
        		</ul>
        	</c:when>
        	<c:otherwise>
        		<ul class="nav_user">
            		<a href="/mypage"><li><i class="fas fa-user-circle"></i> ${login_user.user_email}<span>${emailAddress}</span></li></a>
            		<a href="/logout" class="logout"><li><i class="fas fa-sign-out-alt"></i></li></a>
        		</ul>
        	</c:otherwise>
        </c:choose>
    </div>
</header>

<script type="text/javascript">

		const logout = document.querySelector('.logout');
		logout.onclick = () =>{
			signOut();
			location.href = '/logout';
		}
	function init(){
		gapi.load('auth2', function(){
			let gauth = gapi.auth2.init({
				client_id: '255968834405-10eak4n3n4mf8iip36heu3gtr2sgm0q8.apps.googleusercontent.com'
			});

		});
	}
	   function signOut() {
	    var auth2 = gapi.auth2.getAuthInstance();
	    auth2.signOut().then(function () {
	      alert('User signed out.');
	    });
	  }

</script>
<script src="https://apis.google.com/js/platform.js?onload=init" async defer></script>