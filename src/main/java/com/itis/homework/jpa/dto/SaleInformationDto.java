
package com.itis.homework.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleInformationDto {
    private String dealership;
    private Double sum;
    private Long count;
}
