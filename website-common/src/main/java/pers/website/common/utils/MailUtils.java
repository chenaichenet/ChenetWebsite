package pers.website.common.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pers.website.common.exceptions.CustomException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pers.website.common.constants.Constants.MailUtilsConf.*;

/**
 * 邮件工具类
 *
 * @author ChenetChen
 * @since 2023/3/7 12:32
 */
@Slf4j
public class MailUtils {
    /**
     * 正则校验邮箱地址
     * @param emailAddress 邮箱地址
     * @return 校验结果，true：通过；false：失败
     */
    public static Boolean checkMail(String emailAddress) {
        Pattern regex = Pattern.compile("^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher matcher = regex.matcher(emailAddress);
        return matcher.matches();
    }

    /**
     * 发送邮件
     *
     * @param address 发送地址
     * @param title   邮件标题
     * @param content 邮件内容
     */
    public static void sendMail(String address, String title, String content) {
        String template = readHtmlToString();
        Document document = Jsoup.parse(template);
        Element elementTitle = document.getElementById("title");
        if (elementTitle != null) {
            elementTitle.html(title);
        }
        Element documentElementById = document.getElementById("content");
        if (documentElementById != null) {
            documentElementById.html(content);
        }
        String mailContent = document.toString();

        // 创建Session会话
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "SMTP");
        props.setProperty("mail.host", MAIL_HOST);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.connectiontimeout", "25000");
        props.setProperty("mail.smtp.timeout", "25000");
        props.setProperty("mail.smtp.writetimeout", "25000");
        // 创建验证器
        Authenticator auth = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, DesUtils.desCrypt(PASSWORD, 1));
            }
        };
        Session session = Session.getInstance(props, auth);

        // 创建一个Message，邮件内容
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(address));
            message.setSubject(title);
            message.setContent(mailContent, "text/html;charset=utf-8");
            message.setSentDate(new Date());
            message.saveChanges();

            // 创建Transport发送邮件
            Transport.send(message);
        } catch (MessagingException e) {
            log.error("发送邮件失败");
        }
    }

    /**
     * 读取html邮件模板转为字符串
     *
     * @return 字符串
     */
    private static String readHtmlToString() {
        String result = "";
        InputStream is = null;
        Reader reader = null;
        try {
            is = MailUtils.class.getClassLoader().getResourceAsStream(TEMPLATES_PATH);
            if (is == null) {
                log.error("未找到邮件模板文件");
                throw new CustomException("未找到邮件模板文件，生成流失败");
            }
            reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            int length;
            while ((length = reader.read(buffer, 0, 1024)) != -1) {
                sb.append(buffer, 0, length);
            }
            result = sb.toString();
        } catch (UnsupportedEncodingException e) {
            log.error("创建Reader失败", e);
        } catch (IOException e) {
            log.error("邮件模块文件读取失败", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error("关闭io流异常", e);
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                log.error("关闭io流异常", e);
            }
        }
        return result;
    }
}
