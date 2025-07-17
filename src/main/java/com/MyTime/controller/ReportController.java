package com.MyTime.controller;

import com.MyTime.dto.Message;
import com.MyTime.dto.ReportDto;
import com.MyTime.entity.Voucher;
import com.MyTime.enums.TimeStatus;
import com.MyTime.enums.VoucherStatus;
import com.MyTime.service.CompanyService;
import com.MyTime.service.ReportService;
import com.MyTime.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "*")
public class ReportController {
    @Autowired
    ReportService reportService;
    @Autowired
    CompanyService companyService;
    @Autowired
    VoucherService voucherService;
    @PostMapping("/sendReportCSV")
    public ResponseEntity<?> sendReportCSV(@Valid @RequestBody ReportDto reportDto, BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors())
            return new ResponseEntity(new Message("error in fields"), HttpStatus.BAD_REQUEST);

        Runnable procesoRunnable = () -> runReportProcess(reportDto);

        // Iniciar un nuevo hilo para ejecutar el proceso
        Thread procesoThread = new Thread(procesoRunnable);
        procesoThread.start();

        // Responder inmediatamente con un HTTP OK y un mensaje
        return new ResponseEntity(new Message("Report requested"), HttpStatus.OK);
    }

    private void runReportProcess(ReportDto reportDto) {
        try {
            sendEmailReportExport(reportDto);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Enviar correo al usuario (puedes implementar el envío de correo aquí)
        System.out.println("Proceso completado. Enviar correo al usuario.");
    }

    public void sendEmailReportExport(@Valid ReportDto reportDto) throws MessagingException, IOException {

        try {
            reportService.sendEmailByReport(reportDto);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Proceso batch ejecutado con éxito en el hilo: " + Thread.currentThread().getName());
    }

    @GetMapping("/voucherList/{id}")
    public ResponseEntity<List<Voucher>> getVoucherListByCompanyId(@PathVariable long id) {
        List<String> statusList = Arrays.asList(String.valueOf(VoucherStatus.PAID), String.valueOf(VoucherStatus.GENERATED));

        if (!companyService.existsById((int) id))
            return new ResponseEntity(new Message("company not exist"), HttpStatus.NOT_FOUND);
        List<Voucher> vouchers = voucherService.getByCompanyIdAndStatusIn( id ,statusList );

        return new ResponseEntity(vouchers, HttpStatus.OK);
    }
}
