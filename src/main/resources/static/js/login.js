$(document).ready(function(){
    // $.getJSON('/account', function(data) {
    //     $('#login').text(data.email);
    //     $("#avatar").attr("src", data.pictureUrl);
    // });
    if (getLastUser() != null) {
        $('#email').val(getLastUser())
            .addClass('has-val');
    } else {
        $('#email').empty()
            .removeClass('has-val');
    }
    console.log("On LOAD /login.html Token is " + getJwtToken());
    // $('.input100').each(function () {
    //     // if ($(this).val().trim() != "") {
    //         $(this).addClass('has-val');
    //     // }
    // });

    // assignButtons();
    // loadPages();
    // loadData(0);
    focusInput();
    validateInput();
});

// (function ($) {
//     "use strict";
function focusInput() {
    /*==================================================================
    [ Focus input ]*/
    $('.input100').each(function () {
        $(this).on('blur', function () {
            if ($(this).val().trim() != "") {
                $(this).addClass('has-val');
                console.log("Focus ON");
            } else {
                $(this).removeClass('has-val');
                console.log("Focus OFF");
            }
        })
    });
}

function validateInput() {
    /*==================================================================
    [ Validate ]*/
    var input = $('.validate-input .input100');
    console.log("Input Length = " + input.length);

    $('.validate-form').on('submit', function () {
        var check = true;

        for (var i = 0; i < input.length; i++) {
            if (validate(input[i]) == false) {
                showValidate(input[i]);
                console.log("Input[" + i + "] = " + input[i]);
                check = false;
            }
        }

        if (check === true) {
            var loginData = {
                "email": $("#email").val(),
                "password": $("#password").val()
            };
            console.log(loginData);

            doLogin(loginData);
            console.log("From 'Login1.js' - Token is " + getJwtToken());
            // window.location.assign("/index.html");


            // var auth = {
            //     url: "/api/users/auth",
            //     type: "POST",
            //     timeout: 0,
            //     contentType: "application/json",
            //     data: JSON.stringify(loginData),
            //     dataType: "json",
            //     success: function (result, status, xhr, data, dataType) {
            //         console.log("Status is " + status);
            //         console.log("Result is " + xhr);
            //         var jsonResult = jQuery.parseJSON(xhr.responseText);
            //         var currentToken = jsonResult.token;
            //             console.log("Token is " + jsonResult.token);
            //         console.log("XHR is " + xhr.getAllResponseHeaders());
            //         if (xhr.status == 200) {
            //             console.log("Added successfully");
            //             setTimeout(function () {
            //                 toUrl("/index.html", currentToken);
            //                 // window.location = "/index.html";
            //             }, 2000);
            //         } else {
            //             console.log("Register successfully")
            //             // window.location = "/registration.html";
            //         }
            //     },
            //     error: function (xhr, status, error) {
            //         var jsonError = jQuery.parseJSON(xhr.responseText);
            //         var desc = (jsonError != "") ? jsonError.message : "no details";
            //
            //         alert("Result: " + status + " " + error + " " +
            //             xhr.status + " " + jsonError.value("path") + ": " + desc);
            //     }
            // };
            // console.log(auth);

            // $.ajax(auth).done(function (response) {
            //     console.log(response);
            //     var tokenJSON = response;
            //     // var tokenJSON = jQuery.parseJSON(response);
            //     console.log(tokenJSON);
            //     // window.location = "/index.html";
            // });
        }

        return check;
    });


    $('.validate-form .input100').each(function () {
        $(this).focus(function () {
            hideValidate(this);
        });
    });
}

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

    function toUrl(url, token) {
        $.ajax({
            type: "GET",
            url: url,
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (result, status, xhr) {
                // window.location = url;
                // if (result.description == "OK") {
                //     $("#messageSpan").text("Added successfully");
                //     setTimeout(function () {
                //         window.location = "/";
                //     }, 2000);
                // } else {
                //     $("#messageSpan").text("Error occured!");
                // }
            },
            error: function (xhr, status, error) {
                var jsonError = jQuery.parseJSON(xhr.responseText);
                var desc = (jsonError != "") ? jsonError.message : "no details";

                console.log("Result: " + status + " " + error + " " +
                    xhr.status + " " + xhr.statusText + ": " + desc);
            }
        });
    }
    
    
// })(jQuery);