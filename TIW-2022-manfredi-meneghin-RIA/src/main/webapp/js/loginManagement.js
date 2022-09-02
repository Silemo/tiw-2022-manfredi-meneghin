/**
 * Login management
 */

(function() {
    // Links html elements
    var login_button          = document.getElementById("login_button");
    var register_button       = document.getElementById("register_button");
    var open_register_button  = document.getElementById("open_register_button");
    var login_warning_div     = document.getElementById('login_warning_id');
    var register_div          = document.getElementById("register-div");
    var email_input           = register_button.closest("form").querySelector('input[name="email"]');
    var password_input        = register_button.closest("form").querySelector('input[name="password"]');
    var repeat_password_input = register_button.closest("form").querySelector('input[name="repeat_pwd"]');
    var register_warning_div  = document.getElementById('register_warning_id');
	
    // Attaches to login button
    login_button.addEventListener("click", (e) => {
	
        var form = e.target.closest("form"); 
        login_warning_div.style.display = 'none';
        // Does a form check
        if (form.checkValidity()) {
	
            sendToServer(form, login_warning_div, 'Login', true);
            
        } else { 
            //If not valid, notifies
            form.reportValidity(); 
        }
    });

    //Attaches to register button
    register_button.addEventListener("click", (e) => {
	
        var form = e.target.closest("form"); 
        register_warning_div.style.display = 'none';
        // Checks if the inserted string (EMAIL) matches with an e-mail syntax (RCF5322 e-mail) by using a RegEx
		const emailRegEx = new RegExp("^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$");
        // Does a form check
        if (form.checkValidity()) { 
            // Checks if repeat_pwd and password field are not equal. If so sets a warning
            if (repeat_password_input.value != password_input.value) {
	
                register_warning_div.textContent   = "Passwords do not match";
                register_warning_div.style.display = 'block';
                return;
            }
            // Checks if the email is not valid. If so sets a warning
            if (!emailRegEx.test(email_input.value)) {
				
				register_warning_div.textContent   = "The email is not valid";
                register_warning_div.style.display = 'block';
                return;
			}
            
            sendToServer(form, register_warning_div, 'Register', false);
        
        } else {
	 		//If not valid, notifies
            form.reportValidity(); 
        }
    });

    //Attaches to register view button
    open_register_button.addEventListener("click", function(e) {
	
        if (e.target.textContent === "Register now!") {
	
            e.target.textContent       = "Hide register form";
            register_div.style.display = 'block';
            
        } else {
	
            e.target.textContent       = "Register now!";
            register_div.style.display = 'none';
        }
    });

	var self = this;

    function sendToServer(form, error_div, request_url, isLogin) {
	
        makeCall("POST", request_url, form, function(req) {
			// Gets status code
            switch(req.status) {
	
                case 200: // ok
                                
                    if (isLogin) {
	
						var data = JSON.parse(req.responseText);
                    	sessionStorage.setItem('id', data.id);
                    	sessionStorage.setItem('name', data.name);
                    	sessionStorage.setItem('username', data.username);
                    	window.location.href = "home.html";
                    
                    } else {
						
						var click = new Event("click");
                        self.open_register_button.dispatchEvent(click);
					}
					
                    break;
                    
                case 400: // bad request
                
                case 401: // unauthorized
                
                case 500: // server error
                    error_div.textContent   = req.responseText;
                    error_div.style.display = 'block';
                    break;
                    
                default: // error
                    error_div.textContent   = "Request reported status " + req.status;
                    error_div.style.display = 'block';
            }
        });
    }
})();