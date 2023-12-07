package com.example.todolistbackend.bussines.concretes;


import com.example.todolistbackend.bussines.abstractt.IUserService;
import com.example.todolistbackend.Entity.User;
import com.example.todolistbackend.repository.UserRepository;
import com.example.todolistbackend.utilities.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManager implements IUserService {
    private final UserRepository userRepository;

    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public DataResult<List<User>> getAllUser() {

        return new SuccesDataResult<List<User>>("Data getirildi",userRepository.findAll());
    }

    @Override
    public DataResult<User> getOneUserById(int userId) {
        Optional<User>userTest=userRepository.findById(userId);
        if (userTest.isPresent()){
            return new SuccesDataResult<User>("Kullanıcı getirildi",userTest.get());
        }

        return new ErrorDataResult<User>("Böyle bir kullanıcı yok",null);
    }

    @Override
    public DataResult<User> getByUserName(String userName) {

        return new SuccesDataResult<User>
                ("Veri getirildi",userRepository.findByUserName(userName));

    }

    @Override
    public Result createOneUser(User user) {

        User userTest=userRepository.save(user);
        if (userTest==null){
            return new ErrorResult("Kullanıcı Eklenemedi !");

        }
        return new SuccessResult("Kullanıcı Başarıyla Eklendi");
    }

}
