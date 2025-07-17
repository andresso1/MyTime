package com.MyTime.service;

import com.MyTime.dto.ReportDto;
import com.MyTime.entity.User;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @Mock
    private CsvService csvService;

    @InjectMocks
    private ReportService reportService;

    private User user;
    private ReportDto reportDtoCsv;
    private ReportDto reportDtoPdf;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setName("Test User");

        reportDtoCsv = new ReportDto();
        reportDtoCsv.setUserId(1);
        reportDtoCsv.setCompanyId(1L);
        reportDtoCsv.setYear(2023L);
        reportDtoCsv.setMonth(1L);
        reportDtoCsv.setFormat("CSV");

        reportDtoPdf = new ReportDto();
        reportDtoPdf.setUserId(1);
        reportDtoPdf.setCompanyId(1L);
        reportDtoPdf.setYear(2023L);
        reportDtoPdf.setMonth(1L);
        reportDtoPdf.setFormat("PDF");
    }

    @Test
    void sendEmailByReport_CsvFormat_Success() throws MessagingException, IOException, JRException, SQLException {
        byte[] csvData = "csv content".getBytes();

        when(userService.getById(1)).thenReturn(user);
        when(csvService.createCsvFile(anyLong(), anyLong(), anyLong())).thenReturn(csvData);
        doNothing().when(emailService).sendEmailWithAttachment(anyString(), anyString(), anyString(), any(byte[].class), anyString());

        reportService.sendEmailByReport(reportDtoCsv);

        verify(userService, times(1)).getById(1);
        verify(csvService, times(1)).createCsvFile(1L, 2023L, 1L);
        verify(emailService, times(1)).sendEmailWithAttachment("test@example.com", "Attached: Report CSV - myTime", "Hello, here is your report.", csvData, "report.CSV");
    }

    @Test
    void sendEmailByReport_PdfFormat_Success() throws MessagingException, IOException, JRException, SQLException {
        byte[] pdfData = "pdf content".getBytes();

        when(userService.getById(1)).thenReturn(user);
        when(csvService.createPDFFile(anyLong(), anyLong(), anyLong())).thenReturn(pdfData);
        doNothing().when(emailService).sendEmailWithAttachment(anyString(), anyString(), anyString(), any(byte[].class), anyString());

        reportService.sendEmailByReport(reportDtoPdf);

        verify(userService, times(1)).getById(1);
        verify(csvService, times(1)).createPDFFile(1L, 2023L, 1L);
        verify(emailService, times(1)).sendEmailWithAttachment("test@example.com", "Attached: Report PDF - myTime", "Hello, here is your report.", pdfData, "report.PDF");
    }

    @Test
    void sendEmailByReport_CsvFormat_MessagingException() throws MessagingException, IOException, JRException, SQLException {
        byte[] csvData = "csv content".getBytes();

        when(userService.getById(1)).thenReturn(user);
        when(csvService.createCsvFile(anyLong(), anyLong(), anyLong())).thenReturn(csvData);
        doThrow(new MessagingException("Email error")).when(emailService).sendEmailWithAttachment(anyString(), anyString(), anyString(), any(byte[].class), anyString());

        reportService.sendEmailByReport(reportDtoCsv);

        // Verify that the exception is caught and handled (no re-throw)
        verify(userService, times(1)).getById(1);
        verify(csvService, times(1)).createCsvFile(1L, 2023L, 1L);
        verify(emailService, times(1)).sendEmailWithAttachment(anyString(), anyString(), anyString(), any(byte[].class), anyString());
    }

    @Test
    void sendEmailByReport_PdfFormat_JRException() throws MessagingException, IOException, JRException, SQLException {
        when(userService.getById(1)).thenReturn(user);
        doThrow(new JRException("JasperReports error")).when(csvService).createPDFFile(anyLong(), anyLong(), anyLong());

        assertThrows(RuntimeException.class, () -> reportService.sendEmailByReport(reportDtoPdf));

        verify(userService, times(1)).getById(1);
        verify(csvService, times(1)).createPDFFile(1L, 2023L, 1L);
        verify(emailService, never()).sendEmailWithAttachment(anyString(), anyString(), anyString(), any(byte[].class), anyString());
    }

    @Test
    void sendEmailByReport_PdfFormat_SQLException() throws MessagingException, IOException, JRException, SQLException {
        when(userService.getById(1)).thenReturn(user);
        doThrow(new SQLException("SQL error")).when(csvService).createPDFFile(anyLong(), anyLong(), anyLong());

        assertThrows(RuntimeException.class, () -> reportService.sendEmailByReport(reportDtoPdf));

        verify(userService, times(1)).getById(1);
        verify(csvService, times(1)).createPDFFile(1L, 2023L, 1L);
        verify(emailService, never()).sendEmailWithAttachment(anyString(), anyString(), anyString(), any(byte[].class), anyString());
    }
}
