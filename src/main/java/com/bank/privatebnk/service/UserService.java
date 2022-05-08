package com.bank.privatebnk.service;


import com.bank.privatebnk.controller.dto.UserDTO;
import com.bank.privatebnk.controller.request.RegisterRequest;
import com.bank.privatebnk.controller.request.UserUpdateRequest;
import com.bank.privatebnk.domain.Role;
import com.bank.privatebnk.domain.User;
import com.bank.privatebnk.domain.enumeration.UserRole;
import com.bank.privatebnk.exception.ConflictException;
import com.bank.privatebnk.exception.ResourceNotFoundException;
import com.bank.privatebnk.exception.message.ExceptionMessage;
import com.bank.privatebnk.repository.RoleRepository;
import com.bank.privatebnk.repository.UserRepository;
import com.bank.privatebnk.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    public void registerUser(RegisterRequest registerRequest) {
        if(userRepository.existsByUserName(registerRequest.getUserName())) {
            throw new ConflictException(String.format(ExceptionMessage.USERNAME_ALREADY_EXIST_MESSAGE, registerRequest.getUserName()));
        }

        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException(String.format(ExceptionMessage.EMAIL_ALREADY_EXIST_MESSAGE, registerRequest.getEmail()));
        }


        if(userRepository.existsBySsn(registerRequest.getSsn())) {
            throw new ConflictException(String.format(ExceptionMessage.SSN_ALREADY_EXIST_MESSAGE, registerRequest.getSsn()));
        }

         Set<Role> userRoles=new HashSet<>();

        //We add ROLE_CUSTOMER for every registered user by default
        //find customer role fm role repo
        Role role=roleRepository.findByName(UserRole.ROLE_CUSTOMER).
        orElseThrow(
         ()->new ResourceNotFoundException(
                 String.format(ExceptionMessage.ROLE_NOT_EXIST_MESSAGE,
                        UserRole.ROLE_CUSTOMER.name())));

        userRoles.add(role);
        // I am creating user from User domain by using the values from registerRequest
        User user=new User(
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getSsn(),
                registerRequest.getUserName(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getPhoneNumber(),
                registerRequest.getAddress(),
                registerRequest.getDateOfBirth(),
                //line 50-59:Because we converted all string roles into the userRoles set
               //user service line 54 we gathered customer role from repo with "findByName" method
                //to which we gave "UserRole.ROLE_CUSTOMER" as parameter
                // User domain line 70 in constructor argument to be given is "Set<Role> roles"
                userRoles
        );

        userRepository.save(user);

        User userSaved = userRepository.findById(user.getId()).
           orElseThrow(()->new ResourceNotFoundException(
             String.format(ExceptionMessage.USER_NOT_FOUND_MESSAGE, user.getId())));

        accountService.createAccount(userSaved);
    }

    public User findOneWithAuthoritiesByUserName() {
        String currentUserLogin= SecurityUtils.getCurrentUserLogin().
                orElseThrow(
                        ()->new ResourceNotFoundException(
                                ExceptionMessage.CURRENTUSER_NOT_FOUND_MESSAGE));

        User user=userRepository
                .findOneWithAuthoritiesByUserName(currentUserLogin)
                .orElseThrow(
                ()->new ResourceNotFoundException(
                String.format(ExceptionMessage.USER_NOT_FOUND_MESSAGE,currentUserLogin)));

        return user;
    }
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        String.format(ExceptionMessage.USERID_NOT_FOUND_MESSAGE, id)));
    }

    public void updateUser(Long id, UserUpdateRequest request) {
        boolean emailExist=userRepository.existsByEmail(request.getEmail());
        User foundUser=findById(id);
        //avoid that user updates his/her email with some other user's email
        if(emailExist && !foundUser.getEmail().equals(request.getEmail())) {
            throw new ConflictException(String.format(ExceptionMessage.EMAIL_ALREADY_EXIST_MESSAGE, request.getEmail()));
        }

        Boolean ssnExist = userRepository.existsBySsn(request.getSsn());

        if(ssnExist&& !foundUser.getSsn().equals(request.getSsn())) {
            throw new ConflictException(String.format(ExceptionMessage.SSN_ALREADY_EXIST_MESSAGE, request.getSsn()));
        }


        foundUser.setFirstName(request.getFirstName());
        foundUser.setLastName(request.getLastName());
        foundUser.setEmail(request.getEmail());
        foundUser.setDateOfBirth(request.getDateOfBirth());
        foundUser.setPhoneNumber(request.getPhoneNumber());
        foundUser.setSsn(request.getSsn());
        foundUser.setAddress(request.getAddress());
        foundUser.setEnabled(request.getEnabled());
        //client send roles as string but we need to convert these roles to our role
        Set<Role> roles= addRoles(request.getRoles());
        foundUser.setRoles(roles);

        userRepository.save(foundUser);


    }
    private Set<Role> addRoles(Set<String> userRoles){
        Set<Role> roles=new HashSet<>();

        if(userRoles==null) {
            Role role=roleRepository.findByName(UserRole.ROLE_CUSTOMER)
                    .orElseThrow(()->new ResourceNotFoundException
                            (String.format(ExceptionMessage.ROLE_NOT_EXIST_MESSAGE,UserRole.ROLE_CUSTOMER.name())));

            roles.add(role);
        }else {

            userRoles.forEach(role->{
                switch (role) {
                    case "Admin":
                        Role adminRole=roleRepository.findByName(UserRole.ROLE_ADMIN)
                          .orElseThrow(()->new ResourceNotFoundException
                            (String.format(ExceptionMessage.ROLE_NOT_EXIST_MESSAGE,UserRole.ROLE_ADMIN.name())));

                        roles.add(adminRole);

                        break;

                    default:
                        Role customerRole=roleRepository.findByName(UserRole.ROLE_CUSTOMER)
                          .orElseThrow(()->new ResourceNotFoundException
                            (String.format(ExceptionMessage.ROLE_NOT_EXIST_MESSAGE,UserRole.ROLE_CUSTOMER.name())));

                        roles.add(customerRole);
                }

            });

        }
        return roles;

    }

    public Page<UserDTO> getUsers(Pageable pageable){

        return userRepository.findUsersPage(pageable);
    }
}
