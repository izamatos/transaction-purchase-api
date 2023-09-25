package com.wex.purchase.transaction.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RatesOfExchangeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8910265831649917398L;

    private List<CountryExchangeRateDTO> data;
}
