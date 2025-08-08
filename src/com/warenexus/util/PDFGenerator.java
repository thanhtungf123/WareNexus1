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

import com.warenexus.dao.WarehouseDAO;
import com.warenexus.model.Customer;
import com.warenexus.model.RentalOrder;
import com.warenexus.model.Warehouse;

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
        
         String warehouseName = "—";
        try {
            WarehouseDAO wdao = new WarehouseDAO();
            Warehouse wh = wdao.getById(rentalOrder.getWarehouseID());
            if (wh != null && wh.getName() != null) warehouseName = wh.getName();
        } catch (Exception ignore) {}
        document.add(new Paragraph("Tên kho: " + warehouseName).setFont(font)); // ✅ thêm tên kho
        
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
        document.add(new Paragraph("Bên A (WareNexus):").setFont(font).setFontSize(12).setBold());
        document.add(new Paragraph("Đại diện hệ thống cho thuê kho").setFont(font));
        document.add(new Paragraph("Website: www.warenexus.com").setFont(font));
        document.add(new Paragraph("Email: tungptde170779@fpt.edu.vn").setFont(font));
        
        // Thông tin bên B
        document.add(new Paragraph("Bên B (Khách hàng):").setFont(font).setFontSize(12).setBold());
        document.add(new Paragraph("Họ và tên: " + customer.getFullName()).setFont(font)); // Lấy tên
        document.add(new Paragraph("Email: " + customer.getEmail()).setFont(font)); // Lấy email
        document.add(new Paragraph("Số điện thoại: " + customer.getPhone()).setFont(font)); // Lấy số điện thoại

        document.add(new Paragraph("\n"));
        document.add(lineSeparator);

        // Điều khoản thỏa thuận
        document.add(new Paragraph("CÁC ĐIỀU KHOẢN VÀ THỎA THUẬN").setFont(font).setBold());
        document.add(new Paragraph("Điều I:").setFont(font).setBold());
        document.add(new Paragraph("Bên B đồng ý thuê kho của Bên A theo nội dung và điều kiện trong đơn thuê đã xác nhận.")
                .setFont(font));

        // Điều II
        document.add(new Paragraph("\nĐiều II: Thời hạn cho thuê kho").setFont(font).setBold());
        document.add(new Paragraph(" - Thời hạn cho thuê là " + rentalPeriod + " ngày").setFont(font));
        document.add(new Paragraph((" - Bắt đầu từ ngày " + rentalOrder.getStartDate())
                + " đến " + rentalOrder.getEndDate()).setFont(font));

        // Điều III
        document.add(new Paragraph("\nĐiều III: Giá cả và phương thức thanh toán").setFont(font).setBold());
        document.add(new Paragraph("1. Giá trị hợp đồng: Tiền đặt cọc:" + " " + decimalFormat.format(rentalOrder.getDeposit()) + " " + "( tương ứng với 20% giá trị hợp đồng)," 
                + " ngay sau khi hợp đồng này ký kết bên B chuyển tổng tiền thuê kho cho bên A, chậm nhất trong thời gian 14 ngày ( không kể ngày nghỉ)," 
                + "để đảm bảo thực hiện hợp đồng. Số tiền cọc bên A sẽ hoàn trả lại cho bên B sau khi kết thúc hợp đồng.")
                .setFont(font));
        document.add(new Paragraph("                              - Tiền cọc: " + decimalFormat.format(rentalOrder.getDeposit()) + "=" + decimalFormat.format(rentalOrder.getTotalPrice()) + " " + "*" + " " + "20%" + " " + " VND.").setFont(font));
        document.add(new Paragraph("                              - Tiền tổng: " + decimalFormat.format(rentalOrder.getTotalPrice()) + " VND.").setFont(font));
        document.add(new Paragraph("2. Phương thức thanh toán: bên B sẽ thanh toán cho bên A tiền thuê nhà 01 lần, sau khi ký kết hợp đồng .")
                .setFont(font));

        // Điều IV
        document.add(new Paragraph("\nĐiều IV: Trách nhiệm của bên cho thuê kho").setFont(font).setBold());
        document.add(new Paragraph("1. Tiến hành bàn giao đầy đủ kho, trang thiết bị và các dịch vụ đi kèm (nếu có) cho bên thuê.")
                .setFont(font));
        document.add(new Paragraph("2. Cung cấp các giấy tờ có liên đến kho bãi cho thuê cho cơ quan nhà nước giúp bên thuê khi cần thiết.")
                .setFont(font));
        document.add(new Paragraph("3. Đảm bảo quyền sử dụng trọn vẹn của bên thuê đối với diện tích ghi trong hợp đồng.")
                .setFont(font));

        // Điều V
        document.add(new Paragraph("\nĐiều V: Trách nhiệm của bên thuê kho").setFont(font).setBold());
        document.add(new Paragraph("1. Trả tổng tiền thuê kho theo đúng thời hạn đã quy định ( 14 ngày bắt đầu từ ngày thuê).").setFont(font));
        document.add(new Paragraph("2. Sử dụng kho theo đúng mục đích").setFont(font));
        document.add(new Paragraph("3. Chấp hành các quy định về giữ gìn vệ sinh môi trường và các quy định về trật tự an ninh chung.")
                .setFont(font));
        document.add(new Paragraph("4. Không được cải tạo sửa chữa kho khi chưa có sự đồng ý của bên cho thuê.")
                .setFont(font));
        document.add(new Paragraph("5. Không được chuyển nhượng hợp đồng thuê và cho thuê lại khi không có sự đồng ý của bên cho thuê.")
                .setFont(font));
        document.add(new Paragraph("6. Trường hợp chấm dứt hợp đồng trước thời hạn đã thỏa thuận phải báo trước cho bên A ngay ít nhất 30 ngày. "
                + "Nếu muốn thuê tiếp bên B phải thỏa thuận trước với bên A.").setFont(font));

        // Điều VI
        document.add(new Paragraph("\nĐiều VI: Hai bên cam kết").setFont(font).setBold());
        document.add(new Paragraph("1. Thực hiện đúng các điều khoản ghi trong hợp đồng. Trường hợp có tranh chấp hoặc một trong hai bên vi phạm hợp đồng "
                + "thì cùng nhau bàn bạc thống nhất trên tinh thần đoàn kết. Nếu không thỏa mãn thì yêu cầu cơ quan có thẩm quyền giải quyết.")
                .setFont(font));
        document.add(new Paragraph("2. Chấm dứt hợp đồng trong các điều kiện sau:").setFont(font));
        document.add(new Paragraph("- Hết thời hạn hợp đồng.").setFont(font));
        document.add(new Paragraph("- Hai bên thỏa thuận chấm dứt hợp đồng trước thời hạn.").setFont(font));
        document.add(new Paragraph("- Nếu bên A không thanh toán toàn bộ số tiền tổng theo đúng thời hạn quy định thì bên B sẽ chấm dứt hợp đồng thuê, "
                + "tiền cọc sẽ không được hoàn lại cho bên A.").setFont(font));
        
        // Điều VII
        document.add(new Paragraph("\nĐiều VII: Hiệu lực của hợp đồng").setFont(font).setBold());
        document.add(new Paragraph("1. Hợp đồng này có hiệu lực kể từ ngày ký cho đến khi hết hạn hợp đồng.").setFont(font));
        document.add(new Paragraph("2. Hợp đồng được lập thành 02 bản có giá trị như nhau. Bên A sẽ giữ 01 bản, bên B giữ 01 bản.")
                .setFont(font));
        document.add(new Paragraph("\n"));

        // Tạo bảng cho chữ ký bên A và bên B
        Table signatureTable = new Table(2);
        signatureTable.setWidth(UnitValue.createPercentValue(100)); // Đặt độ rộng của bảng là 100%
        signatureTable.setHeight(100);
        signatureTable.setBorder(Border.NO_BORDER);

        // Chữ ký Bên A (Thêm hình ảnh chữ ký, căn trái)
        Cell cellB = new Cell().add(new Paragraph("Chữ ký Bên B").setFont(font).setBold().setTextAlignment(TextAlignment.LEFT));
        if (signatureImage != null && !signatureImage.isEmpty()) {
            // Chèn hình ảnh chữ ký của Bên B
            Image img = new Image(ImageDataFactory.create(signatureImage));
            img.setHeight(50); // Đặt chiều cao của hình ảnh chữ ký
            img.setWidth(200); // Đặt chiều rộng của hình ảnh chữ ký (điều chỉnh tùy theo kích thước gốc)
            img.setFixedPosition(40, 65);  // Vị trí chữ ký của Bên B
            img.setAutoScale(true);
            cellB.add(img);
        } else {
            cellB.add(new Paragraph(customer.getFullName()).setFont(font).setTextAlignment(TextAlignment.LEFT));
        }

        // Chữ ký Bên B (Thêm chữ cho chữ ký Bên B, căn phải)
        Cell cellA = new Cell().add(new Paragraph("Chữ ký Bên A").setFont(font).setBold().setTextAlignment(TextAlignment.RIGHT));
        cellA.add(new Paragraph("Đại diện hệ thống của WareNexus").setFont(font).setTextAlignment(TextAlignment.RIGHT));

        // Thêm các ô vào bảng
        signatureTable.addCell(cellB);
        signatureTable.addCell(cellA);

        document.add(signatureTable); // Thêm bảng chữ ký vào tài liệu

        // Đóng tài liệu PDF
        document.close();

        return pdfFilePath;  // Trả về đường dẫn nơi file PDF đã lưu
    }
}