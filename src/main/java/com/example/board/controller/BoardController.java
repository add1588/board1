package com.example.board.controller;

import com.example.board.entity.MyBoard;
import com.example.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/main")
    public String boardMain(HttpSession httpSession, Model model){

//        List<MyBoard> boards = boardService.findAll();
//          boards.forEach(System.out::println); // 디버그창
//
//           model.addAttribute("boards", boards);

       try {
           String userNickName = httpSession.getAttribute("nick_name").toString();
           String userId = httpSession.getAttribute("user_id").toString();
           String userEmail = httpSession.getAttribute("user_email").toString();

           model.addAttribute("nick_name", userNickName);
           model.addAttribute("user_id", userId);
           model.addAttribute("user_email", userEmail);

           //게시판에 있는 모든 내용을 불러온다
           List<MyBoard> boards = boardService.findAll();
//           boards.forEach(System.out::println); // 디버그창

           model.addAttribute("boards", boards);

       } catch (NullPointerException e) {
           String msg = e.getMessage();
           model.addAttribute("message1", msg);
           model.addAttribute("message2", "세션 정보가 없습니다");
       }
        return "/boards/main";
    }

    @GetMapping("/write-form")
    public String writeForm(HttpSession httpSession, Model model) {
        String userId = httpSession.getAttribute("user_id").toString();
        String userEmail = httpSession.getAttribute("user_email").toString();
        String userNickName = httpSession.getAttribute("nick_name").toString();

        if (userId.isEmpty() || userEmail.isEmpty() || userNickName.isEmpty()) {
            model.addAttribute("message", "사용자 정보 만료 , 로그인 다시 해주세요");
            return "/error";
        }

        model.addAttribute("nick_name", userNickName);
        model.addAttribute("user_id", userId);
        model.addAttribute("user_email", userEmail);

        return "/boards/write";
    }
        @PostMapping("/write")
        public String write(@RequestParam("userId") String userId,
                             @RequestParam("boardTitle")  String boardTitle,
                             @RequestParam("boardContent") String boardContent,
                             HttpSession httpSession,
                             Model model){

            //데이터베이스에 글 내용을 저장
            System.out.println("title :" + boardTitle);
            System.out.println("content :" + boardContent);
            System.out.println("userId :" + userId);

            Long id = Long.parseLong(userId); //아까 세팅해놓은 write-form페이지에서 넘어오는 user_id이다
            String ids = httpSession.getAttribute("user_id").toString();
            if(ids.isEmpty()){
                String msg = "로그인 정보가 만료";
                model.addAttribute("message1", msg);

                return "error";
            }
            if( !id.equals(Long.parseLong(ids))){
                String msg = "잘못된 접근";
                model.addAttribute("message1", msg);
                return "error";
            }

            Optional<MyBoard> bySaved =  boardService.save(id, boardTitle, boardContent);
            if(bySaved.isEmpty()){
                String msg = "게시글 저장에 실패했습니다";
                model.addAttribute("message1", msg);
                return "error";
            }
            //리스트로 게시글들을 가져온다

            //
            return "redirect:/boards/main"; //redirect 여기에서 위에 main으로 다시 보내는것
        }

        @GetMapping("/{id}")
        public String detail(@PathVariable Long id, HttpSession httpSession, Model model){
            try {
                //조회수를 1증가 시킨다
                boardService.incrementHit(id);
                
                //주어진 id값으로   board테이블에서 일치하는 해당 레코드를 한개 가져온다
                Optional<MyBoard> byId = boardService.findById(id);
                // 해당 id값이 테이블에 존재하지 않으면 error페이지로 이동
                byId.orElseThrow(() -> new RuntimeException("해당 게시글이 존잴하지 않습니다"));
                // board 라는 모델 변수에
                model.addAttribute("board", byId.get());

                String userId = httpSession.getAttribute("user_id").toString();
                String userNickName = httpSession.getAttribute("nick_name").toString();

                model.addAttribute("nick_name", userNickName);
                model.addAttribute("user_id", userId);
//                System.out.println( model.getAttribute("board"));
                return "/boards/detail";
            } catch (RuntimeException e){
                model.addAttribute("message1", e.getMessage());
                return "error";
            }
        }

        @PostMapping("/inc_like")
        @ResponseBody
        public String incrementLike(@RequestParam("bid") String id, Model model){
            System.out.println("BoardController::incrementLike : boardId = " + id);
            Long newCount = boardService.incrementLike(Long.parseLong(id));
            return String.format("%d", newCount);


        }

        @PostMapping("/dislike")
        @ResponseBody
        public String incrementDisLike(@RequestParam("bid") String id){
            Long newCount = boardService.incrementDisLike(Long.parseLong(id));
            return String.format("%d", newCount);


        }


    }
