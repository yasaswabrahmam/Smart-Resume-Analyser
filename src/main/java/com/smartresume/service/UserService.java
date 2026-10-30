package com.smartresume.service;

import com.smartresume.dto.UserRegistrationDto;
import com.smartresume.model.User;
import com.smartresume.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }

    @Transactional
    public User registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("An account with this email already exists");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = new User();
        user.setEmail(dto.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFullName(dto.getFullName().trim());
        user.setRole("ROLE_USER");
        if (dto.getUserType() != null && !dto.getUserType().isBlank()) {
            user.setUserType(dto.getUserType());
        }
        if (dto.getTargetRole() != null && !dto.getTargetRole().isBlank()) {
            user.setTargetRole(dto.getTargetRole());
        }
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Transactional
    public User updateProfile(User user, String userType, String targetRole, String degree, String institution, Integer gradYear, Integer expYears, String currentRole, String skills, String locationPref, Integer weeklyHours) {
        if (userType != null && !userType.isBlank()) {
            user.setUserType(userType);
        }
        if (targetRole != null && !targetRole.isBlank()) {
            user.setTargetRole(targetRole);
        }
        user.setEducationDegree(degree);
        user.setInstitution(institution);
        user.setGraduationYear(gradYear);
        user.setExperienceYears(expYears != null ? expYears : 0);
        user.setCurrentRole(currentRole);
        user.setCurrentSkills(skills);
        if (locationPref != null && !locationPref.isBlank()) {
            user.setLocationPreference(locationPref);
        }
        if (weeklyHours != null && weeklyHours > 0) {
            user.setWeeklyLearningHours(weeklyHours);
        }
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return Optional.empty();
        }
        return userRepository.findByEmail(auth.getName());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public long getTotalUserCount() {
        return userRepository.count();
    }
}
