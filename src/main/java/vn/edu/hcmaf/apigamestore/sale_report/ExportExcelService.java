package vn.edu.hcmaf.apigamestore.sale_report;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.sale_report.dto.RevenueProjection;

import java.io.IOException;
import java.util.List;
@Service
public class ExportExcelService {


    public void exportRevenueToExcel(List<RevenueProjection> revenues, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Revenue Report");

        // Tạo header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Label", "Date", "Epoch", "Total Revenue"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            // Có thể format font đậm, màu sắc ở đây nếu muốn
        }

        // Tạo dữ liệu từng dòng
        int rowNum = 1;
        for (RevenueProjection revenue : revenues) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(revenue.getLabel());
//            row.createCell(1).setCellValue(revenue.getTime().toString());
//            row.createCell(2).setCellValue(revenue.getEpoch());
            row.createCell(1).setCellValue(revenue.getTotalRevenue().toString());
        }

        // Tự động điều chỉnh độ rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Set content type & header để trình duyệt biết đây là file Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "Revenue_Report.xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Ghi dữ liệu ra output stream của response
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
