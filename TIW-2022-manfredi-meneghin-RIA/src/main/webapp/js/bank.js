/**
 * Bank
 */
(function() {
    //Vars
    var userInfo, accountList, transferResult, transferList, addressBook;
    
    var pageOrchestrator = new PageOrchestrator();

    window.addEventListener("load", () => {
		// initialize the components
        pageOrchestrator.start(); 
        // display initial content
        pageOrchestrator.refresh(); 
    });

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * PageOrchestrator method
     * ------------------------------------------------------------------------------------------------------
     *
     * Notes:
     * - excludeContacts parameter in PageOrchestrator.refresh(..) is used to avoid requesting
     *   address book to server when it's not needed (i.e. always but at first loading).
     *   Others refreshes will be managed by AddressBook object itself.
     */
    function PageOrchestrator(){
        
        this.start = function(){
            // Init components
            // UserInfo componet
            userInfo = new UserInfo(
	
				sessionStorage.getItem('id'),
				sessionStorage.getItem('name'),
				sessionStorage.getItem('username'),	
				[document.getElementById("headerUserId")],
				[document.getElementById("userName")],
				[document.getElementById("headerUsername")],
                document.getElementById("logout-button")
            );
           // AccountList component			   
            accountList = new AccountList(
	
                document.getElementById("create-account-form"), 
                document.getElementById("account-form-button"), 
                document.getElementById("create-account-warning"),
                document.getElementById("create-account-button"),
                document.getElementById("accounts")
            );
            // TransferList component
            transferList = new TransferList(
	
                document.getElementById("account-details"),
                document.getElementById("account-code"),
                document.getElementById("account-balance"),
                document.getElementById("create-transfer-form"),
                document.getElementById("transfer-form-button"),
                document.getElementById("create-transfer-button"),
                document.getElementById("transfers"),
                document.getElementById("transfers-message")
            );
            // TransferResult component
            transferResult = new TransferResult({
	
                "result_div"                    : document.getElementById("result-div"),
                "confirmed_div"                 : document.getElementById("confirmed-div"),
                "failed_div"                    : document.getElementById("failed-div"),
                "src_account_code_span"         : document.getElementById("src-account-code"),
                "src_owner_id_span"             : document.getElementById("src-owner-id"),
                "src_account_balance_old_span"  : document.getElementById("src-account-balance-old"),
                "src_account_balance_span"      : document.getElementById("src-account-balance"),
                "transfer_amount_span"          : document.getElementById("transfer-amount"),
                "transfer_reason_span"          : document.getElementById("transfer-reason"),
                "dest_account_code_span"        : document.getElementById("dest-account-code"),
                "dest_owner_id_span"            : document.getElementById("dest-owner-id"),
                "dest_account_balance_old_span" : document.getElementById("dest-account-balance-old"),
                "dest_account_balance_span"     : document.getElementById("dest-account-balance"),
                "failed_reason_span"            : document.getElementById("failed-reason"),
                "close_success_button"          : document.getElementById("close-success"),
                "close_failed_button"           : document.getElementById("close-failed")
            });
			// AddressBook component
            addressBook = new AddressBook(
	
                document.getElementById("add-contact"), 
                document.getElementById("dest-owner-id"), 
                document.getElementById("dest-account-code"), 
                document.getElementById("add-contact-warning"), 
                document.getElementById("create-transfer-warning"),
                document.getElementById("dest-ids-datalist"),
                document.getElementById("dest-accounts-datalist")
            );
        };
        
        /**
         * Method of the PageOrchestrator to refresh the view
         */
        this.refresh = function(excludeContacts) {
            // Refreshes view
            userInfo.show();
            accountList.show();
            
            if (!excludeContacts) {
                
                addressBook.load();
            }
        };
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor of the UserInfo class
	 * ------------------------------------------------------------------------------------------------------
	 * Description: The UserInfo class contains the information sent using the PacketUserInfo which are 
	 *              the user's infos contained in the sceen after the login.
	 * 
	 * @param {int}    _userid          the user's id
	 * @param {String} _name            the user's name
	 * @param {String} _username        the user's username
	 * @param {*}      idElements       the html elements that need the id of the user
	 * @param {*}      nameElements     the html elements that need the name of the user
	 * @param {*}      usernameElements the html elements that need the username of the user
	 * @param {*}      _logout_button   the logout button
	 */
    function UserInfo(
	
        _userid, 
        _name, 
        _username,
        idElements,
        nameElements,
        usernameElements, 
        _logout_button) { 

		this.id            = _userid;
        this.name          = _name;
        this.username      = _username;
        this.logout_button = _logout_button;

        this.logout_button.addEventListener("click", e => {
	
            sessionStorage.clear();
        });

		/**
		 * Method of the class UserInfo that shows the UserInfo 
		 * binding them with their corresponding elements
		 */
        this.show = function(){
	
            idElements.forEach(element => {
	
                element.textContent = this.id;
            });
            
            nameElements.forEach(element => {
	
                element.textContent = this.name;
            });
            
            usernameElements.forEach(element => {
	
                element.textContent = this.username;
            });
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor of the AccountList class
	 * ------------------------------------------------------------------------------------------------------
     * Description: The class AccountList collects and handles the accountList html elements
     * 
     * Notes:
     * - isNaN is used in combo with NaN, because of autoconversion of input parameter.
     *   (see https://developer.mozilla.org/it/docs/Web/JavaScript/Reference/Global_Objects/isNaN)
     * 
     * - When creating account, only the account list refreshes (and eventually autoclicks the account the user
     *   had already open, to keep its details open)
     *
     * @param {*} _create_account_form    The create account form html element
     * @param {*} _account_form_button    The "create account" form button html element
     * @param {*} _create_account_warning The "create account" form warning text html element 
     * @param {*} _accounts               The "accounts" list html element
     */
    function AccountList(
	
        _create_account_form, 
        _account_form_button,
        _create_account_warning,
        _create_account_button,
        _accounts) {

        this.create_account_form_div = _create_account_form;
        this.account_form_button     = _account_form_button;
        this.create_account_warning  = _create_account_warning;
        this.create_account_button   = _create_account_button;
        this.accounts                = _accounts;

        this.currentSelectedId     = NaN;
        this.last_used_open_button = null;

		// Necessary only for in-function helpers (makeCall)
        var self = this; 
        
        // Creates listener to the new account show form button
        this.create_account_button.addEventListener('click', (e) => {
	
            var button_label = e.target.textContent;
            
            if (button_label === 'Create account') {
	
                e.target.textContent = 'Hide form';
                self.create_account_warning.style.display = 'none';
                self.create_account_form_div.style.display='block';
            
            } else {
                
                e.target.textContent = 'Create account';
                self.create_account_form_div.style.display='none';
            }
        });
        
        // Creates listener to the new account button
        this.account_form_button.addEventListener("click", (e) => {

            self.create_account_warning.style.display = 'none';
            var create_account_form = e.target.closest("form");
            
            if (create_account_form.checkValidity()) {

                makeCall("POST", 'CreateAccount', create_account_form, (req) => {
	
                    switch(req.status) {
	
                        case 200: //ok
                        	var click = new Event("click");
                            self.create_account_button.dispatchEvent(click);
                            self.show();
                            break;
                            
                        case 400: // bad request
                        
                        case 401: // unauthorized
                        
                        case 500: // server error
                        	self.create_account_warning.textContent   = req.responseText;
                            self.create_account_warning.style.display = 'block';
                            break;
                            
                        default: //Error
                        	self.create_account_warning.textContent   = "Request reported status " + req.status;
                            self.create_account_warning.style.display = 'block';
                    }
                });
                
            } else {
	
                create_account_form.reportValidity();
            }
        });

		/**
		 * Method of the class AccountList that requests the user's accounts 
		 * and then shows the results depening on the status of the request
		 */
        this.show = function() {
            // Requests and updates with the results
            makeCall("GET", 'GetAccounts', null, (req) => {
	
                switch(req.status) {
	
                    case 200: //ok
                        var accounts = JSON.parse(req.responseText);
                        self.update(accounts);
                        
                        if (!isNaN(self.currentSelectedCode)) {
	
                            var open_account_button = document.querySelector("a[data_accountCode='" + self.currentSelectedCode + "']");
                            var click               = new Event("click");
                            
                            if (open_account_button) {
                                
                                open_account_button.dispatchEvent(click);
                            }
                        }
                         
                        break;
                        
                    case 400: // bad request
                    
                    case 401: // unauthorized
                    
                    case 500: // server error
                    	self.update(null, req.responseText);
                        break;
                        
                    default: //Error
                    	self.update(null, "Request reported status " + req.status);
                        break;
                }
            });
        };
        
        /**
         * Method of the class AccountList that updates and creates the AccountList in the HomePage
         */
        this.update = function(_accounts, _error) {
            
            self.accounts.style.display = "none";
            self.accounts.innerHTML     = "";
            
			let card, card_title, card_data, b1, open_button;
			let i = 0;

			// Loops for each account
			_accounts.forEach((acc) => {

				card           = document.createElement("div");
				card.className = "card card-blue";

				// Distinguishes between even and odd accounts (for interface purpose)
				if (i % 2 === 0) {

					card.className += " even";
				}
				
				// Adds the title "Account code: " + "0293"
				card_title             = document.createElement("div");
				card_title.className   = "card-title";
				card_title.textContent = "Account code: ";
				card_title.appendChild(document.createTextNode(acc.code));
				card.appendChild(card_title);

				// Adds the data of the card (the balance)
				card_data           = document.createElement("div");
				card_data.className = "card-data";
				
				b1             = document.createElement("b");
				b1.textContent = "Balance: ";
				card_data.appendChild(b1);
				card_data.appendChild(document.createTextNode(acc.balance + "\u20AC"));

				card.appendChild(card_data);
				
				// Adds the "Open" button (to show the account details)
				open_button             = document.createElement("a");
				open_button.className   = "btn btn-gossamer btn-small btn-primary";
				open_button.textContent = "Open";
				open_button.setAttribute('data_accountCode', acc.code);
				// Sets the event on click on the button
				open_button.addEventListener("click", (e) => {

					if (e.target.textContent === "Open") {

						if (self.last_used_open_button !== null) {

							self.last_used_open_button.textContent = "Open";
						}

						e.target.textContent       = "Hide";
						self.last_used_open_button = e.target;
						self.currentSelectedCode   = acc.code;
						transferList.show(acc.code);

					} else {

						self.last_used_open_button = null;
						self.currentSelectedCode   = NaN;
						e.target.textContent       = "Open";
						transferList.hide();
					}
				});

				card.appendChild(open_button);

				// When the card is filled, it appends it				
				self.accounts.appendChild(card);
				
				// Increases i for the next iteration
				i++;
			});

			self.accounts.style.display = "block";
        };
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor of the TransferList class
     * ------------------------------------------------------------------------------------------------------
     * Description: The TransferList class handles the transfers 
     *              list of an account and a the transfer form
     *
     * Notes:
     * - focus event is used to update (and display) suggestions before the user starts typing.
     * - keyup event is used because we needed to know the updated content of the input, after
     *   character is added to the input.
     *   (see https://www.w3.org/TR/DOM-Level-3-Events/#events-keyboard-event-order)
     *
     * @param {*} _account_details          The create account form html element
     * @param {*} _account_code             The "create account" form button html element
     * @param {*} _account_balance          The "create account" form warning text html element 
     * @param {*} _create_transfer_form_div The "accounts" list html element
     * @param {*} _transfer_form_button     The create account form html element
     * @param {*} _create_transfer_button   The "create account" form button html element
     * @param {*} _transfers                The "create account" form warning text html element 
     * @param {*} _transfers_message        The "accounts" list html element
     */
    function TransferList(
                        
       	_account_details,
       	_account_code,
       	_account_balance, 
       	_create_transfer_form_div, 
       	_transfer_form_button,
       	_create_transfer_button,
       	_transfers, 
        _transfers_message) {
        
        //Saving vars
        this.account_details          = _account_details;
        this.account_code_span        = _account_code;
        this.account_balance_span     = _account_balance;
        this.create_transfer_form_div = _create_transfer_form_div;
        this.transfer_form_button     = _transfer_form_button;
        this.create_transfer_button   = _create_transfer_button;
        this.transfers                = _transfers;
        this.transfers_message        = _transfers_message;
        
        this.create_transfer_form = this.create_transfer_form_div.querySelector("form");
        this.dest_input           = this.create_transfer_form.querySelector("input[name='destUserId']");
        this.account_input        = this.create_transfer_form.querySelector("input[name='destAccountCode']");
        this.amount_input         = this.create_transfer_form.querySelector("input[name='amount']");
        this.source_code          = this.create_transfer_form.querySelector("input[name='sourceAccountCode']");
        
        // Creates listener to the "show Tranfer" form button
        this.create_transfer_button.addEventListener("click", (e) => {
	
            if (this.create_transfer_button.textContent === 'Create transfer') {
	
                this.showCreate();
            
            } else {
				//Avoid resetting form
                this.hideCreate(false); 
            }
        });
        // Creates listener to the "makeTransfer form" button
        this.transfer_form_button.addEventListener("click", (e) => {
            // Verifies validity of the input
            if (this.create_transfer_form.checkValidity()) {
                // Verifies if the transfer is from an account onto the same account
                if (this.account_input.value == this.source_code.value) {
	
                    this.create_transfer_form.reset();
                    transferResult.showFailure("Cannot make transfers on the same account");
                    return;
                
                // If the amount to transfer is higher than the account balance
                } else if (Number(this.amount_input.value) > Number(this.account_balance_span.textContent)) {
	
                    this.create_transfer_form.reset();
                    transferResult.showFailure("You don't have enough money on this account to make this transfer");
                    return;
                }
                // Makes a request (a POST)
                var self = this;
                makeCall("POST", 'MakeTransfer', this.create_transfer_form, (req) => {
	
                    switch(req.status) {
	
                        case 200: //ok
                            var data = JSON.parse(req.responseText);
                            pageOrchestrator.refresh(true);
                            // Close form
                            var click = new Event("click");
                            self.create_transfer_button.dispatchEvent(click);

                            transferResult.showSuccess(data.sourceAccount, data.transfer, data.destAccount);
                            break;
                        
                        case 400: // bad request
                        
                        case 401: // unauthorized
                        
                        case 500: // server error
                            transferResult.showFailure(req.responseText);
                            break;
                        
                        default: //Error
                            transferResult.showFailure("Request reported status " + req.status);
                    }
                });
                
            } else {
	
                this.create_transfer_form.reportValidity();
            }
        });
        
        // Adds the EvenetListeners for the autocomplete feature
        this.dest_input.addEventListener("focus", e => {
	
            addressBook.autocompleteDest(e.target.value);
        });
        
        this.dest_input.addEventListener("keyup", e => {
	
            addressBook.autocompleteDest(e.target.value);  
        });
        
        this.account_input.addEventListener("focus", e => {
	
            addressBook.autocompleteAccount(this.dest_input.value, e.target.value, this.source_code.value);
        });
        
        this.account_input.addEventListener("keyup", e => {
	
            addressBook.autocompleteAccount(this.dest_input.value, e.target.value, this.source_code.value);
        });
        
        // Adds the show and hide functions to the block
        this.showCreate = function() {
	
            this.create_transfer_button.textContent     = 'Hide form';
            this.create_transfer_form_div.style.display = 'block';
        };
        
        this.hideCreate = function(reset) {
	
            this.create_transfer_button.textContent     = 'Create transfer';
            this.create_transfer_form_div.style.display = 'none'; 
            
            if (reset) {
                
                this.create_transfer_form.reset();
            }
        };
        
        /**
         * Method of the class TransferList that shows the transfersList with a GET call
         */
        this.show = function(accountCode) {
            // Requests and updates with the results
            var self = this;
            makeCall("GET", 'GetAccountDetails?accountCode=' + accountCode, null, (req) => {
	
                switch(req.status) {
	
                    case 200: //ok
                        var data = JSON.parse(req.responseText);
                        self.update(data.account, data.transfers, false);
                        break;
                        
                    case 400: // bad request
                    
                    case 401: // unauthorized
                    
                    case 500: // server error
                        self.update(null, req.responseText);
                        break;
                        
                    default: //Error
                        self.update(null, "Request reported status " + req.status);
                        break;
                }
            });
        };
        
        /**
         * Method of the class TransfersList that hides the transfersList
         */
        this.hide = function() {
	
            this.account_details.style.display = "none";
            // Also resets form
            this.hideCreate(true); 
        };
        
        /**
         * Method of the class TransfersList that updates the interface by binding the informations to the html elements
         */
        this.update = function(account, transfers, error_message) {
            //Hides content while refreshing
            this.hide();
            //Init headers
            this.account_code_span.textContent    = account.code;
            this.account_balance_span.textContent = account.balance;
            //Init message
            this.transfers_message.className     = (error_message ? "warning-message" : "");
            this.transfers_message.style.display = (error_message || transfers.length === 0 ? "block" : "none");// invert order
            //Init hidden data
            this.source_code.value = account.code;

            //Clears content
            this.transfers.innerHTML = "";
            
            if (error_message) {
	
                this.transfers_message.textContent = error_message;
                this.account_details.style.display = "block";
                return;
            
            } else if (transfers.length === 0) {
                
                this.transfers_message.textContent = "You have no transfers for this account :(";
                this.account_details.style.display = "block";
                return;
            }
            
            // For each transfer in transfers 
            transfers.forEach((transfer) => {

                let card, card_title, card_data, b1, br, b2, amount_div;
				
				// Links the card to the element
                card           = document.createElement("div");
                card.className = "linked-card linked-card-blue";
				
				// Creates the title of the card - which specifies the source or destination account (depending on the amount, if its negative or positive)
                card_title             = document.createElement("div");
                card_title.className   = "linked-card-title";
                
                card_title.textContent = (transfer.account_code_orderer === account.code ? "Destination Account: " + transfer.account_code_beneficiary : "Source Account: " + transfer.account_code_orderer);
                card.appendChild(card_title);
                
                // Links the the card_data element 
                card_data           = document.createElement("div");
                card_data.className = "linked-card-data";
				
				// Adds the timestamp of the transfer
                b1             = document.createElement("b");
                b1.textContent = "Timestamp: ";
                card_data.appendChild(b1);
                card_data.appendChild(document.createTextNode(transfer.timestamp));
                
                // Adds a divider 
                br = document.createElement("br");
                card_data.appendChild(br);

				// Adds the reason of the transfer
                b2             = document.createElement("b");
                b2.textContent = "Reason: ";
                card_data.appendChild(b2);
                card_data.appendChild(document.createTextNode(transfer.reason));
				
				// Adds the transfer amount
                amount_div           = document.createElement("div");
                amount_div.className = "transfers-amount " + (transfer.account_code_orderer === account.code ? "negative" : "positive");
                amount_div.appendChild(document.createTextNode((transfer.account_code_orderer === account.code ? "-" : "+" ) + transfer.amount + "\u20AC")); // "\u20AC" is the unicode character for "€"
                card_data.appendChild(amount_div);
                
                // Appends the card_data to the card
                card.appendChild(card_data);

				// Appends the card
                this.transfers.appendChild(card);
            });
            
            this.account_details.style.display = "block";
        };
    }
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor of the class TransferResult
	 * --------------------------------------------------------------------------------------------------------------
	 * Description: The TransferResult class handles the transfer confimation
	 *              or failure binding them with the html elements
	 *
	 * @param {*} options The html elements of the TrasferResult interface
	 */
    function TransferResult(options){
        //Saves html elements in scope
        this.result_div                    = options["result_div"];
        this.confirmed_div                 = options["confirmed_div"];
        this.failed_div                    = options["failed_div"];
        this.src_account_code_span         = options["src_account_code_span"];
        this.src_owner_id_span             = options["src_owner_id_span"];
        this.src_account_balance_old_span  = options["src_account_balance_old_span"];
        this.src_account_balance_span      = options["src_account_balance_span"];
        this.transfer_amount_span          = options["transfer_amount_span"];
        this.transfer_reason_span          = options["transfer_reason_span"];
        this.dest_account_code_span        = options["dest_account_code_span"];
        this.dest_owner_id_span            = options["dest_owner_id_span"];
        this.dest_account_balance_old_span = options["dest_account_balance_old_span"];
        this.dest_account_balance_span     = options["dest_account_balance_span"];
        this.failed_reason_span            = options["failed_reason_span"];
        this.close_success_button          = options["close_success_button"];
        this.close_failed_button           = options["close_failed_button"];

        //Setups listeners
        this.close_success_button.addEventListener("click", e => {
	
            this.result_div.style.display = 'none';
        });
        
        this.close_failed_button.addEventListener("click", e => {
	
            this.result_div.style.display = 'none';
        });

		/**
		 * Method of the class TransfersResult that shows the successful Transfer interface
		 */
        this.showSuccess = function(srcAccount, transfer, destAccount) {
            //Update spans
            this.src_account_code_span.textContent         = srcAccount.code;
            this.src_owner_id_span.textContent             = srcAccount.user_id;
            this.src_account_balance_old_span.textContent  = srcAccount.balance + transfer.amount;
            this.src_account_balance_span.textContent      = srcAccount.balance;
            this.transfer_amount_span.textContent          = transfer.amount;
            this.transfer_reason_span.textContent          = transfer.reason;
            this.dest_account_code_span.textContent        = destAccount.code;
            this.dest_owner_id_span.textContent            = destAccount.user_id;
            this.dest_account_balance_old_span.textContent = destAccount.balance - transfer.amount;
            this.dest_account_balance_span.textContent     = destAccount.balance;
            //Setup visibility
            addressBook.showButton(destAccount.user_id, destAccount.code);
            this.show(true);
        };
        
        /**
		 * Method of the class TransfersResult that shows the failed Transfer interface
		 */
        this.showFailure = function(reason) {
            //Update spans
            this.failed_reason_span.textContent = reason;
            //Setup visibility
            this.show(false);
        }
        
        /**
		 * Method of the class TransfersResult modifies the display depending if the transaction was successful or not
		 */
        this.show = function(displaySuccess) {
	
            this.confirmed_div.style.display = (displaySuccess ? 'block' : 'none');
            this.failed_div.style.display    = (displaySuccess ? 'none' : 'block');
            this.result_div.style.display    = 'block';
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor of the class AddressBook
     * --------------------------------------------------------------------------------------------------------------
     * Description: The AddressBook class takes care of the account 
     *              memorized and used for autocomplete on transfer form
     *
     * Notes:
     * - the address book is stored as a "map" inside contacts variable. Actually, is parsed
     *   from json as an Object, with properties destIDs, each with an associated List of destAccounts.
     * 
     * - destIDs are converted into a List with Object.keys(), as we needed to do also partial matching.
     *   Because destIDs is a List of String (property names), when actually elements are numbers, 
     *   and the inputDestID.value is a number, we needed an Array function which would compare elements 
     *   with autocasting.
     *   This is why we introduced method Array.contains(el):boolean (utils.js)
     *
     * @param {*} _add_contact                The add contact html element
     * @param {*} _destination_user_span      The destination user span html element
     * @param {*} _destination_account_span   The destination account span html element 
     * @param {*} _add_contact_warning_div    The add contact warning html element
     * @param {*} _create_transfer_warning    The create transfer warning html element
     * @param {*} _dest_ids_datalist          The destination user ids datalist html element 
     * @param {*} _dest_accounts_datalist     The destination account datalist html element
     */
    function AddressBook(
        _add_contact, 
        _destination_user_span,
        _destination_account_span, 
        _add_contact_warning_div, 
        _create_transfer_warning,
        _dest_ids_datalist,
        _dest_accounts_datalist){

        this.add_contact                = _add_contact;
        this.destination_account_span   = _destination_account_span;
        this.destination_user_span      = _destination_user_span;
        this.add_contact_warning_div    = _add_contact_warning_div;
        this.create_transfer_warning    = _create_transfer_warning;
        this.dest_ids_datalist          = _dest_ids_datalist;
        this.dest_accounts_datalist     = _dest_accounts_datalist;
        this.contacts = [];
        
        var self = this;

		// Adds an eventListener to the add_contact button
        this.add_contact.addEventListener("click", (e) => {
	
            e.target.style.display = "none";

            var destUsrId   = self.destination_user_span.textContent;
            var destAccCode = self.destination_account_span.textContent;
            self.addContact(destUsrId, destAccCode);
        });

		/**
		 * Method of the class AddressBook that loads with a get the content of the AddressBook
		 */
        this.load = function() {
	
            makeCall("GET", "GetContacts", null, (req) => {
	
                switch(req.status) {
	
                    case 200: //ok
                        self.create_transfer_warning.style.display = "none";
                        self.contacts = JSON.parse(req.responseText);
                        break;
                        
                    case 400: // bad request
                    
                    case 401: // unauthorized
                    
                    case 500: // server error
                    
                    default: //Error
                        self.create_transfer_warning.textContent   = "Unable to load your contacts";
                        self.create_transfer_warning.style.display = "block";
                        break;
                }
            });
        };
        
        /**
         * Method of the class AddressBook that shows the addContact button
         */
        this.showButton = function(destUserId, destAccountCode){
            
            self.add_contact_warning_div.style.display    = "none";

            if (self.contacts[destUserId]) {
	
                if (self.contacts[destUserId].includes(destAccountCode)) {
	
                    self.add_contact.style.display           = "none";
                    return;
                }
            }
            
            self.add_contact.style.display = "block";
        };
		
		/**
		 * Method of the class AddressBook that adds the Contact with a POST
		 */
        this.addContact = function(destUserId, destAccountCode) {
            
            // TODO: Error solved with a workaround
            // this solution does not work: https://stackoverflow.com/questions/45156413/xmlhttprequest-formdata-not-submitting-data
            // FormData created here does not work as expected -> once the form is sent the request received by the server is null
            // [NOT WORKING CODE]:
            // Creates form data
            // var data = new FormData();
            // data.append("contactId", destUserId);
            // data.append("contactAccountCode", destAccountCode);
            // Sends data
            // makeCall("POST", "AddContact", form, (req) => {
			// [END OF NOT WORKING CODE]
			
            makeCall("POST", "AddContact?contactId=" + destUserId + "&contactAccountCode=" + destAccountCode, null, (req) => {
	
                switch(req.status) {
	
                    case 200: // ok
                        self.load();
                        break;
                        
                    case 400: // bad request
                    
                    case 401: // unauthorized
                    
                    case 500: // server error
                        self.add_contact_warning_div.textContent      = req.responseText;
                        self.add_contact_warning_div.style.display    = "block";
                        break;
                        
                    default: // error
                        self.add_contact_warning_div.textContent      = "Request reported status " + req.status;
                        self.add_contact_warning_div.style.display    = 'block';
                        break;
                }
            });
        };

		/**
		 * Method of the class AddressBook that handles the autocomplete of the destinationUserId
		 */
        this.autocompleteDest = function(dest_id) {
            // Clears suggestions
            this.dest_ids_datalist.innerHTML      = "";
            this.dest_accounts_datalist.innerHTML = "";
            // Gets dest match
            var destIDs = Object.keys(this.contacts);
            // If not already matched
            if (!destIDs.contains(dest_id)) { 
                // Loads partial suggestions
                let similarIDs = [];
                destIDs.forEach(dest => {
					// If matches start
                    if (String(dest).startsWith(dest_id)) {
                    	// Adds to suggested
                        similarIDs.push(dest);
                    }
                });
                
                similarIDs.forEach(dest => {
	
                    let option   = document.createElement("option");
                    option.text  = dest;
                    option.value = dest;
                    this.dest_ids_datalist.appendChild(option);
                });
            }
        };
        
        /**
		 * Method of the class AddressBook that handles the autocomplete of the destinationAccountCode
		 */
        this.autocompleteAccount = function(dest_id, account_code, current_account) {
            // Clears suggestions
            this.dest_ids_datalist.innerHTML      = "";
            this.dest_accounts_datalist.innerHTML = "";
            // Gets dest match
            var destIDs = Object.keys(this.contacts);
            // If already matched
            if (destIDs.contains(dest_id)) { 
                // Dest is already okay, suggests his accounts
                let accountCodes = this.contacts[dest_id];
                // If not already matched
                if (!accountCodes.contains(account_code)) { 

                    let similarCodes = [];
                    accountCodes.forEach(account => {
						// Similar (starts with the same numbers), but not this account
                        if (String(account).startsWith(account_code) && account != current_account) {
                            // Adds to suggested
                            similarCodes.push(account);
                        }
                    });
                    
                    similarCodes.forEach(account => {
	
                        let option   = document.createElement("option");
                        option.text  = account;
                        option.value = account;
                        this.dest_accounts_datalist.appendChild(option);
                    });
                }
            }
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

})();