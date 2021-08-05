$(document).ready(function(){
    $("#containerCurrencies").hide();
    $("#containerUsers").show();
    $("#adminHead").empty().text('ADMIN (Users)');
    loadData(0);
    loadPages("#pages");

	showAdminInformation(function (data) {
	    if (data.role != "ADMIN") {
            window.location.assign("/index.html");
        }
        $("#navUserName").text(data.firstName + " " + data.lastName);
        $("#firstNameProfile").text(data.firstName);
        $("#lastNameProfile").text(data.lastName);
        $("#emailProfile").text(data.email);
        $("#roleProfile").text(data.role);
    });
});

(function($) {
    "use strict"; // Start of use strict

    // Toggle the side navigation
    $("#sidebarToggle, #sidebarToggleTop").on('click', function(e) {
    $("body").toggleClass("sidebar-toggled");
    $(".sidebar").toggleClass("toggled");
    if ($(".sidebar").hasClass("toggled")) {
      $('.sidebar .collapse').collapse('hide');
    };
    });

    // Toggle the selected PAGE button for Users
    $("#pages").on("click", ".page-link", function(event) {
        loadData(event.target.id);
        usersCount();
    });

    // Toggle the selected PAGE button for Currencies
    $("#pagesCurrency").on("click", ".page-link", function(event) {
        loadCurrencyData(event.target.id);
        usersCount();
    });

    // Toggle the DELETE USERS button
    $("#btnUsersDelete").on('click', function() {
        let checkList = $(":checked");
        if (checkList.length > 0) {
            let idList = {'usersId' : []};
            checkList.each(function() {
                idList['usersId'].push($(this).val());
            });
            deleteUsers(idList);
        } else {
            $("#deleteUsersErrorModal").modal('show');
        }
    });

    // Toggle the DELETE CURRENCIES button
    $("#btnCurrencyDelete").on('click', function() {
        let checkboxList = $(":checked");
        if (checkboxList.length > 0) {
            let idList = {'currenciesId' : []};
            checkboxList.each(function() {
                idList['currenciesId'].push($(this).val());
            });
            deleteCurrencies(idList);
        } else {
            $("#deleteCurrencyErrorModal").modal('show');
        }
    });

    // Call Add Currency Modal Form
    $("#btnCurrencyAdd").on('click', function() {
        $("#addCurrencyModal").modal('show');
    })

    // Toggle Add Currency Button
    $("#submitNewCurrency").on('click', function () {
        if ($("#inputCurrencyName").val() != "" && $("#inputCurrencyShortName").val() != "" && $("#inputCurrencySign").val() != "" ) {
            let newCurrency = {
                name: $("#inputCurrencyName").val(),
                shortName: $("#inputCurrencyShortName").val(),
                sign: $("#inputCurrencySign").val()
            }
            addCurrency(newCurrency);
        } else {
            $("#modalAddCurrencyErrorText").text("New Currency fields is empty or invalid. Try again...");
        }
    })

    // Clear New Currency Modal Form
    $("#addCurrencyModal").on('hidden.bs.modal', function () {
        let modal = $(this);
        modal.find('input').val('').end();
        $("#modalAddCurrencyErrorText").empty();
    })

    // Call Edit Currency Modal Form
    $("#btnCurrencyEdit").on('click', function() {
        let checkboxList = $(":checked");
        if (checkboxList.length === 1) {
            setCurrencyId(checkboxList.val());
            $('#editCurrencyHeader').empty().text("Edit Currency ID = ");
            $('#editCurrencyId').empty().text(getCurrencyId());
            loadCurrencyById(getCurrencyId(), function (data) {
                $("#inputEditedCurrencyName").empty().val(data.name);
                $("#inputEditedCurrencyShortName").empty().val(data.shortName);
                $("#inputEditedCurrencySign").empty().val(data.sign);
                $("#editCurrencyModal").modal('show');
            })
        } else {
            $("#editCurrencyErrorModal").modal('show');
        }
    })

    // Toggle Edit Currency Button
    $("#submitEditCurrency").on('click', function () {
        if ($("#inputEditedCurrencyName").val() != "" && $("#inputEditedCurrencyShortName").val() != "" && $("#inputEditedCurrencySign").val() != "" ) {
            let editedCurrency = {
                name: $("#inputEditedCurrencyName").val(),
                shortName: $("#inputEditedCurrencyShortName").val(),
                sign: $("#inputEditedCurrencySign").val()
            }
            let currencyId = getCurrencyId();
            editCurrency(currencyId, editedCurrency);
        } else {
            $("#modalEditCurrencyErrorText").text("Edited Currency fields is empty or invalid. Try again...");
        }
    })

    // Clear Edit Currency Modal Form
    $("#editCurrencyModal").on('hidden.bs.modal', function () {
        let modal = $(this);
        modal.find('input').val('').end();
        $("#modalEditCurrencyErrorText").empty();
    })

    // Toggle Search Topbar Button
    $("#submitSearchTopbar").on('click', function () {
        if ($("#inputSearchTopbar").val() != "") {
            let searchPattern = $("#inputSearchTopbar").val();
            if ($("#containerUsers").is(":visible")) {
                loadUsersDataByPattern(searchPattern);
            } else if ($("#containerCurrencies").is(":visible")) {
                loadCurrenciesDataByPattern(searchPattern);
            }
            $("#inputSearchTopbar").val('').end();
        }
    })

    // Toggle Search Dropdown Button
    $("#submitSearchDropdown").on('click', function () {
        if ($("#inputSearchDropdown").val() != "") {
            let searchPattern = $("#inputSearchDropdown").val();
            if ($("#containerUsers").is(":visible")) {
                loadUsersDataByPattern(searchPattern);
            } else if ($("#containerCurrencies").is(":visible")) {
                loadCurrenciesDataByPattern(searchPattern);
            }
            $("#inputSearchDropdown").val('').end();
        }
    })

    // Toggle Home Button
    $("#selectHome").on('click', function () {
        // toHome();
        showAdminInformation(function (data) {
            if (data.role == "ADMIN") {
                window.location.assign("/admin.html");
            } else {
                window.location.assign("/index.html");
            }
        });
    })

    // Show Edit User Modal
    $("#selectEditUser").on('click', function() {
        loadAllCurrencies(function (data) {
            $('#inputNewUserCurrency').empty()
                .append($('<option>').attr('selected', '').attr('value', '0')
                    .append('User currency')
                );
            for (let i = 0; i < data.length; i++) {
                $('#inputNewUserCurrency')
                    .append($('<option>').attr('class', 'font-weight-normal').attr('value', data[i].currencyId)
                        .append(data[i].shortName + ': ' + data[i].name)
                    );
            }
        });
        setTimeout(function () {
            showUserInformation(function (data) {
                $('#editUserHeader').empty().append("Edit profile");
                $('#inputNewFirstName').empty().val(data.firstName);
                $('#inputNewLastName').empty().val(data.lastName);
                $('#inputNewUserCurrency').val(data.currencyId);
            });
            $("#editUserModal").modal('show');
        }, 70);
    })

    // Toggle Edit User
    $("#submitEditUser").on('click', function() {
        if ($("#inputNewFirstName").val() != "" && $("#inputNewLastName").val() != "") {
            let currencyId;
            if ($("#inputNewUserCurrency").val() == 0) {
                currencyId = getUserCurrency();
            } else {
                currencyId = $("#inputNewUserCurrency").val();
            }
            let updatedUser = {
                "firstName": $("#inputNewFirstName").val(),
                "lastName": $("#inputNewLastName").val(),
                "currencyId": currencyId
            };
            updateUser(updatedUser);
        } else {
            $("#modalEditUserErrorText").text("User's FirstName or LastName is empty. Try again...");
        }
    })

    // Clear Edit User Modal Form
    $("#editUserModal").on('hidden.bs.modal', function () {
        let modal = $(this);
        modal.find('select').val('0');
        modal.find('input').val('').end();
        $("#modalEditUserErrorText").empty();
    })

    // Toggle Users
    $("#selectUsers").click(function() {
        if ($("#containerUsers").is(":hidden")) {
            $("#containerCurrencies").hide();
            $("#containerUsers").show();
            // $("#adminHead").empty().text('ADMIN (Users)');
        }
        loadData(0);
        loadPages("#pages");
    });

    // Toggle Currencies
    $("#selectCurrencies").click(function() {
        if ($("#containerCurrencies").is(":hidden")) {
            $("#containerUsers").hide();
            $("#containerCurrencies").show();
            // $("#adminHead").empty().text('ADMIN (Currencies)');
        }
        loadCurrencyData(0);
        loadPages("#pagesCurrency");
    });
    // *********************************************************************************

  // Close any open menu accordions when window is resized below 768px
  $(window).resize(function() {
    if ($(window).width() < 768) {
      $('.sidebar .collapse').collapse('hide');
    };
    
    // Toggle the side navigation when window is resized below 480px
    if ($(window).width() < 480 && !$(".sidebar").hasClass("toggled")) {
      $("body").addClass("sidebar-toggled");
      $(".sidebar").addClass("toggled");
      $('.sidebar .collapse').collapse('hide');
    };
  });

  // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
  $('body.fixed-nav .sidebar').on('mousewheel DOMMouseScroll wheel', function(e) {
    if ($(window).width() > 768) {
        let e0 = e.originalEvent,
            delta = e0.wheelDelta || -e0.detail;
        this.scrollTop += (delta < 0 ? 1 : -1) * 30;
      e.preventDefault();
    }
  });

  // Scroll to top button appear
  $(document).on('scroll', function() {
      let scrollDistance = $(this).scrollTop();
      if (scrollDistance > 100) {
      $('.scroll-to-top').fadeIn();
    } else {
      $('.scroll-to-top').fadeOut();
    }
  });

  // Smooth scrolling using jQuery easing
  $(document).on('click', 'a.scroll-to-top', function(e) {
      let $anchor = $(this);
      $('html, body').stop().animate({
      scrollTop: ($($anchor.attr('href')).offset().top)
    }, 1000, 'easeInOutExpo');
    e.preventDefault();
  });

})(jQuery); // End of use strict
