# Expense Manager
This application helps user to maintain and keep track of their expenses and incomes.

**USER** can perform the following operations in the application:  
`1.	Edit user profile`  
`2.	Set default user’s currency`  
`3.	Set, Edit and Delete monthly budget limit`  
`4.	Add, Edit and Delete user’s categories and subcategories`  
`5.	Add, Edit and Delete expense and income transactions`  
`6.	View overall monthly expenses`

**ADMIN** can perform the following operations in the application:  
`1.	Delete user profile`  
`2.	Add, Edit and Delete currencies`

# Project Components
**REST API:** project uses the Spring Boot framework with Springboot-Web, Springboot-Data, Springboot-Security. Apart from this, project uses JSON Web Token (JWT) for users authentication.  
**DATABASE:** project uses the MySQL as the relational database and Jdbc to interact with it.  
**USER Interface:** project uses the Bootstrap framework.  
**API DOCS:** project uses the OpenAPI with Swagger-UI.  

```
ExpenseManager
│   pom.xml
│   system.properties
│           
└───src
    └───main
        ├───java
        │   └───com
        │       └───okdev
        │           └───ems
        │               │   ExpenseManagerApplication.java
        │               │   
        │               ├───config
        │               │   │   AppConfig.java
        │               │   │   SecurityConfig.java
        │               │   │   SwaggerConfig.java
        │               │   │   
        │               │   ├───jwt
        │               │   │       JwtAuthenticationSuccessHandler.java
        │               │   │       JwtFilter.java
        │               │   │       JwtProvider.java
        │               │   │       
        │               │   └───utils
        │               │           ExchangeRates.java
        │               │           
        │               ├───controllers
        │               │       AdminController.java
        │               │       BudgetController.java
        │               │       CategoryController.java
        │               │       TransactionController.java
        │               │       UserController.java
        │               │       
        │               ├───dto
        │               │   │   AmountDTO.java
        │               │   │   BudgetDTO.java
        │               │   │   CategoryDeleteDTO.java
        │               │   │   CategoryDTO.java
        │               │   │   CurrencyDTO.java
        │               │   │   PageCountDTO.java
        │               │   │   SubcategoryDTO.java
        │               │   │   SubcategoryDTOext.java
        │               │   │   TokenDTO.java
        │               │   │   TokenValidateDTO.java
        │               │   │   TransactionDTO.java
        │               │   │   UserDTO.java
        │               │   │   
        │               │   └───results
        │               │           BadResult.java
        │               │           ResultDTO.java
        │               │           SuccessResult.java
        │               │           
        │               ├───exceptions
        │               │       EmsAuthException.java
        │               │       EmsBadRequestException.java
        │               │       EmsResourceNotFoundException.java
        │               │       
        │               ├───models
        │               │   │   Budgets.java
        │               │   │   Categories.java
        │               │   │   Currencies.java
        │               │   │   Subcategories.java
        │               │   │   Transactions.java
        │               │   │   Users.java
        │               │   │   
        │               │   ├───embeddedID
        │               │   │       BudgetId.java
        │               │   │       
        │               │   └───enums
        │               │           CategoryType.java
        │               │           UserRole.java
        │               │           
        │               ├───repositories
        │               │       BudgetRepository.java
        │               │       CategoryRepository.java
        │               │       CurrencyRepository.java
        │               │       SubcategoryRepository.java
        │               │       TransactionRepository.java
        │               │       UserRepository.java
        │               │       
        │               └───services
        │                       BudgetService.java
        │                       BudgetServiceImpl.java
        │                       CategoryService.java
        │                       CategoryServiceImpl.java
        │                       CurrencyService.java
        │                       CurrencyServiceImpl.java
        │                       TransactionService.java
        │                       TransactionServiceImpl.java
        │                       UserDetailsServiceImpl.java
        │                       UserService.java
        │                       UserServiceImpl.java
        │                       
        └───resources
            │   application.properties
            │   
            └───static
                │   admin.html
                │   index.html
                │   login.html
                │   registration.html
                │   
                ├───apidocs
                ├───css
                │       index.css
                │       index.min.css
                │       login.css
                │       main.css
                │       util.css
                │       
                ├───fonts 
                ├───images
                ├───js
                │       admin.js
                │       emsMain.js
                │       emsStorage.js
                │       index.js
                │       index.min.js
                │       login.js
                │       registration.js
                │       
                ├───locales
                └───vendor
```

### Controllers  
*	Responsible to respond for client HTTP requests  
*	Controller delegates requests processing to service layer, get back response from it and hand it to server  
### Services  
*	Contains business logic  
*	Interact with other dependent services  
### Repositories  
*	Interacts with Datasource (Ex Database, File System, etc) and knows about it  
*	Service layer propagate request to this layer  
