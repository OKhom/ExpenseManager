// VARIABLES =============================================================
var $notLoggedIn = $("#notLoggedIn");
var $loggedIn = $("#loggedIn").hide();
var $response = $("#response");
var $login = $("#login");
var $userInfo = $("#userInfo");
// var $userInfo = $("#userInfo").hide();

// FUNCTIONS =============================================================
function doLogin(loginData) {
    $.ajax({
        url: "/api/users/auth",
        type: "POST",
        data: JSON.stringify(loginData),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            setJwtToken(data.token);
            setLastUser(loginData.email)
            console.log("Token is " + data.token);
            console.log(getJwtToken());
            // setTimeout(function () {
            //     window.location.assign("/");
            // }, 2000);

            // window.location = url;
            // $login.hide();
            // $notLoggedIn.hide();
            // showTokenInformation()
            // showUserInformation();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status === 401) {
                alert("Authentication Error: " + jqXHR.responseJSON.message)
                // loginError(jqXHR);
            } else {
                alert("an unexpected error occurred: " + errorThrown);
                // throw new Error("an unexpected error occurred: " + errorThrown);
            }
        }
    });
}

function doRegister(registerData) {
    $.ajax({
        url: "/api/users/register",
        type: "POST",
        data: JSON.stringify(registerData),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            console.log("Registered User is " + data);
            console.log("Token is " + getJwtToken());
            setLastUser(data.email)
            window.location.assign("/login.html");

            // window.location = url;
            // $login.hide();
            // $notLoggedIn.hide();
            // showTokenInformation()
            // showUserInformation();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status === 401) {
                loginError(jqXHR);
            } else {
                alert("an unexpected error occurred: " + errorThrown);
                // throw new Error("an unexpected error occurred: " + errorThrown);
            }
        }
    });
}

function loadTotalAmount() {
    $.ajax({
        url: "api/category/amount/" + getCurrentYear() + "/" + (parseInt(getCurrentMonth()) + 1),
        type: "GET",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            console.log(data);
            // returnTotalAmounts(data);
            $('#amountIncome').empty().append(parseInt(data.amountIncome.toFixed(0)).toLocaleString() + ' ' + data.currencySign);
            $('#amountExpense').empty().append(parseInt(data.amountExpense.toFixed(0)).toLocaleString() + ' ' + data.currencySign);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorToConsole(jqXHR);
        }
    });
}

function loadAllCurrencies(returnAllCurrencies) {
    $.ajax({
        url: "/api/category/currency",
        type: "GET",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            console.log(data);
            returnAllCurrencies(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorToConsole(jqXHR);
        }
    });
}

// function returnFunctionData(data) {
//     return data.name;
// }

function loadTransactionById(returnTransaction) {
    $.ajax({
        url: "api/transaction/" + getCurrentTransactionID() + "/category/" + getCurrentCategoryID(),
        type: "GET",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            console.log(data);
            returnTransaction(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorToConsole(jqXHR);
        }
    });
}

function loadCategoryById(returnCategory) {
    $.ajax({
        url: "api/category/" + getCurrentCategoryID(),
        type: "GET",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            console.log(data);
            returnCategory(data);
            // $('#addTransactionHeader').empty().append(data.name);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorToConsole(jqXHR);
        }
    });
}

function loadSubcategories(returnSubcategory) {
    $.ajax({
        url: "api/category/" + getCurrentCategoryID() + "/subcategory",
        type: "GET",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            returnSubcategory(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorToConsole(jqXHR);
        }
    });
}

