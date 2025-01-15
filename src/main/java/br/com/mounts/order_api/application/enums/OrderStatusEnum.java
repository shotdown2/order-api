package br.com.mounts.order_api.application.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum for order status
 *
 * @author Raphael Braga
 */
@AllArgsConstructor
@Getter
public enum OrderStatusEnum {
    PROCESSADO ("Pedido processado com sucesso"),
    DUPLICADO ("Pedido duplicado"),
    INVALIDO("Pedido invalido");


    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static OrderStatusEnum fromValue(String value) {
        for (OrderStatusEnum b : OrderStatusEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

}
