package com.fooddelivery.orders.mapper;

import com.fooddelivery.orders.dto.PaymentRequestDto;
import com.fooddelivery.orders.dto.PaymentResponseDto;
import com.fooddelivery.orders.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    @Mapping(target="orderId",source="order.id")
    PaymentResponseDto paymentToPaymentResponseDto(Payment payment);

    @Mapping(target="orderId",source="order.id")
    @Mapping(target = "order", ignore = true)
    @Mapping(target="id",ignore = true)
    @Mapping(target="amount",ignore = true)
    @Mapping(target="status",ignore = true)
    @Mapping(target = "method", source = "method")
    Payment paymentRequestDtoToPayment(PaymentRequestDto paymentRequestDto);

    default Page<PaymentResponseDto> pagePaymentToPagePaymentDto(Page<Payment> page) {
        return page.map(this::paymentToPaymentResponseDto);
    }

}
