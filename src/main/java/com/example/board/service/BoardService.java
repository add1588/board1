package com.example.board.service;

import com.example.board.entity.MyBoard;
import com.example.board.entity.UserEntity;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor  //
public class BoardService {
    private final BoardRepository  boardRepository;
    private final UserRepository userRepository;

    @Transactional //저장하고 마무리 해라
    public Optional<MyBoard> save(Long userId, String title, String content){
        Optional<UserEntity> byId  = userRepository.findById(userId);
        byId.orElseThrow( ()-> new RuntimeException("사용자 정보가 없습니다"));

        UserEntity userEntity = byId.get();
        MyBoard newBoard = MyBoard.builder()
                .title(title)
                .content(content)
                .userEntity(userEntity)
                .build();

        MyBoard saveBoard =  boardRepository.save( newBoard );
        return Optional.of(saveBoard);
    }
    public ArrayList<MyBoard> findAll(){
//        System.out.println("BoardService :: findAll()");
        return new ArrayList<> (boardRepository.findAll());
    }

    //조회수를 증가시킬떄
    public void incrementHit(Long id) {
        Optional<MyBoard> byId = findById(id);
        //해당 id로 보드 테이블의 레코드를 찾았을떄 
        byId.ifPresent(board -> {
            board.setHit(board.getHit() + 1);
            boardRepository.save(board);
        });
    }

    public Long  incrementLike(Long id){
        AtomicReference<Long> ret = new AtomicReference<>(0L);
        Optional<MyBoard> byId = findById(id);
        //해당 id로 보드 테이블의 레코드를 찾았을떄
        byId.ifPresent(board -> {
            board.setLike_cnt(board.getLike_cnt() + 1);
           MyBoard saved = boardRepository.save(board);
           ret.set(saved.getLike_cnt());

        });

        return ret.get();
    }

    public Long incrementDisLike(Long id){
        AtomicReference<Long> ret = new AtomicReference<>(0L);
        Optional<MyBoard> byId = findById(id);
        //해당 id로 보드 테이블의 레코드를 찾았을떄
        byId.ifPresent(board -> {
            board.setDislike(board.getDislike() + 1);
            MyBoard saved = boardRepository.save(board);
            ret.set(saved.getDislike());
        });
        return ret.get();
    }

    public Optional<MyBoard> findById(Long id) {
        return boardRepository.findById(id);
    }
}
