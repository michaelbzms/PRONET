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
    Cookies.remove("fileDownloading", { path: '/TEDProject/admin' });		// clean the cookie for future downoads
    location.href = "/TEDProject/admin/AdminServlet";		// redirect
  }
}, 500);
