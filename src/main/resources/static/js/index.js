$(document).ready(function(){
    let currentDate = new Date();
    setCurrentMonth(currentDate.getMonth());
	setCurrentYear(currentDate.getFullYear());
	setCategoryType(0);

	$("#container-transactions").hide();
	$("#searchDropdown").hide();
	$("#currentDateView").text(getMonthName(getCurrentMonth()) + " " + getCurrentYear());
	$("#categoryHead").empty().text('CATEGORIES - Expense');
	showUserInformation(function (data) {
        if (data.role == "ADMIN") {
            window.location.assign("/admin.html");
        }
        $("#navUserName").text(data.firstName + " " + data.lastName);
        $("#firstNameProfile").text(data.firstName);
        $("#lastNameProfile").text(data.lastName);
        $("#emailProfile").text(data.email);
        $("#currencyProfile").text("(" + data.currencyShortName + ") " + data.currencyName);
        $("#roleProfile").text(data.role);
        setUserCurrency(data.currencyId);
    });
    loadCategoryCards();
    loadTotalAmount();
});

(function($) {
    "use strict"; // Start of use strict

    // Toggle the side navigation
    $("#sidebarToggle, #sidebarToggleTop").on('click', function(e) {
    $("body").toggleClass("sidebar-toggled");
    $(".sidebar").toggleClass("toggled");
    if ($(".sidebar").hasClass("toggled")) {
      $('.sidebar .collapse').collapse('hide');
    }
    });
  
    // Initialize Bootstrap DatePicker
    $('#inputDate').datepicker({
    weekStart: 1,
    todayBtn: "linked",
    // language: "uk",
    daysOfWeekHighlighted: "0,6",
    autoclose: true,
    todayHighlight: true
    }).datepicker("setDate", new Date());

    // Toggle Home Button
    $("#selectHome").on('click', function () {
        showUserInformation(function (data) {
            if (data.role == "ADMIN") {
                window.location.assign("/admin.html");
            } else {
                window.location.assign("/index.html");
            }
        });
    })

    // Toggle Current Month
    $("#currentDateView").click(function() {
        let currentDate = new Date();
        setCurrentMonth(currentDate.getMonth());
        setCurrentYear(currentDate.getFullYear());
        $("#currentDateView").empty().text(getMonthName(getCurrentMonth()) + " " + getCurrentYear());
        loadTotalAmount();
        if ($("#container-categories").is(":visible")) {
            loadCategoryCards();
        } else if ($("#container-transactions").is(":visible")) {
            loadTransactionCards();
        }
    });
  
    // Toggle Previous Month
    $("#monthPrevious").click(function() {
        if (getCurrentMonth() == 0) {
            setCurrentMonth(11);
            setCurrentYear(parseInt(getCurrentYear()) - 1);
        } else {
            setCurrentMonth(parseInt(getCurrentMonth()) - 1);
        }
        $("#currentDateView").empty().text(getMonthName(getCurrentMonth()) + " " + getCurrentYear());
        loadTotalAmount();
        if ($("#container-categories").is(":visible")) {
            loadCategoryCards();
        } else if ($("#container-transactions").is(":visible")) {
            loadTransactionCards();
        }
    });
  
    // Toggle Next Month
    $("#monthNext").click(function() {
        if (getCurrentMonth() == 11) {
            setCurrentMonth(0);
            setCurrentYear(parseInt(getCurrentYear()) + 1);
        } else {
            setCurrentMonth(parseInt(getCurrentMonth()) + 1);
        }
        $("#currentDateView").empty().text(getMonthName(getCurrentMonth()) + " " + getCurrentYear());
        loadTotalAmount();
        if ($("#container-categories").is(":visible")) {
            loadCategoryCards();
        } else if ($("#container-transactions").is(":visible")) {
            loadTransactionCards();
        }
    });

    // $("#addSubcategoryModal").on('shown.bs.modal', function (event) {
    //     console.log("Value this selector is: " + $(event.relatedTarget).data('id'));
    //     setCurrentCategoryID($(event.relatedTarget).data('id'));
    // })

    // Call Add Transaction Modal Form
    $("#categoryCards").on('click', '#selectAddTransaction', function() {
        setCurrentCategoryID($(this).data('id'));
        loadCategoryById(function (data) {
            $('#addTransactionHeader').empty().append(data.name);
        });
        loadSubcategories(function (data) {
            $('#listSubcategory').empty()
                .append($('<option>').attr('selected', '').attr('value', '0')
                    .append('Subcategory')
                );
            for (let i = 0; i < data.length; i++) {
                $('#listSubcategory')
                    .append($('<option>').attr('class', 'font-weight-normal').attr('value', data[i].subcategoryId)
                        .append(data[i].subname)
                    );
            }
        });
        $("#addTransactionModal").modal('show');
    })

    // Toggle Add Transaction Button
    $("#submitNewTransaction").on('click', function () {
        if (parseFloat($("#transactionAmount").val()) > 0) {
            let pattern = /(\d{2})\/(\d{2})\/(\d{4})/;
            // let pattern = /(\d{2})\.(\d{2})\.(\d{4})/;
            let newTransaction = {
                amount: $("#transactionAmount").val(),
                note: $("#inputDescription").val(),
                date: $("#transactionDate").val().replace(pattern, '$3-$1-$2')
            }
            let currentCategoryID = getCurrentCategoryID();
            let currentSubcategory;
            if ($("#listSubcategory").val() == 0) {
                currentSubcategory = null;
            } else {
                currentSubcategory = $("#listSubcategory").val();
            }
            addTransaction(newTransaction, currentCategoryID, currentSubcategory);
        } else {
            $("#modalTransactionErrorText").text("Transaction amount is empty or invalid. Try again...");
        }
    })

    // Clear Add Transaction Modal Form
    $("#addTransactionModal").on('hidden.bs.modal', function () {
        setCurrentCategoryID(null);
        let modal = $(this);
        modal.find('select').val('0');
        modal.find('#inputDescription').val('').end();
        modal.find('#transactionAmount').val('').end();
        $("#modalTransactionErrorText").empty();
    })

    // Call Edit Transaction Modal Form
    $("#transactionCards").on('click', '#selectEditTransaction', function() {
        setCurrentCategoryID($(this).data('cat'));
        setCurrentTransactionID($(this).data('id'));
        loadSubcategories(function (data) {
            $('#listEditedSubcategory').empty()
                .append($('<option>').attr('selected', '').attr('value', '0')
                    .append('select Subcategory')
                );
            for (let i = 0; i < data.length; i++) {
                $('#listEditedSubcategory')
                    .append($('<option>').attr('class', 'font-weight-normal').attr('value', data[i].subcategoryId)
                        .append(data[i].subname)
                    );
            }
        });
        loadTransactionById(function (data) {
            $('#editTransactionHeader').empty().append(data.categoryName).append(" - edit Transaction");
            $('#listEditedSubcategory').val(data.subcategoryId);
            $('#inputEditedDescription').empty().val(data.note);
            $('#editTransactionAmount').empty().val(data.amount);
            $('#editDate').datepicker({
                weekStart: 1,
                todayBtn: "linked",
                // language: "uk",
                daysOfWeekHighlighted: "0,6",
                autoclose: true,
                todayHighlight: true
            }).datepicker("setDate", new Date(data.date));
        })
        $("#editTransactionModal").modal('show');
    })

    // Toggle Edit Transaction Button
    $("#submitUpdateTransaction").on('click', function () {
        if (parseFloat($("#editTransactionAmount").val()) > 0) {
            let pattern = /(\d{2})\/(\d{2})\/(\d{4})/;
            // let pattern = /(\d{2})\.(\d{2})\.(\d{4})/;
            let editedTransaction = {
                amount: $("#editTransactionAmount").val(),
                note: $("#inputEditedDescription").val(),
                date: $("#editTransactionDate").val().replace(pattern, '$3-$1-$2')
            }
            let currentSubcategory;
            if ($("#listEditedSubcategory").val() == 0) {
                currentSubcategory = null;
            } else {
                currentSubcategory = $("#listEditedSubcategory").val();
            }
            editTransaction(editedTransaction, currentSubcategory);
        } else {
            $("#modalEditTransactionErrorText").text("Transaction amount is empty or invalid. Try again...");
        }
    })

    // Clear Edit Transaction Modal Form
    $("#editTransactionModal").on('hidden.bs.modal', function () {
        setCurrentCategoryID(null);
        setCurrentTransactionID(null);
        let modal = $(this);
        modal.find('select').val('0');
        modal.find('#inputEditedDescription').val('').end();
        modal.find('#editTransactionAmount').val('').end();
        $("#modalDeleteTransactionErrorText").empty();
    })

    // Call Delete Transaction Modal Form
    $("#transactionCards").on('click', '#selectDeleteTransaction', function() {
        setCurrentCategoryID($(this).data('cat'));
        setCurrentTransactionID($(this).data('id'));
        loadTransactionById(function (data) {
            $('#deleteTransactionHeader').empty().append(data.categoryName).append(" - delete Transaction");
            $('#deletedSubcategory').empty().val(data.subcategory);
            $('#deletedDescription').empty().val(data.note);
            $('#deletedTransactionAmount').empty().val(data.amount);
            $('#deletedTransactionDate').empty().val(data.date);
        })
        $("#deleteTransactionModal").modal('show');
    })

    // Toggle Delete Transaction Button
    $("#submitDeleteTransaction").on('click', function () {
        deleteTransaction();
    })

    // Clear Delete Transaction Modal Form
    $("#deleteTransactionModal").on('hidden.bs.modal', function () {
        setCurrentCategoryID(null);
        setCurrentTransactionID(null);
        let modal = $(this);
        modal.find('select').val('0');
        modal.find('#deletedDescription').val('').end();
        modal.find('#deletedTransactionAmount').val('').end();
        $("#modalDeleteTransactionErrorText").empty();
    })

    // Call Add Budget Modal Form
    $("#categoryCards").on('click', '#selectAddBudget', function() {
        setCurrentCategoryID($(this).data('id'));
        loadCategoryById(function (data) {
            $('#addBudgetHeader').empty().append(data.name).append(" - add budget");
        });
        $("#addBudgetModal").modal('show');
    })

    // Toggle Add Budget Button
    $("#submitAddBudget").on('click', function () {
        if (parseFloat($("#budgetAmount").val()) > 0) {
            let newBudget = {
                budget: $("#budgetAmount").val()
            }
            let currentCategoryID = getCurrentCategoryID();
            addBudget(newBudget, currentCategoryID);
        } else {
            $("#modalBudgetErrorText").text("Budget amount is empty or invalid. Try again...");
        }
    })

    // Clear Budget Modal Form
    $("#addBudgetModal").on('hidden.bs.modal', function () {
        setCurrentCategoryID(null);
        let modal = $(this);
        modal.find('input').val('').end();
        $("#modalBudgetErrorText").empty();
    })

    // Call Edit Budget Modal Form
    $("#categoryCards").on('click', '#selectEditBudget', function() {
        setCurrentCategoryID($(this).data('id'));
        loadCategoryById(function (data) {
            $('#editBudgetHeader').empty().append(data.name).append(" - edit budget");
        });
        loadBudgetByCategoryId(function (data) {
            $('#newBudgetAmount').val(data.budget);
        });
        $("#editBudgetModal").modal('show');
    })

    // Toggle Edit Budget Button
    $("#submitEditBudget").on('click', function () {
        if (parseFloat($("#newBudgetAmount").val()) > 0) {
            let newBudget = {
                budget: $("#newBudgetAmount").val()
            }
            let currentCategoryID = getCurrentCategoryID();
            editBudget(newBudget, currentCategoryID);
        } else {
            $("#modalEditBudgetErrorText").text("Budget amount is empty or invalid. Try again...");
        }
    })

    // Clear Edit Budget Modal Form
    $("#editBudgetModal").on('hidden.bs.modal', function () {
        setCurrentCategoryID(null);
        let modal = $(this);
        modal.find('input').val('').end();
        $("#modalEditBudgetErrorText").empty();
    })

    // Call Delete Budget Modal Form
    $("#categoryCards").on('click', '#selectDeleteBudget', function() {
        setCurrentCategoryID($(this).data('id'));
        loadCategoryById(function (data) {
            $('#deleteBudgetHeader').empty().append(data.name).append(" - delete budget");
        });
        loadBudgetByCategoryId(function (data) {
            $('#deletedBudgetAmount').val(data.budget);
        });
        $("#deleteBudgetModal").modal('show');
    })

    // Toggle Delete Budget Button
    $("#submitDeleteBudget").on('click', function () {
        if (parseFloat($("#deletedBudgetAmount").val()) > 0) {
            deleteBudget();
        } else {
            $("#modalDeleteBudgetErrorText").text("Budget amount is empty or invalid. Try again...");
        }
    })

    // Clear Delete Budget Modal Form
    $("#deleteBudgetModal").on('hidden.bs.modal', function () {
        setCurrentCategoryID(null);
        let modal = $(this);
        modal.find('input').val('').end();
        $("#modalDeleteBudgetErrorText").empty();
    })

    // Call Add Subcategory Modal Form
    $("#categoryCards").on('click', '#selectAddSubcategory', function() {
        setCurrentCategoryID($(this).data('id'));
        loadCategoryById(function (data) {
            $('#addSubcategoryHeader').empty().append(data.name).append(" - add subcategory");
        });
        $("#addSubcategoryModal").modal('show');
    })

    // Toggle New Subcategory Button
    $("#submitNewSubcategory").on('click', function () {
        if ($("#inputSubcategory").val() != "") {
            let newSubcategory = {
                subname: $("#inputSubcategory").val()
            }
            let currentCategoryID = getCurrentCategoryID();
            addSubcategory(newSubcategory, currentCategoryID);
        } else {
            $("#modalSubcategoryErrorText").text("Subcategory name is empty. Try again...");
        }
    })

    // Clear Add Subcategory Modal Form
    $("#addSubcategoryModal").on('hidden.bs.modal', function () {
        setCurrentCategoryID(null);
        let modal = $(this);
        modal.find('input').val('').end();
        $("#modalSubcategoryErrorText").empty();
    })

    // Show Update Subcategory Modal
    $("#categoryCards").on('click', '#selectEditSubcategory', function() {
        setCurrentCategoryID($(this).data('id'));
        loadCategoryById(function (data) {
            $('#updateSubcategoryHeader').empty().append(data.name).append(" - update subcategory");
        });
        loadSubcategories(function (data) {
            $('#listUpdatingSubcategory').empty()
                .append($('<option>').attr('selected', '').attr('value', '0')
                    .append('select Subcategory')
                );
            for (let i = 0; i < data.length; i++) {
                $('#listUpdatingSubcategory')
                    .append($('<option>').attr('class', 'font-weight-normal').attr('value', data[i].subcategoryId)
                        .append(data[i].subname)
                    );
            }
        });
        $("#updateSubcategoryModal").modal('show');
    })

    // Update Subcategory Request
    $("#submitUpdateSubcategory").on('click', function () {
        if ($("#listUpdatingSubcategory").val() != 0 && $("#updatedSubcategory").val() != "") {
            let updatingSubcategory = $("#listUpdatingSubcategory").val();
            let newSubcategory = {
                subname: $("#updatedSubcategory").val()
            }
            updateSubcategory(updatingSubcategory, newSubcategory);
        } else {
            $("#modalUpdateSubcategoryErrorText").text("Old or New Subcategory name is empty. Try again...");
        }
    })

    // Clear Update Subcategory Modal Form
    $("#updateSubcategoryModal").on('hidden.bs.modal', function () {
        setCurrentCategoryID(null);
        let modal = $(this);
        modal.find('select').val('0');
        modal.find('input').val('').end();
        $("#modalUpdateSubcategoryErrorText").empty();
    })

    // Show Delete Subcategory Modal
    $("#categoryCards").on('click', '#selectDeleteSubcategory', function() {
        setCurrentCategoryID($(this).data('id'));
        loadCategoryById(function (data) {
            $('#deleteSubcategoryHeader').empty().append(data.name).append(" - delete subcategory");
        });
        loadSubcategories(function (data) {
            $('#listDeletedSubcategory').empty()
                .append($('<option>').attr('selected', '').attr('value', '0')
                    .append('Subcategory')
                );
            for (let i = 0; i < data.length; i++) {
                $('#listDeletedSubcategory')
                    .append($('<option>').attr('class', 'font-weight-normal').attr('value', data[i].subcategoryId)
                        .append(data[i].subname)
                    );
            }
        });
        $("#deleteSubcategoryModal").modal('show');
    })

    // Delete Subcategory Request
    $("#submitDeleteSubcategory").on('click', function () {
        if ($("#listDeletedSubcategory").val() != 0) {
            let deletedSubcategory = $("#listDeletedSubcategory").val();
            deleteSubcategory(deletedSubcategory);
        } else {
            $("#modalDeleteSubcategoryErrorText").text("Subcategory name is empty. Try again...");
        }
    })

    // Clear Delete Subcategory Modal Form
    $("#deleteSubcategoryModal").on('hidden.bs.modal', function () {
        setCurrentCategoryID(null);
        let modal = $(this);
        modal.find('select').val('0');
        $("#modalDeleteSubcategoryErrorText").empty();
    })

    // Show Category Modal Form
    $("#addCategoryModal").on('shown.bs.modal', function () {
        loadAllCurrencies(function (data) {
            $('#inputCategoryCurrency').empty()
                .append($('<option>').attr('selected', '').attr('value', '0')
                    .append('Category currency')
                );
            for (let i = 0; i < data.length; i++) {
                $('#inputCategoryCurrency')
                    .append($('<option>').attr('class', 'font-weight-normal').attr('value', data[i].currencyId)
                        .append(data[i].shortName + ': ' + data[i].name)
                    );
            }
        });
    })

    // Toggle Add Category
    $("#submitNewCategory").on('click', function() {
        if ($("#inputCategory").val() != "") {
            let currencyId;
            if ($("#inputCategoryCurrency").val() == 0) {
                currencyId = getUserCurrency();
            } else {
                currencyId = $("#inputCategoryCurrency").val();
            }
            let newCategory = {
                "name": $("#inputCategory").val(),
                "description": $("#inputCategoryDescription").val(),
                "currencyId": currencyId
            };
            let currentCategoryType = getCategoryType();
            addCategory(newCategory, currentCategoryType);
        } else {
            $("#modalErrorText").text("Category name is empty. Try again...");
        }
    })

    // Clear Category Modal Form
    $("#addCategoryModal").on('hidden.bs.modal', function () {
        let modal = $(this);
        modal.find('select').val('0');
        modal.find('input').val('').end();
        $("#modalErrorText").empty();
    })

    // Show Edit Category Modal
    $("#categoryCards").on('click', '#selectEditCategory', function() {
        setCurrentCategoryID($(this).data('id'));
        loadAllCurrencies(function (data) {
            $('#inputNewCategoryCurrency').empty()
                .append($('<option>').attr('selected', '').attr('value', '0')
                    .append('Category currency')
                );
            for (let i = 0; i < data.length; i++) {
                $('#inputNewCategoryCurrency')
                    .append($('<option>').attr('class', 'font-weight-normal').attr('value', data[i].currencyId)
                        .append(data[i].shortName + ': ' + data[i].name)
                    );
            }
        });
        loadCategoryById(function (data) {
            $('#editCategoryHeader').empty().append(data.name).append(" - edit category");
            $('#inputNewCategory').empty().val(data.name);
            $('#inputNewCategoryCurrency').val(data.currencyId);
            $('#inputNewCategoryDescription').empty().val(data.description);
        });
        $("#editCategoryModal").modal('show');
    })

    // Toggle Edit Category
    $("#submitEditCategory").on('click', function() {
        if ($("#inputNewCategory").val() != "") {
            let currencyId;
            if ($("#inputNewCategoryCurrency").val() == 0) {
                currencyId = getUserCurrency();
            } else {
                currencyId = $("#inputNewCategoryCurrency").val();
            }
            let updatedCategory = {
                "name": $("#inputNewCategory").val(),
                "description": $("#inputNewCategoryDescription").val(),
                "currencyId": currencyId
            };
            updateCategory(updatedCategory);
        } else {
            $("#modalEditCategoryErrorText").text("Category name is empty. Try again...");
        }
    })

    // Clear Edit Category Modal Form
    $("#editCategoryModal").on('hidden.bs.modal', function () {
        let modal = $(this);
        modal.find('select').val('0');
        modal.find('input').val('').end();
        $("#modalEditCategoryErrorText").empty();
    })

    // Show Delete Category Modal
    $("#categoryCards").on('click', '#selectDeleteCategory', function() {
        setCurrentCategoryID($(this).data('id'));
        loadCategoryById(function (data) {
            $('#deleteCategoryHeader').empty().append(data.name).append(" - delete category");
        });
        $("#deleteCategoryModal").modal('show');
    })

    // Delete Category Request
    $("#submitDeleteCategory").on('click', function () {
        let deleteCategoryWithAllTransactions = false;
        if ($("#deleteCategoryWithAllTransactions").is(":checked")) {
            deleteCategoryWithAllTransactions = true;
        }
        let deleteCategoryMode = {
            deleteWithAllTransaction: deleteCategoryWithAllTransactions
        };
        deleteCategory(deleteCategoryMode);
    })

    // Clear Delete Category Modal Form
    $("#deleteCategoryModal").on('hidden.bs.modal', function () {
        setCurrentCategoryID(null);
        $("#deleteCategoryWithAllTransactions").prop("checked", false);
        $("#modalDeleteCategoryErrorText").empty();
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

    // Toggle Categories
    $("#selectCategories").click(function() {
        if ($("#container-categories").is(":hidden")) {
            $("#container-transactions").hide();
            $("#container-categories").show();
            if (getCategoryType() == 0) {
                $("#categoryHead").empty().text('CATEGORIES - Expense');
            } else {
                $("#categoryHead").empty().text('CATEGORIES - Income');
            }
            loadCategoryCards();
        }
    });

    // Toggle Transactions
    $("#selectTransactions").click(function() {
        if ($("#container-transactions").is(":hidden")) {
            $("#container-categories").hide();
            $("#container-transactions").show();
            loadTransactionCards();
        }
    });
  
    // Income Show
    $("#select-income").click(function(e) {
        if ($("#container-categories").is(":visible")) {
            setCategoryType(1);
            $("#categoryHead").empty().text('CATEGORIES - Income');
            loadCategoryCards();
        }
    });
    
    // Expense Show
    $("#select-expense").click(function(e) {
        if ($("#container-categories").is(":visible")) {
            setCategoryType(0);
            $("#categoryHead").empty().text('CATEGORIES - Expense');
            loadCategoryCards();
        }
    });
  
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
