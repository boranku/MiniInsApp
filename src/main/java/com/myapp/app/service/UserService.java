package com.myapp.app.service;

import com.myapp.app.dto.UserResponse;
import com.myapp.app.entity.Role;
import com.myapp.app.entity.User;
import com.myapp.app.exception.UnauthorizedException;
import com.myapp.app.util.PasswordEncoder;
import com.myapp.app.exception.BadRequestException;
import com.myapp.app.exception.NotFoundException;
import com.myapp.app.repository.UserRepository;
import com.myapp.app.util.RequestContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getById(UUID id) {
        User u = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return map(u);
    }

    public UserResponse me() {
        User u = RequestContext.getUser();
        if (u == null) throw new BadRequestException("Not authenticated");
        return map(u);
    }

    public void changePassword(String currentPassword, String newPassword) {
        User u = RequestContext.getUser();
        if (u == null) throw new BadRequestException("Not authenticated");
        if (!passwordEncoder.matches(currentPassword, u.getPassword())) {
            throw new BadRequestException("Current password incorrect");
        }
        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);
    }

    public void deleteMe() {
        User u = RequestContext.getUser();
        if (u == null) throw new BadRequestException("Not authenticated");
        userRepository.delete(u);
    }

    public void deleteByAdmin(UUID id) {
        User current = RequestContext.getUser();
        if (current.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only ADMIN can delete users");
        }
        User target = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(target);
    }

    private UserResponse map(User u) {
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setUsername(u.getUsername());
        r.setRole(u.getRole().name());
        return r;
    }
}
