package com.okdev.ems.controllers;

import com.okdev.ems.dto.CurrencyDTO;
import com.okdev.ems.dto.PageCountDTO;
import com.okdev.ems.dto.UserDTO;
import com.okdev.ems.dto.results.ResultDTO;
import com.okdev.ems.dto.results.SuccessResult;
import com.okdev.ems.services.CurrencyService;
import com.okdev.ems.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administrator Controller", description = "Controller for deleting Users and for adding, updating and deleting Currencies")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final UserService userService;
    private final CurrencyService currencyService;
    private static final Integer PAGE_SIZE = 8;

    @Autowired
    public AdminController(UserService userService, CurrencyService currencyService) {
        this.userService = userService;
        this.currencyService = currencyService;
    }

    @GetMapping("/id")
    @Operation(summary = "Get User Parameters", description = "Allows to get a user parameters")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUser(HttpServletRequest request) {
        Long userId = userService.getUserId(request);
        UserDTO userDTO = userService.findById(userId).toDTO();
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/count")
    @Operation(summary = "Get Count of Users", description = "Allows to get a count of users")
    public ResponseEntity<PageCountDTO> countUsers() {
        PageCountDTO pageCount = PageCountDTO.of(userService.countAllUsers(), currencyService.countAllCurrencies(), PAGE_SIZE);
        return new ResponseEntity<>(pageCount, HttpStatus.OK);
    }

    @GetMapping({"/users/page", "/users/page/{page}"})
    @Operation(summary = "Get All Users by Page", description = "Allows to get all users by page")
    public ResponseEntity<List<UserDTO>> usersList(@PathVariable(value = "page", required = false) Integer page) {
        List<UserDTO> usersList = userService.getAllUsers(
                PageRequest.of(page == null ? 0 : page, PAGE_SIZE, Sort.Direction.ASC, "userId"));
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @GetMapping("/users/search/{pattern}")
    @Operation(summary = "Search Users by Pattern", description = "Allows to search users by pattern")
    public ResponseEntity<List<UserDTO>> findUsersByPattern(@PathVariable("pattern") String pattern) {
        List<UserDTO> usersList = userService.findByPattern(pattern);
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @DeleteMapping("/users")
    @Operation(summary = "Delete Users by ID list", description = "Allows to delete users by ID list")
    public ResponseEntity<ResultDTO> deleteUsers(@RequestBody Map<String, List<Long>> userMap) {
        userService.deleteUsers(userMap.get("usersId"));
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @GetMapping({"/currency/page", "/currency/page/{page}"})
    @Operation(summary = "Get All Currencies by Page", description = "Allows to get all currencies by page")
    public ResponseEntity<List<CurrencyDTO>> currenciesList(@PathVariable(value = "page", required = false) Integer page) {
        List<CurrencyDTO> currenciesList = currencyService.fetchAllCurrencies(
                PageRequest.of(page == null ? 0 : page, PAGE_SIZE, Sort.Direction.ASC, "currencyId"));
        return new ResponseEntity<>(currenciesList, HttpStatus.OK);
    }

    @GetMapping("/currency/{currencyId}")
    @Operation(summary = "Get Currency by ID", description = "Allows to get a currency by it ID")
    public ResponseEntity<CurrencyDTO> currencyById(@PathVariable("currencyId") Long currencyId) {
        CurrencyDTO currency = currencyService.fetchCurrencyById(currencyId);
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }

    @GetMapping("/currency/search/{pattern}")
    @Operation(summary = "Search Currencies by Pattern", description = "Allows to search currencies by pattern")
    public ResponseEntity<List<CurrencyDTO>> findCurrenciesByPattern(@PathVariable("pattern") String pattern) {
        List<CurrencyDTO> currenciesList = currencyService.fetchByPattern(pattern);
        return new ResponseEntity<>(currenciesList, HttpStatus.OK);
    }

    @PostMapping("/currency")
    @Operation(summary = "Add Currency", description = "Allows to add a new currency")
    public ResponseEntity<CurrencyDTO> addCurrency(@RequestBody CurrencyDTO currencyDTO) {
        CurrencyDTO currency = currencyService.addCurrency(currencyDTO);
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }

    @PutMapping("/currency/{currencyId}")
    @Operation(summary = "Update Currency by ID", description = "Allows to update a currency by it ID")
    public ResponseEntity<CurrencyDTO> editCurrency(@PathVariable("currencyId") Long currencyId,
                                                    @RequestBody CurrencyDTO currencyDTO) {
        CurrencyDTO currency = currencyService.editCurrency(currencyId, currencyDTO);
        return new ResponseEntity<>(currency,HttpStatus.OK);
    }

    @DeleteMapping("/currency")
    @Operation(summary = "Delete Currencies by ID list", description = "Allows to delete currencies by ID list")
    public ResponseEntity<ResultDTO> deleteCurrencies(@RequestBody Map<String, List<Long>> userMap) {
        currencyService.deleteCurrencies(userMap.get("currenciesId"));
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }
}
