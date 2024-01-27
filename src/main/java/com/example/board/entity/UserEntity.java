package com.example.board.entity;

import com.example.board.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.lang.reflect.Member;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
@ToString
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false)
    private String userNickName;

    public static UserEntity toUserEntity(UserDTO userDTO){
        UserEntity userEntity = new UserEntity(); //항상 객체를 생성해서 쓰고 있다
        userEntity.setUserEmail(userDTO.getUserEmail());
        userEntity.setUserPassword(userDTO.getUserPassword());
        userEntity.setUserNickName(userDTO.getUserNickName());

        return userEntity;
    }

    public static UserEntity toUpdateUserEntity(UserDTO userDTO){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDTO.getId());
        userEntity.setUserEmail(userDTO.getUserEmail());
        userEntity.setUserPassword(userDTO.getUserPassword());
        userEntity.setUserNickName(userDTO.getUserNickName());

        return userEntity;
    }
}
