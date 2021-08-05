package com.okdev.ems.controllers;

import com.okdev.ems.config.jwt.JwtProvider;
import com.okdev.ems.dto.CurrencyDTO;
import com.okdev.ems.dto.PageCountDTO;
import com.okdev.ems.dto.UserDTO;
import com.okdev.ems.dto.results.ResultDTO;
import com.okdev.ems.dto.results.SuccessResult;
import com.okdev.ems.services.CurrencyService;
import com.okdev.ems.services.UserService;
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
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    CurrencyService currencyService;

    @Autowired
    JwtProvider jwtProvider;

    private static final Integer PAGE_SIZE = 8;

    @GetMapping("/id")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUser(HttpServletRequest request) {
        Long userId = userService.getUserId(request);
        UserDTO userDTO = userService.findById(userId).toDTO();
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<PageCountDTO> countUsers() {
        PageCountDTO pageCount = PageCountDTO.of(userService.countAllUsers(), currencyService.countAllCurrencies(), PAGE_SIZE);
        return new ResponseEntity<>(pageCount, HttpStatus.OK);
    }

    @GetMapping({"/users/page", "/users/page/{page}"})
    public ResponseEntity<List<UserDTO>> usersList(@PathVariable(value = "page", required = false) Integer page) {
        List<UserDTO> usersList = userService.getAllUsers(
                PageRequest.of(page == null ? 0 : page, PAGE_SIZE, Sort.Direction.ASC, "userId"));
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @GetMapping("/users/search/{pattern}")
    public ResponseEntity<List<UserDTO>> findUsersByPattern(@PathVariable("pattern") String pattern) {
        List<UserDTO> usersList = userService.findByPattern(pattern);
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @DeleteMapping("/users")
    public ResponseEntity<ResultDTO> deleteUsers(@RequestBody Map<String, List<Long>> userMap) {
        userService.deleteUsers(userMap.get("usersId"));
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @GetMapping({"/currency/page", "/currency/page/{page}"})
    public ResponseEntity<List<CurrencyDTO>> currenciesList(@PathVariable(value = "page", required = false) Integer page) {
        List<CurrencyDTO> currenciesList = currencyService.fetchAllCurrencies(
                PageRequest.of(page == null ? 0 : page, PAGE_SIZE, Sort.Direction.ASC, "currencyId"));
        return new ResponseEntity<>(currenciesList, HttpStatus.OK);
    }

    @GetMapping("/currency/{currencyId}")
    public ResponseEntity<CurrencyDTO> currencyById(@PathVariable("currencyId") Long currencyId) {
        CurrencyDTO currency = currencyService.fetchCurrencyById(currencyId);
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }

    @GetMapping("/currency/search/{pattern}")
    public ResponseEntity<List<CurrencyDTO>> findCurrenciesByPattern(@PathVariable("pattern") String pattern) {
        List<CurrencyDTO> currenciesList = currencyService.fetchByPattern(pattern);
        return new ResponseEntity<>(currenciesList, HttpStatus.OK);
    }

    @PostMapping("/currency")
    public ResponseEntity<CurrencyDTO> addCurrency(@RequestBody CurrencyDTO currencyDTO) {
        CurrencyDTO currency = currencyService.addCurrency(currencyDTO);
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }

    @PutMapping("/currency/{currencyId}")
    public ResponseEntity<CurrencyDTO> editCurrency(@PathVariable("currencyId") Long currencyId,
                                                    @RequestBody CurrencyDTO currencyDTO) {
        CurrencyDTO currency = currencyService.editCurrency(currencyId, currencyDTO);
        return new ResponseEntity<>(currency,HttpStatus.OK);
    }

    @DeleteMapping("/currency")
    public ResponseEntity<ResultDTO> deleteCurrencies(@RequestBody Map<String, List<Long>> userMap) {
        currencyService.deleteCurrencies(userMap.get("currenciesId"));
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }
}
