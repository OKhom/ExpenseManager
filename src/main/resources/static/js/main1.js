$(document).ready(function(){
    // $.getJSON('/account', function(data) {
    //     $('#login').text(data.email);
    //     $("#avatar").attr("src", data.pictureUrl);
    // });
    console.log("/index.html loaded...")
    showUserInformation();


    // assignButtons();
    // loadPages();
    // loadData(0);
    // focusInput();
    // validateInput();
});

$( window ).on( "load", function() {
    console.log( "WINDOW /index.html loaded..." );
});

function logoutClick () {
    $('#logoutButton').click(function () {
        doLogout();
        // window.location.reload();
    })
}