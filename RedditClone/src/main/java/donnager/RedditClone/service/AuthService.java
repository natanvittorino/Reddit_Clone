package donnager.RedditClone.service;

import donnager.RedditClone.dto.AuthenticationResponse;
import donnager.RedditClone.dto.LoginRequest;
import donnager.RedditClone.dto.RegisterRequest;
import donnager.RedditClone.exceptions.SpringRedditException;
import donnager.RedditClone.model.NotificationEmail;
import donnager.RedditClone.model.User;
import donnager.RedditClone.model.VerificationToken;
import donnager.RedditClone.repositories.UserRepo;
import donnager.RedditClone.repositories.VerificationTokenRepo;
import donnager.RedditClone.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final VerificationTokenRepo verificationTokenRepo;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepo.save(user);


        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail(
                "Please Activate your Account",
                    user.getEmail(),
                 "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepo.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepo.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepo.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with name " + username));
        user.setEnabled(true);
        userRepo.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }
}
