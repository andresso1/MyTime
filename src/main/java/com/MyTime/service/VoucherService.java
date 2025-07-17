package com.MyTime.service;

import com.MyTime.entity.Voucher;
import com.MyTime.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {

    @Autowired
    VoucherRepository voucherRepository;
    Optional<Voucher> findByVoucherId(long id){
        return voucherRepository.findByVoucherId(id);
    }
    public List<Voucher> getByCompanyIdAndStatusIn(long id, List<String> statusList) {
        return voucherRepository.getByCompanyIdAndStatusIn(id, statusList);
    }
}
