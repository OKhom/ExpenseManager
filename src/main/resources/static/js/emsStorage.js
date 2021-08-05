// VARIABLES =============================================================
let TOKEN_KEY = "jwtToken";
let LAST_USER = "lastUser";
let ADMIN_VIEW = "adminView";
let CURRENCY_ID = "currencyId";
let USER_CURRENCY = "userCurrency";
let TRANSACTION_ID = "transactionId";
let CATEGORY_ID = "categoryID";
let CATEGORY_TYPE = "categoryType";
let CURRENT_MONTH = "currentMonth";
let CURRENT_YEAR = "currentYear";
let MONTHS = "months";
let months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
let colors = ["pink", "orange", "warning", "info", "primary", "indigo", "purple"];

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

function getCurrencyId() {
    return localStorage.getItem(CURRENCY_ID);
}

function setCurrencyId(currencyId) {
    localStorage.setItem(CURRENCY_ID, currencyId);
}

function getAdminView() {
    return localStorage.getItem(ADMIN_VIEW);
}

function setAdminView(adminView) {
    localStorage.setItem(ADMIN_VIEW, adminView);
}
