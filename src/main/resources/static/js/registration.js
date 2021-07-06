$(document).ready(function(){
    // $.getJSON('/account', function(data) {
    //     $('#login').text(data.email);
    //     $("#avatar").attr("src", data.pictureUrl);
    // });
    console.log("On LOAD /login.html Token is " + getJwtToken());
    // $('.input100').each(function () {
    //     // if ($(this).val().trim() != "") {
    //         $(this).addClass('has-val');
    //     // }
    // });

    focusInput();
    validateInput();
});

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

    $('.register100-btn').click(function () {
        var check = true;

        for (var i = 0; i < input.length; i++) {
            if (validate(input[i]) == false) {
                showValidate(input[i]);
                console.log("Input[" + i + "] = " + input[i]);
                check = false;
            }
        }

        if (check === true) {
            var registerData = {
                "firstName": $("#name").val(),
                "lastName": $("#lastname").val(),
                "email": $("#email").val(),
                "password": $("#password").val()
            };
            console.log(registerData);
            doRegister(registerData);
            // window.location.assign("/index.html");
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
