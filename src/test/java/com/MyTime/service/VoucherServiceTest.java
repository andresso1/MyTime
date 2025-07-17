package com.MyTime.service;

import com.MyTime.entity.Voucher;
import com.MyTime.repository.VoucherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VoucherServiceTest {

    @Mock
    private VoucherRepository voucherRepository;

    @InjectMocks
    private VoucherService voucherService;

    private Voucher voucher1;
    private Voucher voucher2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        voucher1 = new Voucher();
        voucher1.setVoucherId(1L);
        voucher1.setStatus("ACTIVE");

        voucher2 = new Voucher();
        voucher2.setVoucherId(2L);
        voucher2.setStatus("INACTIVE");
    }

    @Test
    void findByVoucherId_Found() {
        when(voucherRepository.findByVoucherId(1L)).thenReturn(Optional.of(voucher1));

        Optional<Voucher> foundVoucher = voucherService.findByVoucherId(1L);

        assertTrue(foundVoucher.isPresent());
        assertEquals(1L, foundVoucher.get().getVoucherId());
    }

    @Test
    void findByVoucherId_NotFound() {
        when(voucherRepository.findByVoucherId(3L)).thenReturn(Optional.empty());

        Optional<Voucher> foundVoucher = voucherService.findByVoucherId(3L);

        assertFalse(foundVoucher.isPresent());
    }

    @Test
    void getByCompanyIdAndStatusIn() {
        List<String> statusList = Arrays.asList("ACTIVE", "INACTIVE");
        when(voucherRepository.getByCompanyIdAndStatusIn(1L, statusList)).thenReturn(Arrays.asList(voucher1, voucher2));

        List<Voucher> vouchers = voucherService.getByCompanyIdAndStatusIn(1L, statusList);

        assertNotNull(vouchers);
        assertEquals(2, vouchers.size());
        assertEquals("ACTIVE", vouchers.get(0).getStatus());
    }
}
