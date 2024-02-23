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

    @Value("${app.client}")
    private String client;
    @Value("${app.scheme}")
    private String httpScheme;
    @Value("${app.email.sender-address}")
    private String sender;
    @Value("${app.email.contact-address}")
    private String contact;

    private void sendMail(String receiverEmail, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(sender);
        email.setTo(receiverEmail);
        email.setSubject(subject);
        email.setText(message);
        log.info("Sending email: {}", email);
        // disabled for testing purposes
         mailSender.send(email);
    }

    @SneakyThrows // temp
    public void sendEmailConfirmation(User user, String token) {

        URI uri = new URIBuilder()
                .setScheme(httpScheme)
                .setHost(client)
                .setPathSegments("confirm-email")
                .addParameter("token", token)
                .build();

        String message = String.format("""
                Dear %s %s,\s

                Thank you for signing up to Lithuanian press photography. We’re excited to have you on board\s

                To complete registration please follow the link: %s\s

                Thank you once again for registering for our website. In case of any questions, contact - %s.\s

                \s

                Best regards,\s

                Lithuanian press photography\s""", user.getName(), user.getSurname(), uri.toString(), contact);

        sendMail(user.getEmail(), "Confirm your email address", message);
    }

    @SneakyThrows // temp
    public void sendPasswordResetToken(User user, String token) {

        URI uri = new URIBuilder()
                .setScheme(httpScheme)
                .setHost(client)
                .setPathSegments("reset-password")
                .addParameter("token", token)
                .build();

        String message = String.format("""
                Dear %s %s,

                We have sent you this email in response to your request to reset your password for Lithuanian press photography.\s
                To reset your password for Lithuanian press photography, please follow the link below:\s

                %s

                We recommend that you keep your password secure and not share it with anyone. If you feel your password has been compromised, you can change it by going to your My Account Page and clicking on the "Change Password" link.\s

                If you need help or have any other questions, feel free to email %s.\s

                Best regards,\s

                ShapeLithuanian press photography""",user.getName(), user.getSurname(), uri.toString(), contact);

        sendMail(user.getEmail(), "Reset your password", message);
    }

}
