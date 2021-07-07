
(function ($) {
    "use strict";

    /*==================================================================
    [ Focus input ]*/
    $('.input100').each(function(){
        // if($(this).val().trim() != "") {
        //     $(this).addClass('has-val');
        //     console.log("Focus ON");
        // }
        $(this).on('blur', function(){
            if($(this).val().trim() != "") {
                $(this).addClass('has-val');
                console.log("Focus ON");
            }
            else {
                $(this).removeClass('has-val');
                console.log("Focus OFF");
            }
        })    
    })
  
  
    /*==================================================================
    [ Validate ]*/
    var input = $('.validate-input .input100');
    console.log("Input Length = " + input.length);

    $('.validate-form').on('submit',function(){
        var check = true;

        for(var i=0; i<input.length; i++) {
            if(validate(input[i]) == false){
                showValidate(input[i]);
                console.log("Input[" + i + "] = " + input[i]);
                check=false;
            }
        }

        if(check == true) {
            var authJSON = {
                "email": $("#email").val(),
                "password": $("#password").val()
            };
            console.log(authJSON);

            var auth = {
                url: "/api/users/auth",
                type: "POST",
                timeout: 0,
                contentType: "application/json",
                data: JSON.stringify(authJSON),
                dataType: "json",
                success: function (result, status, xhr) {
                    console.log("Status is " + status);
                    console.log("Result is " + result.message);
                    var jsonResult = jQuery.parseJSON(xhr.responseText);
                    console.log("XHR length is " + jsonResult.length);
                    console.log("XHR is " + jsonResult.value);
                    if (result.description == "OK") {
                        alert("Added successfully");
                        setTimeout(function () {
                            window.location = "/";
                        }, 2000);
                    } else {
                        window.location = "/registration.html";
                    }
                },
                error: function (xhr, status, error) {
                    var jsonError = jQuery.parseJSON( xhr.responseText );
                    var desc = (jsonError != "") ? jsonError.message : "no details";

                    alert("Result: " + status + " " + error + " " +
                        xhr.status + " " + jsonError.value("path") + ": " + desc);
                }
            };
            console.log(auth);

            $.ajax(auth).done(function (response) {
                console.log(response);
                // var tokenJSON = JSON.parse(response);
                // console.log(response);
                window.location = "/index.html";
            });
        }

        return check;
    });


    $('.validate-form .input100').each(function(){
        $(this).focus(function(){
           hideValidate(this);
        });
    });

    function validate (input) {
        if($(input).attr('type') == 'email' || $(input).attr('name') == 'email') {
            if($(input).val().trim().match(/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
                return false;
            }
        }
        else {
            if($(input).val().trim() == ''){
                return false;
            }
        }
    }

    function showValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).addClass('alert-validate');
    }

    function hideValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).removeClass('alert-validate');
    }
    
    
})(jQuery);