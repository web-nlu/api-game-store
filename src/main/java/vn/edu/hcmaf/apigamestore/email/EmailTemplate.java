package vn.edu.hcmaf.apigamestore.email;

public class EmailTemplate {
    public static final String ORDER_CONFIRMATION = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <style>\n" +
            "        body {\n" +
            "            font-family: Arial, sans-serif;\n" +
            "            color: #333333;\n" +
            "            background-color: #f9f9f9;\n" +
            "            padding: 20px;\n" +
            "        }\n" +
            "\n" +
            "        .container {\n" +
            "            background-color: #ffffff;\n" +
            "            padding: 20px;\n" +
            "            border-radius: 8px;\n" +
            "            box-shadow: 0 2px 8px rgba(0,0,0,0.05);\n" +
            "        }\n" +
            "\n" +
            "        h2 {\n" +
            "            color: #2e6da4;\n" +
            "        }\n" +
            "\n" +
            "        table {\n" +
            "            width: 100%%;\n" +
            "            border-collapse: collapse;\n" +
            "            margin-top: 16px;\n" +
            "        }\n" +
            "\n" +
            "        th, td {\n" +
            "            padding: 12px;\n" +
            "            border: 1px solid #dddddd;\n" +
            "            text-align: left;\n" +
            "        }\n" +
            "\n" +
            "        th {\n" +
            "            background-color: #f0f0f0;\n" +
            "        }\n" +
            "\n" +
            "        .footer {\n" +
            "            margin-top: 30px;\n" +
            "            font-size: 14px;\n" +
            "            color: #777777;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <h2>Xin chào %s,</h2>\n" +
            "        <p>Cảm ơn bạn đã đặt hàng tại <strong>Game Store</strong>!</p>\n" +
            "        <p>Mã đơn hàng của bạn là <strong>%s</strong>.</p>\n" +
            "\n" +
            "        <h3>Thông tin tài khoản bạn đã mua:</h3>\n" +
            "        <table>\n" +
            "            <thead>\n" +
            "                <tr>\n" +
            "                    <th>Tiêu đề tài khoản</th>\n" +
            "                    <th>Tên đăng nhập</th>\n" +
            "                    <th>Mật khẩu</th>\n" +
            "                </tr>\n" +
            "            </thead>\n" +
            "            <tbody>\n" +
            "                %s\n" +
            "            </tbody>\n" +
            "        </table>\n" +
            "\n" +
            "        <p class=\"footer\">Trân trọng,<br>Đội ngũ Game Store</p>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>\n";

    public static final String PASSWORD_RESET = "Dear %s,\n\n" +
            "You have requested to reset your password. Please click the link below to reset it:\n" +
            "%s\n\n" +
            "If you did not request this, please ignore this email.\n\n" +
            "Best regards,\n" +
            "The Game Store Team";
}
