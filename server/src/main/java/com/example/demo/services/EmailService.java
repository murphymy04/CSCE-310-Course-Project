package com.example.demo.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.demo.types.BookOrderResponse;
import com.example.demo.types.OrderItem;
import com.example.demo.types.PurchaseType;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendHtmlEmailAsync(String to, String subject, BookOrderResponse order) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, "UTF-8");

            String htmlBody = buildOrderHtml(order);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            helper.setFrom("mylesm2004@gmail.com");

            mailSender.send(msg);

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    private String buildOrderHtml(BookOrderResponse order) {

        StringBuilder sb = new StringBuilder();
        sb.append("<!doctype html>");
        sb.append("<html lang=\"en\">");
        sb.append("<head>");
        sb.append("  <meta charset=\"utf-8\"/>");
        sb.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>");
        sb.append("  <style>");
        // Inline minimal, email-friendly CSS
        sb.append("    body { font-family: Arial, sans-serif; background:#f6f6f6; margin:0; padding:20px; }");
        sb.append("    .card { background:#ffffff; max-width:600px; margin:0 auto; padding:20px; border-radius:8px; box-shadow:0 1px 3px rgba(0,0,0,0.1); }");
        sb.append("    h1 { font-size:20px; margin:0 0 8px 0; }");
        sb.append("    p { margin:4px 0 12px 0; color:#555; }");
        sb.append("    table { width:100%; border-collapse:collapse; }");
        sb.append("    th, td { padding:10px 8px; text-align:left; border-bottom:1px solid #eee; }");
        sb.append("    th { background:#fafafa; font-weight:600; }");
        sb.append("    .price { text-align:right; white-space:nowrap; }");
        sb.append("    .total-row td { font-weight:700; border-top:2px solid #ddd; }");
        sb.append("    .badge-bought { background:#e6ffea; color:#006400; padding:4px 8px; border-radius:12px; font-size:12px; }");
        sb.append("    .badge-rented { background:#fff5e6; color:#8a4b00; padding:4px 8px; border-radius:12px; font-size:12px; }");
        sb.append("    @media (max-width:480px) { .card { padding:12px; } th, td { padding:8px 6px; } }");
        sb.append("  </style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("  <div class=\"card\">");
        sb.append("    <h1>Thank you for your order!</h1>");
        sb.append("    <p>Order ID: <strong>").append(order.getOrderId()).append("</strong></p>");

        sb.append("    <table>");
        sb.append("      <thead>");
        sb.append("        <tr>");
        sb.append("          <th style=\"width:58%\">Item</th>");
        sb.append("          <th style=\"width:22%\">Type</th>");
        sb.append("          <th style=\"width:20%\" class=\"price\">Price</th>");
        sb.append("        </tr>");
        sb.append("      </thead>");
        sb.append("      <tbody>");

        for (OrderItem item : order.getOrderItems()) {
            String badge = PurchaseType.purchase.equals(item.getPurchaseType())
                    ? "<span class=\"badge-bought\">BOUGHT</span>"
                    : "<span class=\"badge-rented\">RENTED</span>";

            String priceStr = "$" + item.getPrice().toString();

            sb.append("        <tr>");
            sb.append("          <td>").append(item.getTitle()).append("</td>");
            sb.append("          <td>").append(badge).append("</td>");
            sb.append("          <td class=\"price\">").append(priceStr).append("</td>");
            sb.append("        </tr>");
        }

        sb.append("        <tr class=\"total-row\">");
        sb.append("          <td></td>");
        sb.append("          <td style=\"text-align:right\">Total</td>");
        sb.append("          <td class=\"price\">").append("$" + order.getTotalPrice().toString()).append("</td>");
        sb.append("        </tr>");

        sb.append("      </tbody>");
        sb.append("    </table>");

        sb.append("    <p style=\"margin-top:18px; color:#666; font-size:13px;\">If you have any questions reply to this email or visit our support page.</p>");
        sb.append("  </div>");
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }
}
