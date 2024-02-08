package lt.javinukai.javinukai.service;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.User;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@Async
@Slf4j
@RequiredArgsConstructor
public class EmailService {
private final JavaMailSender mailSender;

    @Value("${app.host}")
    private String host;
    @Value("${app.scheme}")
    private String httpScheme;

    private void sendMail(String receiverEmail, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("no-reply@contest.com");
        email.setTo(receiverEmail);
        email.setSubject(subject);
        email.setText(message);
        log.info("Sending email: {}", email);
        // disabled for testing purposes
        // mailSender.send(email);
    }

    @SneakyThrows // temp
    public void sendEmailConfirmation(User user, String token) {

        URI uri = new URIBuilder()
                .setScheme(httpScheme)
                .setHost("localhost:5173")
                .setPathSegments("confirm-email")
                .addParameter("token", token)
                .build();

        String message = "Welcome to the site, " + user.getName() +
                ". You can confirm your email by clicking this link: " +
                uri.toString();

        sendMail(user.getEmail(), "Confirm your email address", message);
    }

    @SneakyThrows // temp
    public void sendPasswordResetToken(User user, String token) {

        URI uri = new URIBuilder()
                .setScheme(httpScheme)
                .setHost("localhost:5173")
                .setPathSegments("reset-password")
                .addParameter("token", token)
                .build();

        String message = user.getName() +
                ", you can reset your password by following this link: " +
                uri.toString();

        sendMail(user.getEmail(), "Reset your password", message);
    }

}
