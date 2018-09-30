function checkIdenticalPasswords() {
	if ($("#password").val() != $("#rePassword").val()) {
        alert("Passwords do not match!");
        return false;
    }
	return true;
}

function restrictPassword() {
	//var regEx = new RegExp('^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])[A-Za-z\\d#^$@!%&*+-]{8,}$');
	var password = $("#password").val();
	
	var upperCase= new RegExp('[A-Z]');
	var lowerCase= new RegExp('[a-z]');
	var numbers = new RegExp('[0-9]');

	if (password.length >= 8 && password.match(upperCase) && password.match(lowerCase) && password.match(numbers)) {
		return true;
	} else {
		alert("Weak password!\nYour password must be at least 8 characters long and must contain at least:\n * An uppercase letter\n * A lowercase letter\n * A number");
		return false;
	}

}