function loadTransactionCards() {
    $.ajax({
        url: "/api/transaction/" + getCurrentYear() + "/" + (parseInt(getCurrentMonth()) + 1),
        type: "GET",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            console.log(data);
            $("#transactionCards").empty();
            for (let i = 0; i < data.length; i++) {
                let colorId = getColor(data[i].categoryId % getColorsNumber());
                let transactionDate = new Date(data[i].date);
                let amountColor;
                let amountSign;
                if (data[i].type == "EXPENSE") {
                    amountColor = 'danger';
                    amountSign = '-';
                } else {
                    amountColor = 'success';
                    amountSign = '+';
                }
                $("#transactionCards").append($('<div>').attr('class', 'card mb-4 py-2 border-bottom-' + colorId)
                    .append($('<div>').attr('class', 'card-body pr-1 py-0')
                        .append($('<div>').attr('class', 'row no-gutters align-items-top')
                            .append($('<div>').attr('class', 'col-auto mt-3 mr-3')
                                .append($('<i>').attr('class', 'fas fa-clipboard-list fa-2x text-' + colorId)
                                )
                            )
                            .append($('<div>').attr('class', 'col mr-3')
                                .append($('<div>').attr('class', 'row no-gutters align-items-end')
                                    .append($('<div>').attr('class', 'text-lg mb-0 mr-3 font-weight-bold text-gray-800 text-uppercase')
                                        .append(transactionDate.toLocaleDateString("uk", {month: 'long', day: '2-digit'})
                                            + ' ' + transactionDate.getFullYear())
                                    )
                                    .append($('<div>').attr('class', 'text mb-0 mr-3 align-bottom font-weight-bold text-gray-800 text-uppercase')
                                        .append(transactionDate.toLocaleDateString("uk", {weekday: 'long'}))
                                    )
                                )
                                .append($('<div>').attr('class', 'row no-gutters align-items-end justify-content-between')
                                    .append($('<div>').attr('class', 'row no-gutters align-items-end')
                                        .append($('<div>').attr('class', 'text mb-0 mr-2 font-weight-bold text-uppercase text-' + colorId)
                                            .append(data[i].categoryName)
                                        )
                                        .append($('<div>').attr('class', 'text mb-0 mr-2 font-weight-bold text-uppercase text-gray-800')
                                            .append((data[i].subcategory != null) ? ('(' + data[i].subcategory + ')') : "")
                                        )
                                    )
                                    .append($('<div>').attr('class', 'text-lg mb-0 mr-0 font-weight-bold text-uppercase text-' + amountColor)
                                        .append(amountSign + parseFloat(data[i].amount.toFixed(2)).toLocaleString() + ' ' + data[i].currencySign)
                                    )
                                )
                                .append($('<div>').attr('class', 'row no-gutters align-items-end')
                                    .append($('<div>').attr('class', 'text-xs mb-0 mr-3 font-weight-bold font-italic text-gray-800')
                                        .append(data[i].note)
                                    )
                                )
                            )
                            .append($('<div>').attr('class', 'dropdown no-arrow')
                                .append($('<a>').attr('class', 'text dropdown-toggle').attr('id', 'transactionDropdown').attr('href', '#')
                                    .attr('role', 'button').attr('data-toggle', 'dropdown').attr('aria-haspopup', 'true').attr('aria-expanded', 'false')
                                    .append($('<i>').attr('class', 'fas fa-ellipsis-v fa-sm fa-fw text-gray-400'))
                                )
                                .append($('<div>').attr('class', 'dropdown-menu').attr('aria-labelledby', 'transactionDropdown')
                                    .append($('<a>').attr('class', 'dropdown-item').attr('href', '#').attr('id', 'selectEditTransaction')
                                        .attr('data-id', data[i].transactionId).attr('data-cat', data[i].categoryId)
                                        .append('Edit transaction')
                                    )
                                    .append($('<a>').attr('class', 'dropdown-item').attr('href', '#').attr('id', 'selectDeleteTransaction')
                                        .attr('data-id', data[i].transactionId).attr('data-cat', data[i].categoryId)
                                        .append('Delete transaction')
                                    )
                                )
                            )
                        )
                    )
                );
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorToConsole(jqXHR);
        }
    });
}

