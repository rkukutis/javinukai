package lt.javinukai.javinukai.config.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
public class MailConfig {

    // Mailtrap.io login
    // username: pmo55400@zslsz.com
    // password: 98u7}sPeU'Ds@az

    @Value("${app.email.smtp-server.host}")
    private String host;
    @Value("${app.email.smtp-server.port}")
    private int port;
    @Value("${app.email.smtp-server.auth.user}")
    private String user;
    @Value("${app.email.smtp-server.auth.password}")
    private String password;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl sender =  new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(user);
        sender.setPassword(password);

        Properties props = sender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return sender;
    }

}
