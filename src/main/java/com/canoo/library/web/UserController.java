package com.canoo.library.web;

import com.canoo.library.model.User;
import com.canoo.library.persistence.repository.RecordNotFoundException;
import com.canoo.library.persistence.repository.UserRepository;
import com.canoo.library.security.JWTUtil;
import com.canoo.library.security.UsernamePasswordDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    //http://username:password@example.com/

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ResponseEntity<String> usernamePasswordLogin(@RequestHeader("Authorization") String authHeader) {
        String[] decoded = UsernamePasswordDecoder.decodeUserNamePassword(authHeader);
        String username = decoded[0];
        String password = decoded[1];

        User user = repository.getByName(username);

        if(user==null || !encoder.matches(password, user.getPassword())){
            return new ResponseEntity<>("unauthorized access",HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(jwtUtil.generateToken(user),HttpStatus.OK);
    }

    @RequestMapping(value="/register", method=RequestMethod.PUT)
    public ResponseEntity<String> usernamePasswordRegistration(@RequestBody User userToCreate) {

        User user = null;

        try{
            user = repository.getByName(userToCreate.getUsername());
            if(user!=null){
                return new ResponseEntity<>(jwtUtil.generateToken(user),HttpStatus.BAD_REQUEST);
            }
        } catch(RecordNotFoundException e){
            user = new User.Builder()
                            .setName(userToCreate.getUsername())
                            .setPassword(encoder.encode(userToCreate.getPassword()))
                            .setEmail(userToCreate.getEmail())
                            .build();
            repository.save(user);
        }

        return new ResponseEntity<>(jwtUtil.generateToken(user),HttpStatus.OK);
    }

}