function loadCategoryCards() {
    $.ajax({
        url: "/api/category/" + getCurrentYear() + "/" + (parseInt(getCurrentMonth()) + 1) + "/type/" + getCategoryType(),
        type: "GET",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            console.log(data);
            $("#categoryCards").empty();
            for (let i = 0; i < data.length; i++) {
                let colorId = getColor(data[i].categoryId % getColorsNumber());
                let currentAmount = data[i].totalExpense;
                let currentBudget = data[i].budget;
                let restBudget = currentBudget - currentAmount;
                let percent;
                if (currentBudget == 0) {
                    percent = '100';
                    restBudget = 0;
                } else if (restBudget < 0) {
                    percent = '100';
                } else {
                    percent = (currentAmount / currentBudget * 100).toFixed(0);
                }
                $("#categoryCards").append($('<div>').attr('class', 'col-xl-3 col-md-6 mb-4')
                    .append($('<div>').attr('class', 'card border-left-' + colorId + ' shadow h-100 py-2')
                        .append($('<div>').attr('class', 'card-body py-2')
                            .append($('<div>').attr('class', 'row no-gutters align-items-center')
                                .append($('<a>').attr('class', 'text mr-2').attr('href', '#').attr('role', 'button')
                                    .attr('data-id', data[i].categoryId).attr('id', 'selectAddTransaction')
                                    .append($('<i>').attr('class', 'fas fa-clipboard-list fa-2x text-' + colorId)
                                    )
                                )
                                .append($('<div>').attr('class', 'col')
                                    .append($('<div>').attr('class', 'card-title mb-0 d-flex flex-row align-items-start justify-content-between')
                                        .append($('<div>').attr('class', 'text-xs font-weight-bold text-' + colorId + ' text-uppercase mb-0')
                                            .attr('href', '#').attr('role', 'button').attr('data-id', data[i].categoryId)
                                            .attr('id', 'selectAddTransaction')
                                            .append(data[i].name)
                                        )
                                        .append($('<div>').attr('class', 'dropdown no-arrow')
                                            .append($('<a>').attr('class', 'text dropdown-toggle').attr('href', '#').attr('role', 'button')
                                                .attr('id', 'categoryDropdown').attr('data-toggle', 'dropdown').attr('aria-haspopup', 'true')
                                                .attr('aria-expanded', 'false')
                                                .append($('<i>').attr('class', 'fas fa-ellipsis-v fa-sm fa-fw text-gray-400')
                                                )
                                            )
                                            .append($('<div>').attr('class', 'dropdown-menu').attr('aria-labelledby', 'categoryDropdown')
                                                .append($('<a>').attr('class', 'dropdown-item').attr('href', '#')
                                                    .attr('data-id', data[i].categoryId).attr('id', 'selectAddBudget')
                                                    .append('Add budget')
                                                )
                                                .append($('<a>').attr('class', 'dropdown-item').attr('href', '#')
                                                    .attr('data-id', data[i].categoryId).attr('id', 'selectEditBudget')
                                                    .append('Edit budget')
                                                )
                                                .append($('<a>').attr('class', 'dropdown-item').attr('href', '#')
                                                    .attr('data-id', data[i].categoryId).attr('id', 'selectDeleteBudget')
                                                    .append('Delete budget')
                                                )
                                                .append($('<div>').attr('class', 'dropdown-divider'))
                                                .append($('<a>').attr('class', 'dropdown-item').attr('href', '#')
                                                    .attr('data-id', data[i].categoryId).attr('id', 'selectAddSubcategory')
                                                    .append('Add subcategory')
                                                )
                                                .append($('<a>').attr('class', 'dropdown-item').attr('href', '#')
                                                    .attr('data-id', data[i].categoryId).attr('id', 'selectEditSubcategory')
                                                    .append('Edit subcategory')
                                                )
                                                .append($('<a>').attr('class', 'dropdown-item').attr('href', '#')
                                                    .attr('data-id', data[i].categoryId).attr('id', 'selectDeleteSubcategory')
                                                    .append('Delete subcategory')
                                                )
                                                .append($('<div>').attr('class', 'dropdown-divider'))
                                                .append($('<a>').attr('class', 'dropdown-item').attr('href', '#')
                                                    .attr('data-id', data[i].categoryId).attr('id', 'selectEditCategory')
                                                    .append('Edit category')
                                                )
                                                .append($('<a>').attr('class', 'dropdown-item').attr('href', '#')
                                                    .attr('data-id', data[i].categoryId).attr('id', 'selectDeleteCategory')
                                                    .append('Delete category')
                                                )
                                            )
                                        )
                                    )
                                    .append($('<div>').attr('class', 'row mb-1 no-gutters align-items-center')
                                        .append($('<div>').attr('class', 'col')
                                            .append($('<div>').attr('class', 'progress progress-sm mr-2').attr('href', '#')
                                                .attr('role', 'button').attr('data-id', data[i].categoryId).attr('id', 'selectAddTransaction')
                                                .append($('<div>').attr('class', 'progress-bar bg-' + colorId).attr('role', 'progressbar')
                                                    .attr('style', 'width: ' + percent + '%')
                                                )
                                            )
                                        )
                                    )
                                    .append($('<div>').attr('class', 'row no-gutters align-items-center d-flex flex-row justify-content-between')
                                        .attr('href', '#').attr('role', 'button').attr('data-id', data[i].categoryId).attr('id', 'selectAddTransaction')
                                        .append($('<div>').attr('class', 'text-xs mb-0 mr-3 font-weight-bold text-gray-800')
                                            .append(parseInt(currentAmount.toFixed(0)).toLocaleString() + ' ' + data[i].currencySign)
                                        )
                                        .append($('<div>').attr('class', 'text-xs mb-0 font-weight-bold text-' + (restBudget < 0 ? 'danger' : 'success'))
                                            .append(parseInt(restBudget.toFixed(0)).toLocaleString() + ' ' + data[i].currencySign)
                                        )
                                    )
                                )
                            )
                        )
                    )
                );
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorToConsole(jqXHR);
        }
    });
}

