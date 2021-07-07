// VARIABLES =============================================================
var TOKEN_KEY = "jwtToken";
var LAST_USER = "lastUser";
let USER_CURRENCY = "userCurrency";
let TRANSACTION_ID = "transactionId";
let CATEGORY_EXPENSE = "Expense";
let CATEGORY_INCOME = "Income";
let CATEGORY_ID = "categoryID";
var CATEGORY_TYPE = "categoryType";
var CURRENT_MONTH = "currentMonth";
var CURRENT_YEAR = "currentYear";
var MONTHS = "months";
var months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
var colors = ["pink", "orange", "warning", "info", "primary", "indigo", "purple"];

// FUNCTIONS =============================================================
function getJwtToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function setJwtToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
}

function removeJwtToken() {
    localStorage.removeItem(TOKEN_KEY);
}

function getLastUser() {
    return localStorage.getItem(LAST_USER);
}

function setLastUser(email) {
    localStorage.setItem(LAST_USER, email);
}

function removeLastUser() {
    localStorage.removeItem(LAST_USER);
}

function getCategoryType() {
    return localStorage.getItem(CATEGORY_TYPE);
}

function setCategoryType(categoryType) {
    localStorage.setItem(CATEGORY_TYPE, categoryType);
}

function getCurrentMonth() {
    return localStorage.getItem(CURRENT_MONTH);
}

function setCurrentMonth(currentMonth) {
    localStorage.setItem(CURRENT_MONTH, currentMonth);
}

function getCurrentYear() {
    return localStorage.getItem(CURRENT_YEAR);
}

function setCurrentYear(currentYear) {
    localStorage.setItem(CURRENT_YEAR, currentYear);
}

function getMonthName(currentMonth) {
    return months[currentMonth];
}

function getMonthNames() {
    return months;
}

function setMonthNames(months) {
    localStorage.setItem(MONTHS, months);
}

function getColor(colorId) {
    return colors[colorId];
}

function getColorsNumber() {
    return colors.length;
}

function getCurrentCategoryID() {
    return localStorage.getItem(CATEGORY_ID);
}

function setCurrentCategoryID(currentCategoryID) {
    localStorage.setItem(CATEGORY_ID, currentCategoryID);
}

function getCurrentTransactionID() {
    return localStorage.getItem(TRANSACTION_ID);
}

function setCurrentTransactionID(currentTransactionID) {
    localStorage.setItem(TRANSACTION_ID, currentTransactionID);
}

function getUserCurrency() {
    return localStorage.getItem(USER_CURRENCY);
}

function setUserCurrency(userCurrency) {
    localStorage.setItem(USER_CURRENCY, userCurrency);
}
