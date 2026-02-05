package com.fooddelivery.orders.mapper;

import com.fooddelivery.orders.dto.PaymentRequestDto;
import com.fooddelivery.orders.dto.PaymentResponseDto;
import com.fooddelivery.orders.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {OrderMapper.class})
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order", qualifiedByName = "orderToId")
    PaymentResponseDto paymentToPaymentResponseDto(Payment payment);

    @Named("orderToId")
    default Long orderToId(com.fooddelivery.orders.entity.Order order) {
        return order != null ? order.getId() : null;
    }

    @Mapping(target = "order", ignore = true)
    @Mapping(target="id",ignore = true)
    @Mapping(target="amount",ignore = true)
    @Mapping(target="status",ignore = true)
    Payment paymentRequestDtoToPayment(PaymentRequestDto paymentRequestDto);

    default Page<PaymentResponseDto> pagePaymentToPagePaymentDto(Page<Payment> page) {
        return page.map(this::paymentToPaymentResponseDto);
    }
}
