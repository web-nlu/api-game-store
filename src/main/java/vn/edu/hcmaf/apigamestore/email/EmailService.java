package vn.edu.hcmaf.apigamestore.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.product.accountInfo.AccountInfoDto;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendmail() {
        // test send 1 email to 20130166@st.hcmuaf.edu.vn
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo("20130166@st.hcmuaf.edu.vn");
            helper.setSubject("Test Service send email api-game-store");
            helper.setText("this is test mail");
            System.out.println("Start sending email");
            mailSender.send(message);
        }catch (MessagingException e) {
            throw new RuntimeException("Gửi email thất bại: " + e.getMessage(), e);
        }
    }

    public void sendOrderConfirmationEmail(String toEmail, String customerName, String orderNumber, List<AccountInfoDto> accounts) {
        String subject = "Xác nhận đơn hàng #" + orderNumber;
        String htmlContent = generateHtmlContent(customerName, orderNumber, accounts);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi email thất bại: " + e.getMessage(), e);
        }
    }

    private String generateHtmlContent(String customerName, String orderNumber, List<AccountInfoDto> accounts) {
        System.out.println(accounts);
        String rows = accounts.stream()
                .map(a -> String.format(
                        "<tr><td>%s</td><td>%s</td><td>%s</td></tr>",
                        escapeFormat(a.getAccountTitle()),
                        escapeFormat(a.getUsername()),
                        escapeFormat(a.getPassword())
                ))
                .collect(Collectors.joining());
        System.out.println(rows);
        return String.format(EmailTemplate.ORDER_CONFIRMATION, customerName, orderNumber, rows);
    }

    private String escapeFormat(String input) {
        if (input == null) return "";
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                .replace("%", "%%"); // Để tránh lỗi String.format()
    }

    public static void main(String[] args) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("hoanghailata@gmail.com");
        mailSender.setPassword("shgveoajuydqiwwv");
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        // test send mail
        EmailService emailService = new EmailService(mailSender); // Mock JavaMailSender for testing
        emailService.sendmail();
    }
}
