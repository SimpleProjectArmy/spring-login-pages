package com.example.simplespringboot.service;

import com.example.simplespringboot.entity.User;
import com.example.simplespringboot.exception.UserException;
import com.example.simplespringboot.repository.UserRepository;
import com.example.simplespringboot.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private  final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public  UserService(UserRepository repository, PasswordEncoder passwordEncoder){
        this.repository =  repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findById(String id) {
        return repository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User update(User user){
        return repository.save(user);
    }

    public  User updateName(String id,String name) throws UserException {
        Optional<User> opt = repository.findById(id);
        if(opt.isEmpty()){
            throw UserException.notFound();
        }
        User user = opt.get();
        user.setName(name);
        return repository.save(user);
    }

    public void deleteById(String id ){
        repository.deleteById(id);
    }
    public boolean matchPassword(String rawPassword,String encodedPassword){
        return passwordEncoder.matches(rawPassword,encodedPassword);
    }

    public User create(String email, String password, String name,String token,Date tokenExpireDate) throws UserException {

        // validate

        if(Objects.isNull(email)){
            throw UserException.createEmailNull();
        }

        if(Objects.isNull(password)){
            throw UserException.createPasswordNull();
        }

        if(Objects.isNull(name)){
            throw UserException.createNameNull();
        }
        // write for make sure
        if(repository.existsByEmail(email)){
            throw  UserException.createEmailDuplicated();
        }

        User entity =  new User();
        entity.setEmail(email);
        entity.setPassword(passwordEncoder.encode(password));
        entity.setName(name);
        entity.setToken(token);
        entity.setTokenExpire(tokenExpireDate);
        return repository.save(entity);
    }

}