function addCategory(newCategory, categoryType) {
    $.ajax({
        url: "/api/category/" + categoryType,
        type: "POST",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(newCategory),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            console.log("New category is: " + data.categoryId + ", " + data.name + ", " + data.currencyId + ", " + data.type);
            $("#addCategoryModal").modal('hide');
            loadCategoryCards();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalErrorText");
        }
    });
}

function updateCategory(updateCategory) {
    $.ajax({
        url: "/api/category/" + getCurrentCategoryID(),
        type: "PUT",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(updateCategory),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            console.log("Updated category is: " + data.categoryId + ", " + data.name + ", " + data.currencyId + ", " + data.type);
            $("#editCategoryModal").modal('hide');
            loadCategoryCards();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalEditCategoryErrorText");
        }
    });
}

function deleteCategory(deleteCategoryMode) {
    $.ajax({
        url: "/api/category/" + getCurrentCategoryID(),
        type: "DELETE",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(deleteCategoryMode),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            $("#deleteCategoryModal").modal('hide');
            loadCategoryCards();
            loadTotalAmount();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalDeleteCategoryErrorText");
        }
    });
}

function addSubcategory(newSubcategory, currentCategoryID) {
    $.ajax({
        url: "/api/category/" + currentCategoryID + "/subcategory",
        type: "POST",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(newSubcategory),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            console.log("New subcategory is: " + data.subcategoryId + ", " + data.subname + ", " + data.categoryId);
            $("#addSubcategoryModal").modal('hide');
            // loadCategoryCards();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalSubcategoryErrorText");
        }
    });
}

function updateSubcategory(subcategoryID, newSubcategory) {
    $.ajax({
        url: "/api/category/" + getCurrentCategoryID() + "/subcategory/" + subcategoryID,
        type: "PUT",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(newSubcategory),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            $("#updateSubcategoryModal").modal('hide');
            // loadCategoryCards();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalUpdateSubcategoryErrorText");
        }
    });
}

function deleteSubcategory(subcategoryID) {
    $.ajax({
        url: "/api/category/" + getCurrentCategoryID() + "/subcategory/" + subcategoryID,
        type: "DELETE",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            $("#deleteSubcategoryModal").modal('hide');
            // loadCategoryCards();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalDeleteSubcategoryErrorText");
        }
    });
}

function addBudget(newBudget, currentCategoryID) {
    $.ajax({
        url: "/api/category/" + currentCategoryID + "/budget/" + getCurrentYear() + "/" + (parseInt(getCurrentMonth()) + 1),
        type: "POST",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(newBudget),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            $("#addBudgetModal").modal('hide');
            loadCategoryCards();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalBudgetErrorText");
        }
    });
}

