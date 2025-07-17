package com.MyTime.controller;

import com.MyTime.dto.ReportDto;
import com.MyTime.entity.Voucher;
import com.MyTime.service.CompanyService;
import com.MyTime.service.ReportService;
import com.MyTime.service.VoucherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReportControllerTest {

    @Mock
    private ReportService reportService;
    @Mock
    private CompanyService companyService;
    @Mock
    private VoucherService voucherService;

    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void sendReportCSV() throws IOException {
        ReportDto reportDto = new ReportDto();

        ResponseEntity<?> response = reportController.sendReportCSV(reportDto, mock(BindingResult.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getVoucherListByCompanyId() {
        when(companyService.existsById(1)).thenReturn(true);
        when(voucherService.getByCompanyIdAndStatusIn(anyLong(), any())).thenReturn(Collections.emptyList());

        ResponseEntity<List<Voucher>> response = reportController.getVoucherListByCompanyId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
