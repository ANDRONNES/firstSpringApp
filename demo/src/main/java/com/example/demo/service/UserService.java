package com.example.demo.service;

import com.example.demo.repository.User;
import com.example.demo.repository.UserRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
    public User create(User user){
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if(optionalUser.isPresent()) throw new IllegalStateException("User with that email is already exists");
        user.setAge(Period.between(user.getBirth(), LocalDate.now()).getYears());
        return userRepository.save(user);
    }
    public void delete(Long id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) userRepository.deleteById(id);
        else throw new IllegalStateException("There is no user with that id");
    }


    @Transactional
    public void update(Long id,String email,String name){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()) {
            throw new IllegalStateException("There is no user with that id");
        }
        User user = optionalUser.get();

        if(email != null && !email.equals(user.getEmail())){
            Optional<User> foundByEmail = userRepository.findByEmail(email);
            if(foundByEmail.isPresent()){
                throw new IllegalStateException("User with that email is already exists");
            }
            user.setEmail(email);
        }

        if(name != null && !name.equals(user.getName())){
            user.setName(name);
        }

//        userRepository.save(user);
    }
}
