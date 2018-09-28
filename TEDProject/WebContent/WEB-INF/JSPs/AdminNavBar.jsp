<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<nav class="navbar navbar-expand-xl bg-light justify-content-center">
	<div class="container-fluid">
	    <div class="navbar-header">
	    	<a class="navbar-brand" href="/TEDProject/admin/AdminServlet">PRONET</a>
	    </div>
		<ul class="navbar-nav"  role="navigation">
			<li class="nav-item">
				<form class="form-inline" action="/TEDProject/LogoutServlet" method="post">
					<input class="btn btn-primary" type="submit" value="Logout" >
				</form>
			</li>
		</ul>
	</div>
</nav>
