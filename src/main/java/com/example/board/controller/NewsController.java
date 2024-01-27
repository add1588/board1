package com.example.board.controller;

import com.example.board.entity.CategoryEntity;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {


    @GetMapping("/main")
    public String newsMain(HttpSession httpSession, Model model){
        String userNickName = httpSession.getAttribute("nick_name").toString();
        String userId = httpSession.getAttribute("user_id").toString();
        String userEmail = httpSession.getAttribute("user_email").toString();

        model.addAttribute("nick_name", userNickName);
        model.addAttribute("user_id", userId);
        model.addAttribute("user_email", userEmail);

        return "/news/main";
    }
}
