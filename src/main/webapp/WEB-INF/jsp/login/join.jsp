<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
	<title>회원가입</title>
	<link rel="stylesheet" href="//unpkg.com/bootstrap/dist/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="/css/style.css">
	</head>
<body class="login">
    
	<div class="container">
    <div class="row">
      <div class="col">
        <form class="form-signin" action="/join" method="POST">
          <h2 class="form-signin-heading">회원가입</h2>
          <label for="inputAcount" class="sr-only">사용자계정</label>
          <input type="text" id="cust_id" name="cust_id" class="form-control" placeholder="Account" required autofocus />
          <label for="inputPassword" class="sr-only">패스워드</label>
          <input type="password" id="cust_pass" name="cust_pass" class="form-control" placeholder="Password" required />
          <input type="password" id="reinputPassword" name="recust_pass" class="form-control" placeholder="Re-Password" required />
          <div class="error-message alert text-danger ${resultCode}" style="">${resultMessage}</div>
          
          <button class="btn btn-lg btn-block" type="submit">회원가입 </button>
          <hr>
          <ul class="list-group text-muted">
          </ul>
        </form>
      </div>
    </div>

  </div> <!-- /container -->
</body>
</html>