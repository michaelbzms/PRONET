function selectAll(source) {
	checkboxes = document.getElementsByName('profID');
	for(var i=0, n=checkboxes.length;i<n;i++) {
		checkboxes[i].checked = source.checked;
	}
}

function checkIfAnyChecked() {
	checkboxes = document.getElementsByName('profID');
	for(var i=0, n=checkboxes.length;i<n;i++) {
		if (checkboxes[i].checked == true) {
			return true;
		}
	}
    alert("At least one Professional must be selected."); 
    return false;
}

setInterval(function() {
	if (Cookies.get("fileDownloading")) {
		// clean the cookie for future downloads
		Cookies.remove("fileDownloading", { path: '/TEDProject/admin' });
		Cookies.remove("fileDownloading", { path: '/TEDProject/admin/' });
		location.href = "/TEDProject/admin/AdminServlet";		// redirect
	}
}, 500);
