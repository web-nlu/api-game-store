package vn.edu.hcmaf.apigamestore.sale_report.dto;

import lombok.Data;

@Data
public class StatisticFilterRequest {
  long startDate;
  long endDate;
  String type;
}
