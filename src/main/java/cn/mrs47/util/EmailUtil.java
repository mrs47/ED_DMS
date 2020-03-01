package cn.mrs47.util;

import cn.mrs47.pojo.EmailMessage;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * @author mrs47
 */

public class EmailUtil {

    private static String emailAccount;
    private static String emailPsw;
    private static Session session;
    private static String url;

    /**
     * 修改密码的验证码发送
     * @param msg
     * @return
     * @throws Exception
     */
    public static Boolean sendForgetEmail(EmailMessage msg) throws Exception {
        boolean isSuccess = true;
        init();
        msg.setPersonal("嵌入式管理平台");
        msg.setSubject("嵌入式管理-验证码");
        msg.setContent("你的验证码是:<br/> "
                + "<p>" + msg.getCode()
                + "</p><p>此验证码用于密码修改，打死不能告诉别人！</p>");
        //3.根据Session获取邮件传输对象
        Transport transport = session.getTransport();
        try {
            //4.创建一封邮件
            MimeMessage message = createMimeMessage(session, emailAccount, msg);
            // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
            transport.connect(emailAccount, emailPsw);
            // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            // 7. 关闭连接
            transport.close();
        }
        return isSuccess;

    }


    /**
     *  发送注册邮件
     * @param msg
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    public static Boolean sendRegisterEmail(EmailMessage msg) throws IOException, MessagingException {
        boolean isSuccess = true;
        init();
        msg.setPersonal("嵌入式管理平台注册中心");
        msg.setSubject("嵌入式管理平台注册中心");
        msg.setContent("尊敬的" + msg.getUserName() + "先生:<br/>   当您看到这封邮箱时,现在时间为:" + new Date()
                + "，您已经注册了嵌入式管理平台账号。请点击下方连接进行激活!"
                + "</br> <a href='" +url+ msg.getUrl()
                + "' target='_blank'><p>" + msg.getUrl()
                + "</p></a> ");

        //3.根据Session获取邮件传输对象
        Transport transport = session.getTransport();
        try {
            //4.创建一封邮件
            MimeMessage message = createMimeMessage(session, emailAccount, msg);
            // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
            transport.connect(emailAccount, emailPsw);
            // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            // 7. 关闭连接
            transport.close();
        }
        return isSuccess;
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session  和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param msg      收件人信息
     * @return message MimeMessage
     * @throws Exception 抛出错误
     */
    private static MimeMessage createMimeMessage(Session session, String sendMail, EmailMessage msg) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sendMail, msg.getPersonal(), "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(msg.getUserEmail(), "尊敬的用户", "UTF-8"));
        message.setSubject(msg.getSubject(), "UTF-8");
        message.setContent(msg.getContent(), "text/html;charset=utf-8");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private static void init() throws IOException {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = EmailUtil.class.getClassLoader().getResourceAsStream("email.properties");
            properties.load(in);
            emailAccount = properties.getProperty("email.emailAccount");
            emailPsw = properties.getProperty("email.emailPassword");
            url=properties.getProperty("email.url");
            properties.setProperty("mail.smtp.port", "80");
            properties.setProperty("mail.smtp.socketFactory.port", "80");
            session = Session.getInstance(properties);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in!=null){
                in.close();
            }
        }
    }
}
