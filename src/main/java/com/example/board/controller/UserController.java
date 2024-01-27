package com.example.board.controller;

import com.example.board.dto.UserDTO;
import com.example.board.entity.UserEntity;
import com.example.board.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping("/loginForm")
    public String loginForm(){
        return  "login";
    }
    @GetMapping("/joinForm")
    public String joinForm(){
        System.out.println("/member/joinForm");
        return "join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute UserDTO userDTO, Model model){
        try {
            userService.save(userDTO);
        } catch (RuntimeException e){
            return "error";
        }

        return "login";

    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO userDTO, Model model, HttpSession httpSession){
        UserEntity userEntity;
        try{
          userEntity   = userService.login(userDTO);
        } catch (RuntimeException e){
            return "error";
        }

        httpSession.setAttribute("nick_name", userEntity.getUserNickName());
        httpSession.setAttribute("user_id", userEntity.getId());
        httpSession.setAttribute("user_email", userEntity.getUserEmail());


        return "redirect:/boards/main";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate(); //세션 연결 종료
        System.out.println("@@@@@@@@@@@@@@@@@@ logout @@@@@@@@@@@@@@@@@@@@@@@@@@");
        return "redirect:/member/loginForm";
    }
    @GetMapping("/updateForm")
    public  String updateForm(Model model, HttpSession httpSession){

        String email = (String)httpSession.getAttribute("user_email");
        System.out.println("user_email" + email);
        UserEntity userEntity = userService.findByUserEmail(email);
        if (userEntity==null){
            return "error";
        }

        model.addAttribute("nick_name", userEntity.getUserNickName());
        model.addAttribute("user_id", userEntity.getId());
        model.addAttribute("user_email", userEntity.getUserEmail());
        model.addAttribute("user_password", userEntity.getUserPassword());

        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute UserDTO userDTO, Model model,HttpSession httpSession){
        try {
            System.out.println("UserControlloer::update :" + userDTO);
            UserEntity saved = userService.update(userDTO);

            model.addAttribute("nick_name", saved.getUserNickName());
            model.addAttribute("user_id", saved.getId());
            model.addAttribute("user_email", saved.getUserEmail());

            httpSession.setAttribute("nick_name", saved.getUserNickName());
            httpSession.setAttribute("user_id", saved.getId());
            httpSession.setAttribute("user_email", saved.getUserEmail());

        } catch (RuntimeException e){
            return "error";
        }


        return "/boards/main";
    }
    @PostMapping("/emailCheck")
    public @ResponseBody String emailCheck(@RequestParam("userEmail") String userEmail){
        if (userEmail.isEmpty())
            return "이메일을 입력해주세요";

        UserEntity userEntity = userService.findByUserEmail(userEmail);
        if (userEntity==null)
            return "ok";

        return  "이미 존재하는 이메일입니다";

    }
}
