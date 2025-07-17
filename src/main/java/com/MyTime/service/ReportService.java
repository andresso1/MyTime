package com.MyTime.service;

import com.MyTime.dto.ReportDto;
import com.MyTime.entity.User;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

@Service
@Transactional
public class ReportService {
    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;
    @Autowired
    CsvService csvService;

    public void sendEmailByReport(@Valid ReportDto reportDto) throws MessagingException, IOException {
        // Lógica de tu proceso batch
        // Llama al servicio para enviar el correo electrónico con el archivo adjunto
        User userInfo = userService.getById(reportDto.getUserId());
        String subject = "";
        byte[] fileData = new byte[0];
        String reportFileName = "";
        try {
            String recipientEmail = userInfo.getEmail();
            if(reportDto.getFormat().contains("CSV")){
                subject = "Attached: Report CSV - myTime";
                fileData = csvService.createCsvFile(reportDto.getCompanyId(),
                        reportDto.getYear(),reportDto.getMonth());
                reportFileName = "report.CSV";
            }
            if(reportDto.getFormat().contains("PDF")){
                subject = "Attached: Report PDF - myTime";
                fileData = csvService.createPDFFile(reportDto.getCompanyId(),
                        reportDto.getYear(),reportDto.getMonth());
                reportFileName = "report.PDF";
            }

            String body = "Hello, here is your report.";
            // Lee el archivo CSV y conviértelo a un array de bytes

            // Llama al servicio para enviar el correo electrónico con el archivo adjunto
            emailService.sendEmailWithAttachment(recipientEmail, subject, body, fileData, reportFileName);
            //return "Correo electrónico enviado con éxito.";
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            //return "Error al enviar el correo electrónico.";
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Proceso batch ejecutado con éxito en el hilo: " + Thread.currentThread().getName());
    }
}
