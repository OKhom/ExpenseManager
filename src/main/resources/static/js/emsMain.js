// VARIABLES =============================================================

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
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status === 401) {
                alert("Authentication Error: " + jqXHR.responseJSON.message)
            } else {
                alert("an unexpected error occurred: " + errorThrown);
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
            setLastUser(data.email)
            window.location.assign("/login.html");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status === 401) {
                loginError(jqXHR);
            } else {
                alert("an unexpected error occurred: " + errorThrown);
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
            $('#amountIncome').empty()
                .append(parseInt(data.amountIncome.toFixed(0)).toLocaleString() + ' ' + data.currencySign);
            $('#amountExpense').empty()
                .append(parseInt(data.amountExpense.toFixed(0)).toLocaleString() + ' ' + data.currencySign);
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
            returnAllCurrencies(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorToConsole(jqXHR);
        }
    });
}

function loadTransactionById(returnTransaction) {
    $.ajax({
        url: "api/transaction/" + getCurrentTransactionID() + "/category/" + getCurrentCategoryID(),
        type: "GET",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
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
            returnCategory(data);
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
            $("#addSubcategoryModal").modal('hide');
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
}

function createAuthorizationTokenHeader() {
    let token = getJwtToken();
    if (token) {
        return {"Authorization": "Bearer " + token};
    } else {
        return {};
    }
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

function showUserInformation(returnUserInfo) {
    $.ajax({
        url: "/api/users/id",
        type: "GET",
        dataType: "json",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            returnUserInfo(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorShowForm(jqXHR, textStatus, errorThrown);
        }
    });
}

function showAdminInformation(returnAdminInfo) {
    $.ajax({
        url: "/api/admin/id",
        type: "GET",
        dataType: "json",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            returnAdminInfo(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorShowForm(jqXHR, textStatus, errorThrown);
        }
    });
}

function loadPages(pageNavbar) {
    $.ajax({
        url: "/api/admin/count",
        type: "GET",
        dataType: "json",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            $("#quantityUsers").empty().text(data.count);
            $("#quantityCurrencies").empty().text(data.countCurrencies);
            $(pageNavbar).empty();
            let count = pageNavbar === "#pages" ? data.count : data.countCurrencies;
            let pageCount = (count / data.pageSize) + (count % data.pageSize > 0 ? 1 : 0);
            for (let i = 1; i <= pageCount; i++) {
                $(pageNavbar).append(
                    $('<li>').attr('class', 'page-item').append(
                        $('<a>').attr('class', 'page-link').attr('id', i - 1)
                            .append(i))
                );
            }
            if (pageNavbar === "#pages") {
                $("#pageUsersNavbar").show();
            } else if (pageNavbar === "#pagesCurrency") {
                $("#pageCurrencyNavbar").show();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorShowForm(jqXHR, textStatus, errorThrown);
        }
    });
}

function loadData(page) {
    $.ajax({
        url: "/api/admin/users/page/" + page,
        type: "GET",
        dataType: "json",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            usersDataBuilder(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorShowForm(jqXHR, textStatus, errorThrown);
        }
    });
}

function loadUsersDataByPattern(searchPattern) {
    $.ajax({
        url: "/api/admin/users/search/" + searchPattern,
        type: "GET",
        dataType: "json",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            usersDataBuilder(data);
            $("#pageUsersNavbar").hide();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorShowForm(jqXHR, textStatus, errorThrown);
        }
    });
}

function usersDataBuilder(data) {
    $("#dataUsers > tbody").empty();
    for (let i = 0; i < data.length; i++) {
        $('#dataUsers > tbody:last-child').append(
            $('<tr>')
                .append(
                    $('<td>').append(
                        $('<input>').attr('type', 'checkbox').attr('value', data[i].userId)
                    )
                )
                .append($('<td>').append(data[i].email))
                .append($('<td>').append(data[i].firstName))
                .append($('<td>').append(data[i].lastName))
                .append($('<td>').append(data[i].currencyShortName))
        );
    }
}

function loadCurrencyData(page) {
    $.ajax({
        url: "/api/admin/currency/page/" + page,
        type: "GET",
        dataType: "json",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            currenciesDataBuilder(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorShowForm(jqXHR, textStatus, errorThrown);
        }
    });
}

function loadCurrenciesDataByPattern(searchPattern) {
    $.ajax({
        url: "/api/admin/currency/search/" + searchPattern,
        type: "GET",
        dataType: "json",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            currenciesDataBuilder(data);
            $("#pageCurrencyNavbar").hide();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorShowForm(jqXHR, textStatus, errorThrown);
        }
    });
}

function currenciesDataBuilder(data) {
    $("#dataCurrencies > tbody").empty();
    for (let i = 0; i < data.length; i++) {
        $('#dataCurrencies > tbody:last-child').append(
            $('<tr>')
                .append(
                    $('<td>').append(
                        $('<input>').attr('type', 'checkbox').attr('value', data[i].currencyId)
                    )
                )
                .append($('<td>').append(data[i].name))
                .append($('<td>').append(data[i].shortName))
                .append($('<td>').append(data[i].sign))
        );
    }
}

function loadCurrencyById(currencyId, returnCurrency) {
    $.ajax({
        url: "/api/admin/currency/" + currencyId,
        type: "GET",
        dataType: "json",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            returnCurrency(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorShowForm(jqXHR, textStatus, errorThrown);
        }
    });
}

function usersCount() {
    $.ajax({
        url: "/api/admin/count",
        type: "GET",
        dataType: "json",
        headers: createAuthorizationTokenHeader(),
        success: function (data, textStatus, jqXHR) {
            $("#quantityUsers").empty().text(data.count);
            $("#quantityCurrencies").empty().text(data.countCurrencies);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorShowForm(jqXHR, textStatus, errorThrown);
        }
    });
}

function deleteUsers(usersId) {
    $.ajax({
        url: "/api/admin/users",
        type: "DELETE",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(usersId),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            loadData(0);
            loadPages("#pages");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorShowForm(jqXHR, textStatus, errorThrown)
        }
    });
}

function deleteCurrencies(usersId) {
    $.ajax({
        url: "/api/admin/currency",
        type: "DELETE",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(usersId),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            loadCurrencyData(0);
            loadPages("#pagesCurrency");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorShowForm(jqXHR, textStatus, errorThrown)
        }
    });
}

function addCurrency(newCurrency) {
    $.ajax({
        url: "/api/admin/currency",
        type: "POST",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(newCurrency),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            $("#addCurrencyModal").modal('hide');
            loadCurrencyData(0);
            loadPages("#pagesCurrency");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalAddCurrencyErrorText");
        }
    });
}

function editCurrency(currencyId, editedCurrency) {
    $.ajax({
        url: "/api/admin/currency/" + currencyId,
        type: "PUT",
        headers: createAuthorizationTokenHeader(),
        data: JSON.stringify(editedCurrency),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            $("#editCurrencyModal").modal('hide');
            loadCurrencyData(0);
            loadPages("#pagesCurrency");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            modalInputError(jqXHR, "#modalEditCurrencyErrorText");
        }
    });
}

function errorShowForm(jqXHR, textStatus, errorThrown) {
    if (jqXHR.status === 401) {
        console.log("HTTP Status 401: " + jqXHR.responseJSON.message);
    } else if (jqXHR.status === 403) {
        console.log("Access to Endpoint is Forbidden");
        console.log("HTTP Status 403: " + textStatus);
        window.location.replace("/login.html");
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
        console.log("an unexpected error occurred: " + jqXHR.status + " " + textStatus + " " + errorThrown);
    }
}

$("#logoutButton").click(doLogout);