function editBudget(newBudget, currentCategoryID) {
    $.ajax({
        url: "/api/category/" + currentCategoryID + "/budget/" + getCurrentYear() + "/" + (parseInt(getCurrentMonth()) + 1),
        type: "PUT",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(newBudget),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            $("#editBudgetModal").modal('hide');
            loadCategoryCards();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalEditBudgetErrorText");
        }
    });
}

function deleteBudget() {
    $.ajax({
        url: "/api/category/" + getCurrentCategoryID() + "/budget/" + getCurrentYear() + "/" + (parseInt(getCurrentMonth()) + 1),
        type: "DELETE",
        headers: createAuthorizationTokenHeader(),
        // data: JSON.stringify(newBudget),
        // contentType: "application/json; charset=utf-8",
        // dataType: "json",
        success: function (data, textStatus, jqXHR) {
            $("#deleteBudgetModal").modal('hide');
            loadCategoryCards();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalDeleteBudgetErrorText");
        }
    });
}

function addTransaction(newTransaction, currentCategoryID, currentSubcategory) {
    let url = "/api/transaction/category/" + currentCategoryID;
    if (currentSubcategory != null) {
        url = url + "/subcategory/" + currentSubcategory;
    }
    $.ajax({
        url: url,
        type: "POST",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(newTransaction),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            console.log("Added Transaction is: " + data.categoryId + ", " + data.amount + ", " + data.note + ", " + data.date);
            $("#addTransactionModal").modal('hide');
            loadCategoryCards();
            loadTotalAmount();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalTransactionErrorText");
        }
    });
}

function editTransaction(newTransaction, currentSubcategory) {
    let url = "/api/transaction/" + getCurrentTransactionID() + "/category/" + getCurrentCategoryID();
    if (currentSubcategory != null) {
        url = url + "/subcategory/" + currentSubcategory;
    }
    $.ajax({
        url: url,
        type: "PUT",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(newTransaction),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            console.log("Edited Transaction is: " + data.categoryId + ", " + data.amount + ", " + data.note + ", " + data.date);
            $("#editTransactionModal").modal('hide');
            loadTransactionCards();
            loadTotalAmount();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalEditTransactionErrorText");
        }
    });
}

function deleteTransaction() {
    $.ajax({
        url: "/api/transaction/" + getCurrentTransactionID() + "/category/" + getCurrentCategoryID(),
        type: "DELETE",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            $("#deleteTransactionModal").modal('hide');
            loadTransactionCards();
            loadTotalAmount();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalDeleteTransactionErrorText");
        }
    });
}

function modalInputError(jqXHR, selectModalForm) {
    if (jqXHR.status === 401) {
        console.log("Error Code is: " + jqXHR.status + " " + jqXHR.responseJSON.message);
        $(selectModalForm).text("User Authorized invalid. Relogin and try again...");
        // loginError(jqXHR);
    } else if (jqXHR.status === 400) {
        console.log("Error Code is: " + jqXHR.status + " " + jqXHR.responseJSON.message);
        $(selectModalForm).text(jqXHR.responseJSON.message);
    } else if (jqXHR.status === 404) {
        console.log("Error Code is: " + jqXHR.status + " " + jqXHR.responseJSON.message);
        $(selectModalForm).text("Entered Data is invalid. Try again...");
    } else {
        console.log("Error Code is: " + jqXHR.status + " An unexpected error occurred: " + jqXHR.responseJSON.message);
        $(selectModalForm).text("Error Code is: " + jqXHR.status + " " + jqXHR.responseJSON.message);
    }
}

function errorToConsole(jqXHR) {
    if (jqXHR.status === 400) {
        console.log("HTTP Status 400: " + jqXHR.responseJSON.message);
    } else if (jqXHR.status === 401) {
        console.log("HTTP Status 401: " + jqXHR.responseJSON.message);
    } else if (jqXHR.status === 404) {
        console.log("HTTP Status 404: " + jqXHR.responseJSON.message);
    } else if (jqXHR.status === 500) {
        console.log("HTTP Status 500: " + jqXHR.readyState + " StatusText " + jqXHR.statusText);
    } else {
        console.log("an unexpected error occurred: " + jqXHR.status);
        // throw new Error("an unexpected error occurred: " + errorThrown);
    }
}

