package com.sparta.board.controller;

import com.sparta.board.dto.LoginRequestDto;
import com.sparta.board.dto.ResponseDto;
import com.sparta.board.dto.SignUpRequestDto;
import com.sparta.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@Controller  //restcontroller싸야하는 것 아닌가?
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody  SignUpRequestDto signUpRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            Map<String, String> validatorResult = userService.validateHandling(errors);
            return ResponseEntity.ok(validatorResult);
        } else{
            userService.signup(signUpRequestDto);
            String msg = "회원가입에 성공했습니다. " ;
            return ResponseEntity.ok(new ResponseDto(msg,200));
        }

    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        String msg = "로그인에 성공했습니다. " ;
        return ResponseEntity.ok(new ResponseDto(msg, 200));
    }


}
