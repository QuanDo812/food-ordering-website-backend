package com.quan.controller;

import com.quan.model.Order;
import com.quan.repository.OrderRepository;
import com.quan.service.OrderService;
import com.quan.service.VNPAYService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
public class VNPAYController {
    @Autowired
    private VNPAYService vnPayService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public RedirectView handleVnpayIPN(HttpServletRequest request) throws Exception {
        Map<String, String> params = vnPayService.getParamsFromRequest(request);

        String vnp_ResponseCode = params.get("vnp_ResponseCode"); // Trạng thái giao dịch
        String orderId = params.get("vnp_TxnRef");               // Mã đơn hàng
        String transactionNo = params.get("vnp_TransactionNo");  // Mã giao dịch

        String frontendUrl = "http://localhost:3000/payment-result"
                + "?status=" + vnp_ResponseCode
                + "&orderId=" + params.get("vnp_TxnRef")
                + "&transactionNo=" + params.get("vnp_TransactionNo")
                + "&vpn_Amount=" + params.get("vnp_Amount");


        if ("00".equals(vnp_ResponseCode)) {
            Order order = orderRepository.findById(Long.parseLong(orderId)).get();
            order.setIsPayment(true);
            orderRepository.save(order);
        }
        return new RedirectView(frontendUrl);

    }
}