function loginError(jqXHR) {
    $('#loginErrorModal')
        .modal("show")
        .find(".modal-body")
        .empty()
        .html("<p>" + jqXHR.responseJSON.message + "</p>");
}

function doLogout() {
    removeJwtToken();
    window.location = "/logout";
    // $login.show();
    // $userInfo
    //     .hide()
    //     .find("#userInfoBody").empty();
    // $loggedIn
    //     .hide()
    //     .attr("title", "")
    //     .empty();
    // $notLoggedIn.show();
}

function createAuthorizationTokenHeader() {
    var token = getJwtToken();
    if (token) {
        return {"Authorization": "Bearer " + token};
    } else {
        return {};
    }
}

function showUserInformation(returnUserInfo) {
    $.ajax({
        url: "/api/users/id",
        type: "GET",
        dataType: "json",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            console.log(data);
            returnUserInfo(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status === 401) {
                console.log("HTTP Status 401: " + jqXHR.responseJSON.message);
            } else if (jqXHR.status === 404) {
                console.log("HTTP Status 404: " + jqXHR.responseJSON.message);
            } else if (jqXHR.status === 302) {
                console.log("HTTP Status 302: " + jqXHR.responseJSON.message + jqXHR.getAllResponseHeaders());
            } else if (jqXHR.status === 500) {
                console.log("HTTP Status 500 - Redirect page to LOGIN PAGE by STATE " + jqXHR.readyState + " StatusText " + jqXHR.statusText);
                window.location.replace("/login.html");
            } else if (jqXHR.status === 200 && textStatus === "parsererror") {
                console.log("Redirect page to ... by STATE " + jqXHR.readyState + " StatusText " + jqXHR.statusText);
                window.location.replace("/login.html");
            } else {
                console.log("an unexpected error occurred: " + jqXHR.status + " " + textStatus + " " + errorThrown + " - END of Error");
                // throw new Error("an unexpected error occurred: " + errorThrown);
            }
        }
    });
}

function updateUser(updatedUser) {
    $.ajax({
        url: "/api/users/id",
        type: "PUT",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(updatedUser),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            console.log("Updated User is: " + data.userId + ", " + data.firstName + ", " + data.lastName + ", " + data.currencyId);
            $("#editUserModal").modal('hide');
            showUserInformation(function (data) {
                $("#navUserName").empty().text(data.firstName + " " + data.lastName);
                $("#firstNameProfile").empty().text(data.firstName);
                $("#lastNameProfile").empty().text(data.lastName);
                $("#currencyProfile").empty().text("(" + data.currencyShortName + ") " + data.currencyName);
                setUserCurrency(data.currencyId);
                loadTotalAmount();
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalEditUserErrorText");
        }
    });
}

function showTokenInformation() {
    $loggedIn
        .text("Token: " + getJwtToken())
        .attr("title", "Token: " + getJwtToken())
        .show();
}

function showResponse(statusCode, message) {
    $response
        .empty()
        .text(
            "status code: "
            + statusCode + "\n-------------------------\n"
            + (typeof message === "object" ? JSON.stringify(message) : message)
        );
}

// REGISTER EVENT LISTENERS =============================================================
$("#loginForm").submit(function (event) {
    event.preventDefault();

    var $form = $(this);
    var formData = {
        username: $form.find('input[name="username"]').val(),
        password: $form.find('input[name="password"]').val()
    };

    doLogin(formData);
});

$("#logoutButton").click(doLogout);

$("#exampleServiceBtn").click(function () {
    $.ajax({
        url: "/api/person",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            showResponse(jqXHR.status, JSON.stringify(data));
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showResponse(jqXHR.status, jqXHR.responseJSON.message)
        }
    });
});

$("#adminServiceBtn").click(function () {
    $.ajax({
        url: "/api/hiddenmessage",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            showResponse(jqXHR.status, data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showResponse(jqXHR.status, jqXHR.responseJSON.message)
        }
    });
});

$loggedIn.click(function () {
    $loggedIn
        .toggleClass("text-hidden")
        .toggleClass("text-shown");
});

// INITIAL CALLS =============================================================
if (getJwtToken()) {
    // $login.hide();
    // $notLoggedIn.hide();
    // showTokenInformation();
    // showUserInformation();
}
