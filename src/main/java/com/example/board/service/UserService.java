package com.example.board.service;

import com.example.board.dto.UserDTO;
import com.example.board.entity.UserEntity;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(UserDTO userDTO){
        // userDTO로 부터 받은 email을 이용하여 user 테이블에서 검색
        // 이미 존재하는 email이면 에러를 일으킴
        Optional<UserEntity> byEmail = userRepository.findByUserEmail(userDTO.getUserEmail());
        if(byEmail.isPresent()){
            System.out.println(userDTO.getUserEmail() +  " : 이미 존재하는 이메일입니다.");
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        // 존재하지 않는 email이면 테이블에 데이터(usuerDTO > userEntity)를 저장
        UserEntity userEntity = UserEntity.toUserEntity(userDTO);
        try {
            userRepository.save(userEntity);
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    public UserEntity login(UserDTO userDTO){
        Optional<UserEntity> byEmail =
        userRepository.findByUserEmailAndUserPassword(userDTO.getUserEmail(),
                userDTO.getUserPassword());

        if (byEmail.isPresent()){
            return byEmail.get();
        } else {
            throw new RuntimeException("사용자를 찾을 수 없습니다");
        }
    }

    public UserEntity update(UserDTO userDTO){
        UserEntity userEntity = UserEntity.toUpdateUserEntity(userDTO);
        try {
           UserEntity savedUser =  userRepository.save(userEntity);
           return  savedUser;
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public UserEntity findByUserEmail(String email) {

       Optional<UserEntity> byEmail =  userRepository.findByUserEmail(email);
        return byEmail.orElse(null);

    }
}
