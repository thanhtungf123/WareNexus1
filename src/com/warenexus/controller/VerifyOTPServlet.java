package com.warenexus.controller;

import com.warenexus.dao.ContractDAO;
import com.warenexus.dao.CustomerDAO;
import com.warenexus.dao.RentalOrderDAO;
import com.warenexus.model.Account;
import com.warenexus.model.Contract;
import com.warenexus.model.Customer;
import com.warenexus.model.RentalOrder;
import com.warenexus.util.PDFGenerator;
import com.warenexus.util.SignatureUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Properties;

@WebServlet("/verifyOTP")
public class VerifyOTPServlet extends HttpServlet {
    private final RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
    private final ContractDAO contractDAO = new ContractDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String otpEntered = request.getParameter("otp");
        HttpSession session = request.getSession();

        // Lấy OTP từ session
        String otpStored = (String) session.getAttribute("otp");
        long otpExpiration = (long) session.getAttribute("otpExpiration");

        if (otpStored == null || System.currentTimeMillis() > otpExpiration) {
            // OTP đã hết hạn
            request.setAttribute("errorMessage", "Mã OTP đã hết hạn.");
            request.getRequestDispatcher("enterOtp.jsp").forward(request, response);
            return;
        }

        // Kiểm tra OTP
        if (otpEntered != null && otpEntered.equals(otpStored)) {
            // OTP hợp lệ, tạo PDF hợp đồng và gửi email
            try {
                // Lấy rentalOrderId từ request
                Integer rentalOrderId = Integer.parseInt(request.getParameter("rentalOrderId"));
                if (rentalOrderId == null) {
                    request.setAttribute("errorMessage", "Mã đơn thuê không hợp lệ.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }

                // Lấy chữ ký từ session
                String signatureImage = (String) session.getAttribute("signatureImage");
                String signatureFilePath = "";
                if (signatureImage != null && !signatureImage.isEmpty()) {
                    // Lưu chữ ký Base64 thành tệp hình ảnh
                    signatureFilePath = "C:/contracts/signature_" + rentalOrderId + ".png"; // Thư mục bạn muốn lưu chữ ký
                    SignatureUtils.saveBase64Signature(signatureImage, signatureFilePath);
                }

                // Lấy email người dùng từ session
                Account acc = (Account) session.getAttribute("acc");
                if (acc == null) {
                    request.setAttribute("errorMessage", "Bạn chưa đăng nhập.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }
                CustomerDAO cdao = new CustomerDAO();
                Customer customer = cdao.getByAccountId(acc.getAccountId());
                String userEmail = acc.getEmail(); // Lấy email từ đối tượng Account trong session

                // Tạo hợp đồng PDF và gửi email
                createAndSendContract(request, response, rentalOrderId, signatureFilePath, userEmail, customer);

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Lỗi khi tạo hợp đồng.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else {
            // OTP sai
            request.setAttribute("errorMessage", "Mã OTP không đúng.");
            request.getRequestDispatcher("enterOtp.jsp").forward(request, response);
        }
    }

    // Phương thức tạo hợp đồng PDF và gửi email
    private void createAndSendContract(HttpServletRequest request, HttpServletResponse response, Integer rentalOrderId, String signatureFilePath, String userEmail, Customer customer) throws Exception {
        RentalOrder rentalOrder = rentalOrderDAO.getRentalOrderById(rentalOrderId);
        // Kiểm tra xem rentalOrder có hợp lệ không
        if (rentalOrder == null) {
            throw new Exception("Không tìm thấy RentalOrder với ID: " + rentalOrderId);
        }
        // Tạo hợp đồng PDF
        String pdfFilePath = PDFGenerator.createPDFContract(rentalOrderId, signatureFilePath, userEmail, customer, rentalOrder);

        // Gửi hợp đồng qua email
        sendContractEmail(userEmail, pdfFilePath, rentalOrderId);

        // Cập nhật trạng thái hợp đồng trong DB là "Đã ký hợp đồng"
        updateContractStatus(rentalOrderId, rentalOrder);

        // Chuyển hướng đến trang thành công
        response.sendRedirect("contractSuccess.jsp");
    }

    // Phương thức gửi email với hợp đồng PDF đính kèm
    private void sendContractEmail(String userEmail, String pdfFilePath, Integer rentalOrderId) throws MessagingException {
        // Sử dụng JavaMail để gửi email với hợp đồng đính kèm
        String senderEmail = "tienloveu123@gmail.com"; // Email của bạn
        String senderPassword = "vgfn whay lrck iyfg"; // Mật khẩu ứng dụng Gmail
        String host = "smtp.gmail.com"; // Host cho SMTP Gmail

        // Cấu hình thư viện JavaMail
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Tạo phiên làm việc JavaMail
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        // Tạo đối tượng email
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
        message.setSubject("Hợp đồng đã ký - WareNexus");

        // Tạo phần nội dung email
        String emailContent = "Chào bạn,\n\nHợp đồng thuê kho của bạn đã được ký. Vui lòng xem hợp đồng đính kèm.\n\nBest regards,\nWareNexus Team";
        message.setText(emailContent);

        // Đính kèm hợp đồng PDF
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(pdfFilePath);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("contract_" + rentalOrderId + ".pdf"); // Sử dụng rentalOrderId để tạo tên tệp hợp đồng

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // Gửi email
        message.setContent(multipart);
        Transport.send(message);
    }

    // Phương thức cập nhật trạng thái hợp đồng trong DB
    private void updateContractStatus(Integer rentalOrderId, RentalOrder rentalOrder) throws Exception {
        // Giả sử bạn có DAO để lưu hợp đồng vào DB
        rentalOrderDAO.updateStatus(rentalOrderId, "Pending");

        // Tạo hợp đồng mới
        Contract contract = new Contract();
        contract.setRentalOrderID(rentalOrderId);
        contract.setContractNumber("CONTRACT_" + rentalOrderId);  // Tạo số hợp đồng từ rentalOrderId
        contract.setPdfPath("C:/contracts/contract_" + rentalOrderId + ".pdf");  // Lưu đường dẫn đến file PDF
        contract.setStatus("Signed");  // Trạng thái hợp đồng là "Đã ký"

        // Thêm các thông tin mới vào hợp đồng
        contract.setPrice(rentalOrder.getTotalPrice());  // Lấy giá tiền từ rentalOrder
        contract.setDeposit(rentalOrder.getDeposit());  // Lấy tiền đặt cọc từ rentalOrder
        contract.setRentalPeriod(getRentalPeriod(rentalOrder));  // Lấy thời gian thuê từ rentalOrder

        // Lưu hợp đồng vào DB
        contractDAO.createContract(contract);
    }

    // Phương thức tính thời gian thuê (theo số ngày hoặc số tháng)
    private int getRentalPeriod(RentalOrder rentalOrder) {
        long diffInMillis = rentalOrder.getEndDate().getTime() - rentalOrder.getStartDate().getTime();
        long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);  // Chuyển đổi từ milliseconds sang ngày
        return (int) diffInDays;  // Trả về số ngày thuê
    }


}

