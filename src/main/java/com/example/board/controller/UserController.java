package com.example.board.controller;

import com.example.board.application.dto.UserDto;
import com.example.board.application.security.auth.LoginUser;
import com.example.board.application.service.UserService;
import com.example.board.application.validator.CustomValidators;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final CustomValidators.EmailValidator emailValidator;
    private final CustomValidators.NicknameValidator nicknameValidator;

    @InitBinder
    public void validatoBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
        binder.addValidators(nicknameValidator);
    }

    /**
     * 회원가입 : join
     */

    @GetMapping("/auth/join")
    public String joinForm() {
        return "/user/joinForm";
    }

    //회원가입 로직
    @PostMapping("/auth/joinPro")
    public String saveUser(@Valid UserDto.Request dto, Errors errors, Model model) {

        if(errors.hasErrors()) {
            model.addAttribute("user",dto);

            Map<String,String> validateResult = userService.validateHandling(errors);
            for(String key : validateResult.keySet()) {
                model.addAttribute(key, validateResult.get(key));
            }
            return "/user/joinForm";
        }

        userService.joinUser(dto);

        model.addAttribute("message", "회원가입이 완료되었습니다.");
        model.addAttribute("searchUrl", "/");
        return "message";
    }

//    @PostMapping("/auth/joinPro")
//    public String saveUser(UserDto.Request dto, RedirectAttributes redirectAttributes) {
//        userService.joinUser(dto);
//        //redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
//        return "redirect:/auth/login"; // 로그인 페이지로 리디렉션
//    }

    /**
     * 로그인 로그아웃
     */

    @GetMapping("/auth/login")
    public String login(@RequestParam(value = "error", required = false)String error,
     @RequestParam(value = "exception", required = false)String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "/user/user-login";
    }

    /**
     * 
     * 관리자가 참조하는 페이지 - 회원 리스트 조회
     * 여기는 안 건들어도 됨. 완성함
     *
     */

    //회원 리스트 조회 - 여기서 회원 하나를 누르면 단일 회원 조회인 /user/view/{id}로 가져야 함.
    @GetMapping("/admin/users")
    public String viewUsers(Model model) {
        List<UserDto.Response> users = userService.viewUsers();
        model.addAttribute("users", users);
        return "/user/userList";
    }

    // 단일 회원 조회
    @GetMapping("/admin/users/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        UserDto.Response user = userService.viewUser(id);
        model.addAttribute("user",user);

        return "/user/userView";
    }

    /**
     * 관리자가 관여하지 않는 부분으로 만들 예정이라
     * 수정 범주를 username, nickname, email 로만 줄임
     */

    @GetMapping("/user/modify")
    public String modifyUser(@LoginUser UserDto.Response user, Model model) {
        if(user!=null) model.addAttribute("user",user);
        return "/user/user-modify";
    }

    // 회원 수정
//    @PostMapping("/modify")
//    public String updateUser(@ModelAttribute UserDto.Request userRequest,
//                             UserDto.Response userResponse,
//                             Model model) {
//
//        if(userResponse!=null) {
//            userService.modify(userRequest);
//            model.addAttribute("message", "회원 수정이 완료되었습니다.");
//            model.addAttribute("searchUrl", "/user/userView");
//        }
//        else {
//            model.addAttribute("message","존재하지 않는 회원입니다.");
//            model.addAttribute("searchUrl", "/user/userView");
//        }
//
//        return "message";
//    }


}
