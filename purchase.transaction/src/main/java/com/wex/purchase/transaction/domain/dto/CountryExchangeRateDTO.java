package com.wex.purchase.transaction.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryExchangeRateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2483668954893649966L;

    @JsonProperty("record_date")
    private Date recordDate;

    private String country;

    private String currency;

    @JsonProperty("country_currency_desc")
    private String countryCurrencyDescription;

    @JsonProperty("exchange_rate")
    private Double exchangeRate;

    @JsonProperty("effective_date")
    private String effectiveDate;

    @JsonProperty("src_line_nbr")
    private String sourceLineNumber;

    @JsonProperty("record_fiscal_year")
    private String fiscalYear;

    @JsonProperty("record_fiscal_quarter")
    private String fiscalQuarterNumber;

    @JsonProperty("record_calendar_year")
    private String calendarYear;

    @JsonProperty("record_calendar_quarter")
    private String calendarQuarterNumber;

    @JsonProperty("record_calendar_month")
    private String calendarMonthNumber;

    @JsonProperty("record_calendar_day")
    private String calendarDayNumber;
}
