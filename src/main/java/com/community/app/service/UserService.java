package com.community.app.service;

import com.community.app.dto.UserDto;
import com.community.app.entity.User;
import com.community.app.exception.NotFoundException;
import com.community.app.mapper.UserMapper;
import com.community.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return mapUserToDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapUserToDto).toList();
    }

    public UserDto createUser(UserDto userDto) {
        User user = mapDtoToUser(userDto);
        userRepository.save(user);
        return mapUserToDto(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException();
        }
        userRepository.deleteById(id);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        updateExistingUser(userDto, user);
        userRepository.save(user);
        return mapUserToDto(user);
    }

    private UserDto mapUserToDto(User user) {
        return userMapper.userToUserDto(user);
    }

    private User mapDtoToUser(UserDto userDto) {
        return userMapper.userDtoToUser(userDto);
    }

    private void updateExistingUser(UserDto userDto, User user) {
        userMapper.updateUser(userDto, user);
    }
}
