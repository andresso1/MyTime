package com.MyTime.repository;

import com.MyTime.entity.Company;
import com.MyTime.entity.Voucher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VoucherRepositoryTest {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Company company1;
    private Voucher voucher1;
    private Voucher voucher2;

    @BeforeEach
    void setUp() {
        company1 = new Company("Test Company", "Address", "12345", "NIT");
        entityManager.persist(company1);

        voucher1 = new Voucher();
        voucher1.setVoucherId(1L);
        voucher1.setStatus("ACTIVE");
        voucher1.setCompanyId(company1.getCompanyId());
        entityManager.persist(voucher1);

        voucher2 = new Voucher();
        voucher2.setVoucherId(2L);
        voucher2.setStatus("INACTIVE");
        voucher2.setCompanyId(company1.getCompanyId());
        entityManager.persist(voucher2);

        entityManager.flush();
    }

    @Test
    void findByVoucherId_Found() {
        Optional<Voucher> foundVoucher = voucherRepository.findByVoucherId(voucher1.getVoucherId());
        assertTrue(foundVoucher.isPresent());
        assertEquals(voucher1.getVoucherId(), foundVoucher.get().getVoucherId());
    }

    @Test
    void findByVoucherId_NotFound() {
        Optional<Voucher> foundVoucher = voucherRepository.findByVoucherId(999L);
        assertFalse(foundVoucher.isPresent());
    }

    @Test
    void getByCompanyIdAndStatusIn() {
        List<String> statusList = Arrays.asList("ACTIVE", "INACTIVE");
        List<Voucher> vouchers = voucherRepository.getByCompanyIdAndStatusIn(company1.getCompanyId(), statusList);

        assertNotNull(vouchers);
        assertEquals(2, vouchers.size());
        assertTrue(vouchers.stream().anyMatch(v -> v.getVoucherId() == voucher1.getVoucherId()));
        assertTrue(vouchers.stream().anyMatch(v -> v.getVoucherId() == voucher2.getVoucherId()));
    }

    @Test
    void saveVoucher() {
        Voucher newVoucher = new Voucher();
        newVoucher.setStatus("PENDING");
        newVoucher.setCompanyId(company1.getCompanyId());

        Voucher savedVoucher = voucherRepository.save(newVoucher);

        assertNotNull(savedVoucher.getVoucherId());
        assertEquals("PENDING", savedVoucher.getStatus());

        Optional<Voucher> found = voucherRepository.findById((int)savedVoucher.getVoucherId());
        assertTrue(found.isPresent());
        assertEquals("PENDING", found.get().getStatus());
    }

    @Test
    void findById() {
        Optional<Voucher> foundVoucher = voucherRepository.findById((int)voucher1.getVoucherId());
        assertTrue(foundVoucher.isPresent());
        assertEquals(voucher1.getVoucherId(), foundVoucher.get().getVoucherId());
    }

    @Test
    void findAllVouchers() {
        List<Voucher> vouchers = voucherRepository.findAll();
        assertNotNull(vouchers);
        assertEquals(2, vouchers.size());
    }

    @Test
    void deleteVoucher() {
        voucherRepository.deleteById((int)voucher1.getVoucherId());
        entityManager.flush();

        Optional<Voucher> found = voucherRepository.findById((int)voucher1.getVoucherId());
        assertFalse(found.isPresent());
    }
}
