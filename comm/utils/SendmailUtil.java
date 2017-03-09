package com.siweidg.comm.utils;

import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
 
public class SendmailUtil {
     
    // 设置服务器
    private static String KEY_SMTP = "mail.smtp.host";
    private static String VALUE_SMTP = "smtp.siweidg.com";
    // 服务器验证
    private static String KEY_PROPS = "mail.smtp.auth";
    private static boolean VALUE_PROPS = true;
    // 发件人用户名、密码
    private  static String SEND_USER = "orderservice@siweidg.com";
    private static  String SEND_UNAME = "orderservice";
    private static  String SEND_PWD = "48169eXb7";
 
 
 
    /**
     * 发送邮件
     * 
     * @param headName
     *            邮件头文件名
     * @param sendHtml
     *            邮件内容
     * @param receiveUser
     *            收件人地址(多个)
     */
    public static void doSendHtmlEmail(String headName, String sendHtml,
            String[] receiveUsers) {
        try {
    	   Properties props = System.getProperties();
           props.setProperty(KEY_SMTP, VALUE_SMTP);
           props.put(KEY_PROPS, "true");
           Session  s =  Session.getDefaultInstance(props, new Authenticator(){
              protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(SEND_UNAME, SEND_PWD);
              }});
            s.setDebug(true);
            MimeMessage  message = new MimeMessage(s);
            // 发件人
            InternetAddress from = new InternetAddress(SEND_USER);
            message.setFrom(from);
            // 收件人
            
            InternetAddress[] addresses = new InternetAddress[receiveUsers.length];
           
           for(int i =0 ;i<receiveUsers.length;i++) {
            	addresses[i] = new InternetAddress(receiveUsers[i]);  
            
        	}
            
            message.setRecipients(RecipientType.TO, addresses);  
            // 邮件标题
            message.setSubject(headName);
            String content = sendHtml.toString();
            // 邮件内容,也可以使纯文本"text/plain"
            message.setContent(content, "text/html;charset=GBK");
            message.saveChanges();
            Transport transport = s.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(VALUE_SMTP, SEND_UNAME, SEND_PWD);
            // 发送
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            
            System.out.println("send success!");
        } catch (AddressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
      
    }
}
