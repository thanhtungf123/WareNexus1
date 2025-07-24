package com.warenexus.util;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.borders.Border;
import com.warenexus.model.Customer;
import com.warenexus.model.RentalOrder;


import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFGenerator {

    public static String createPDFContract(Integer rentalOrderId, String signatureImage, String userEmail, Customer customer, RentalOrder rentalOrder, String fontPath) throws Exception {
        // Đường dẫn lưu file PDF
        String pdfDirectory = "C:/contracts/";  // Ví dụ: Thư mục lưu trữ trên Windows
        String pdfFilePath = pdfDirectory + "contract_" + rentalOrderId + ".pdf";

        // Kiểm tra thư mục, nếu không tồn tại thì tạo mới
        File directory = new File(pdfDirectory);
        if (!directory.exists()) {
            directory.mkdirs();  // Tạo thư mục nếu chưa tồn tại
        }

        // Tạo tài liệu PDF với iText 7
        PdfWriter writer = new PdfWriter(pdfFilePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        FontProgram fontProgram = FontProgramFactory.createFont(fontPath);
        PdfFont font = PdfFontFactory.createFont(fontProgram, "Identity-H");

        // Tiêu đề hợp đồng ở giữa
        Paragraph title = new Paragraph("Hợp Đồng Thuê Kho")
                .setFont(font)
                .setFontSize(19)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));

        // Thêm nội dung hợp đồng
        document.add(new Paragraph("Ngày ký: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date())).setFont(font));
        document.add(new Paragraph("Mã đơn thuê: " + rentalOrderId).setFont(font));
        // Tính toán thời gian thuê (Rental Period)
        long diffInMillis = rentalOrder.getEndDate().getTime() - rentalOrder.getStartDate().getTime();
        long rentalPeriod = diffInMillis / (1000 * 60 * 60 * 24); // Chuyển đổi từ mili giây sang ngày
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");  // Định dạng số với dấu phẩy phân cách

        // Thêm thông tin giá tiền, tiền đặt cọc, thời gian thuê
        document.add(new Paragraph("Giá tiền: " + decimalFormat.format(rentalOrder.getTotalPrice()) + " VND").setFont(font));
        document.add(new Paragraph("Tiền đặt cọc: " + decimalFormat.format(rentalOrder.getDeposit()) + " VND").setFont(font));
        document.add(new Paragraph("Thời gian thuê: " + rentalPeriod + " ngày").setFont(font));

        // Thêm đường viền
        SolidLine solidLine = new SolidLine();
        solidLine.setLineWidth(1f); // Độ dày của đường viền
        solidLine.setColor(ColorConstants.BLACK); // Màu đường viền

        LineSeparator lineSeparator = new LineSeparator(solidLine);
        lineSeparator.setWidth(UnitValue.createPercentValue(100));

        document.add(new Paragraph("\n")); // Đoạn văn trống nếu cần thêm một khoảng trống
        document.add(lineSeparator);

        document.add(new Paragraph("Thông Tin Các Bên").setFont(font).setFontSize(15));
        // Thông tin bên A
        document.add(new Paragraph("Bên A (Khách hàng):").setFont(font).setFontSize(12).setBold());
        document.add(new Paragraph("Họ và tên: " + customer.getFullName()).setFont(font)); // Lấy tên
        document.add(new Paragraph("Email: " + customer.getEmail()).setFont(font)); // Lấy email
        document.add(new Paragraph("Số điện thoại: " + customer.getPhone()).setFont(font)); // Lấy số điện thoại

        // Thông tin bên B
        document.add(new Paragraph("Bên B (WareNexus):").setFont(font).setFontSize(12).setBold());
        document.add(new Paragraph("Đại diện hệ thống cho thuê kho").setFont(font));
        document.add(new Paragraph("Website: www.warenexus.com").setFont(font));
        document.add(new Paragraph("Email: support@warenexus.com").setFont(font));

        document.add(new Paragraph("\n"));
        document.add(lineSeparator);

        // Điều khoản thỏa thuận
        document.add(new Paragraph("Điều Khoản Thỏa Thuận").setFont(font).setFontSize(15));
        document.add(new Paragraph("Bên A đồng ý thuê kho của Bên B theo nội dung và điều kiện trong đơn thuê đã xác nhận.").setFont(font));
        document.add(new Paragraph("Giá thuê, thời hạn thuê, diện tích, đặt cọc và các chi phí liên quan đã được hai bên thống nhất.").setFont(font));
        document.add(new Paragraph("Bên A cam kết thanh toán đúng hạn và sử dụng kho đúng mục đích.").setFont(font));
        document.add(new Paragraph("Bên B cam kết cung cấp kho đúng chất lượng, hỗ trợ kỹ thuật và an ninh trong suốt thời gian thuê.").setFont(font));
        document.add(new Paragraph("Hợp đồng có hiệu lực kể từ thời điểm ký và sau khi Bên A hoàn tất thanh toán.").setFont(font));

        document.add(new Paragraph("\n"));

        // Tạo bảng cho chữ ký bên A và bên B
        Table signatureTable = new Table(2);
        signatureTable.setWidth(UnitValue.createPercentValue(100)); // Đặt độ rộng của bảng là 100%
        signatureTable.setHeight(100);
        signatureTable.setBorder(Border.NO_BORDER);

        // Chữ ký Bên A (Thêm hình ảnh chữ ký, căn trái)
        Cell cellA = new Cell().add(new Paragraph("Chữ ký Bên A").setFont(font).setBold().setTextAlignment(TextAlignment.LEFT));
        if (signatureImage != null && !signatureImage.isEmpty()) {
            // Chèn hình ảnh chữ ký của Bên A
            Image img = new Image(ImageDataFactory.create(signatureImage));
            img.setHeight(50); // Đặt chiều cao của hình ảnh chữ ký
            img.setWidth(200); // Đặt chiều rộng của hình ảnh chữ ký (điều chỉnh tùy theo kích thước gốc)
            img.setFixedPosition(40, 65);  // Vị trí chữ ký của Bên A
            img.setAutoScale(true);
            cellA.add(img);
        } else {
            cellA.add(new Paragraph(customer.getFullName()).setFont(font).setTextAlignment(TextAlignment.LEFT));
        }

        // Chữ ký Bên B (Thêm chữ cho chữ ký Bên B, căn phải)
        Cell cellB = new Cell().add(new Paragraph("Chữ ký Bên B").setFont(font).setBold().setTextAlignment(TextAlignment.RIGHT));
        cellB.add(new Paragraph("Đại diện hệ thống của WareNexus").setFont(font).setTextAlignment(TextAlignment.RIGHT));

        // Thêm các ô vào bảng
        signatureTable.addCell(cellA);
        signatureTable.addCell(cellB);

        document.add(signatureTable); // Thêm bảng chữ ký vào tài liệu

        // Đóng tài liệu PDF
        document.close();

        return pdfFilePath;  // Trả về đường dẫn nơi file PDF đã lưu
    }
}