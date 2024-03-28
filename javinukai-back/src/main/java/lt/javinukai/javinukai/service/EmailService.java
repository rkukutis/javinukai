package lt.javinukai.javinukai.service;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.entity.ParticipationRequest;
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

    private void sendMail(String receiverEmail, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(sender);
        email.setTo(receiverEmail);
        email.setSubject(subject);
        email.setText(message);
        log.info("Sending email: {}", email);
        // disabled for testing purposes
        //mailSender.send(email);
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
                
                Gerb. %1$s %2$s,
                     
                Dėkojame, kad prisiregistravote į Lietuvos spaudos fotografiją. Mes džiaugiamės turėdami jus šalia.
                Norėdami užbaigti registraciją, prašome paspausti nuorodą: %3$s
                Dar kartą dėkojame už registraciją mūsų svetainėje. Jei turite kokių nors klausimų, prašome susisiekti - %4$s.
                                
                Geriausi linkėjimai,
                Lietuvos spaudos fotografija


                English version:
                                
                Dear %1$s %2$s,
                                
                Thank you for signing up to Lithuanian press photography. We’re excited to have you on board
                To complete registration please follow the link: %3$s
                Thank you once again for registering for our website. In case of any questions, contact - %4$s.
                                
                Best regards,
                Lithuanian press photography""", user.getName(), user.getSurname(), uri.toString(), sender);

        sendMail(user.getEmail(), "Patvirtinkite savo el. paštą", message);
    }

    @SneakyThrows // temp
    public void sendPasswordResetToken(User user, String token) {

        URI uri = new URIBuilder().setScheme(httpScheme).setHost(client).setPathSegments("reset-password").addParameter("token", token).build();

        String message = String.format("""
                
                Gerb, %1$s %2$s,
                             
                Šį laišką siunčiame atsakydami į jūsų prašymą atkurti slaptažodį Lietuvos spaudos fotografijos puslapyje.
                Norėdami atkurti slaptažodį Lietuvos spaudos fotografijai, prašome paspausti žemiau esančią nuorodą:
                %3$s
                             
                Rekomenduojame saugoti slaptažodį ir nepateikti jo niekam. Jei manote, jog Jūsų slaptažodis buvo pažeistas, galite jį pakeisti nuėję į savo "Asmeninė informacija" puslapį ir paspausdami "Pakeisti slaptažodį".
                Jei Jums reikia pagalbos ar turite kitų klausimų, nedvejokite rašyti el. laišką adresu: %4$s.
                             
                Geriausi linkėjimai,
                Lietuvos spaudos fotografija    
                                
                                
                English version:
                                                                        
                Dear %1$s %2$s,

                We have sent you this email in response to your request to reset your password for Lithuanian press photography.
                To reset your password for Lithuanian press photography, please follow the link below:
                %3$s
                                
                We recommend that you keep your password secure and not share it with anyone. If you feel your password has been compromised, you can change it by going to your "Personal information" page and clicking on the "Change Password".\s
                If you need help or have any other questions, feel free to email: %4$s.

                Best regards,
                Lithuanian press photography""", user.getName(), user.getSurname(), uri.toString(), sender);

        sendMail(user.getEmail(), "Atkurkite slaptažodį", message);
    }

    public void participationStatusChangeNotification(User user, ParticipationRequest participationRequest) {

        String message = String.format("""
                
                Gerb. %1$s %2$s,
                                        
                Šiuo laišku norime Jums pranešti, jog konkursui dalyvauti pateiktos užklausos būsena pasikeitė.
                                        
                Konkurso pavadinimas: %3$s
                Nauja būsena: %4$s
                                        
                Jei Jums reikia pagalbos ar turite kitų klausimų, drąsiai rašykite el. laišką adresu %5$s.
                                        
                Geriausi linkėjimai,
                Lietuvos spaudos fotografija
                                        
                                                                                        
                English version:
                                        
                Dear %1$s %2$s,

                We have sent you this email to inform you, that the status of the request to participate in the competition has been changed.
                       
                Contest name: %3$s
                New status: %4$s
                         
                If you need help or have any other questions, feel free to email %5$s.

                Best regards,
                Lithuanian press photography""", user.getName(), user.getSurname(), participationRequest.getContest().getName(), participationRequest.getRequestStatus(), sender);

        sendMail(user.getEmail(), "Dalyvavimo prašymo būsena pasikeitė", message);

    }

}
