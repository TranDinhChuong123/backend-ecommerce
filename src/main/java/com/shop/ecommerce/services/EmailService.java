package com.shop.ecommerce.services;


import com.shop.ecommerce.models.VerificationCode;
import com.shop.ecommerce.repositories.VerificationCodeRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;


import java.security.SecureRandom;


import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final VerificationCodeRepository verificationCodeRepository;
    private final TemplateEngine templateEngine;




    // Tạo mã xác thực ngẫu nhiên
    public String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000); // Mã gồm 6 chữ số
        return String.valueOf(code);
    }

    // Gửi mã xác thực và lưu vào cơ sở dữ liệu
    public boolean sendVerificationEmail(String email) {
        try {
            // Tạo mã xác thực
            String verificationCode = generateVerificationCode();

            // Lưu mã xác thực vào cơ sở dữ liệu
            VerificationCode codeEntity = new VerificationCode();
            codeEntity.setEmail(email);
            codeEntity.setCode(verificationCode);
            codeEntity.setSentTime(LocalDateTime.now());
            codeEntity.setExpirationTime(LocalDateTime.now().plusMinutes(10)); // Hết hạn sau 10 phút
            verificationCodeRepository.save(codeEntity);


            Context context = new Context();
            context.setVariable("verificationCode", verificationCode);

            // Tạo nội dung HTML từ Thymeleaf template
            String htmlContent = templateEngine.process("verification-email", context);

            // Tạo và gửi email với nội dung HTML
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(email);
            messageHelper.setSubject("Your Verification Code");
            messageHelper.setText(htmlContent, true); // true để gửi email dưới dạng HTML

            mailSender.send(mimeMessage);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendResetPasswordEmail(String email) {

        try {
            String code = generateVerificationCode();

            VerificationCode codeEntity = new VerificationCode();
            codeEntity.setEmail(email);
            codeEntity.setCode(code);
            codeEntity.setSentTime(LocalDateTime.now());
            codeEntity.setExpirationTime(LocalDateTime.now().plusMinutes(10)); // Hết hạn sau 10 phút
            verificationCodeRepository.save(codeEntity);

            Context context = new Context();
            context.setVariable("verificationCode", code);
            String htmlContent = templateEngine.process("reset-password-email", context);

            // Tạo và gửi email với nội dung HTML
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(email);
            messageHelper.setSubject("Your Verification Code");
            messageHelper.setText(htmlContent, true); // true để gửi email dưới dạng HTML

            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    };



    public boolean verifyCode(String email, String code) {
        List<VerificationCode> verificationCodes =
                verificationCodeRepository.findAllByEmail(email).orElse(null);

        return verificationCodes.stream()
                .anyMatch(verificationCode ->
                        verificationCode.getCode().equals(code) &&
                                verificationCode.getExpirationTime().isAfter(LocalDateTime.now()) // Kiểm tra mã còn hiệu lực
                );
    }

}



