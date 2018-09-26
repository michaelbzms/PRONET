<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<nav class="navbar navbar-expand-xl bg-light justify-content-center">
	<div class="container-fluid">
	    <div class="navbar-header">
	      <a class="navbar-brand" href="/TEDProject">PRONET</a>
	    </div>
		<div class="navbar-nav" role="navigation">
			<div class="nav-item ml-xl-1">
				<form class="form-inline" method="POST" action="/TEDProject/WelcomeServlet?register=false">
			    	<input class="form-control mr-1" type="email" name="email" placeholder="Email" required>
					<input class="form-control mr-1" type="password" name="password" placeholder="Password" required>
					<input id="tmzOffset" type="hidden" name="tmzOffset" value="0">
					<input class="btn btn-primary" type="submit" value="Log in">
				</form>
			</div>
			<div class="nav-item vertical_center ml-3 mr-3">
				<p class="text-secondary mt-auto mb-auto">or</p>
			</div>
			<a class="nav-item ml-xl-1 btn btn-primary ml-1" href="/TEDProject">Click here to Sign up</a>
		</div>
	</div>
</nav>
