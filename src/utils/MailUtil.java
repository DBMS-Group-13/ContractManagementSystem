package utils;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeMessage;  

import model.User;
public class MailUtil {
	//--------------参数---------------------
    private static final String FROM = "garciiaa@163.com";//发件人的email
    private static final String PWD = "cs123456";//发件人密码--邮箱密码
    private static final String URL = "http://localhost:8888/ContractSystem";//项目主页
    private static final int TIMELIMIT = 1000*60*60*24; //激活邮件过期时间24小时
    private static final String TITLE = "iClass账户激活邮件";
    private static final String HOST = "smtp.163.com";
    private static final String SMTP = "smtp";

    public static User activateMail(User u) throws AddressException, MessagingException, NoSuchAlgorithmException {
        //注册邮箱
        String to  = u.getEmail();
        //当前时间戳
        Long curTime = System.currentTimeMillis();
        //激活的有效时间
        Long activateTime = curTime+TIMELIMIT;
        //激活码--用于激活邮箱账号
        String token = to+curTime;
        u.setToken(SecurityUtil.md5(token));
        u.setCreateDate(new Date().toString());
        token = u.getToken();
        //过期时间
        u.setActivateTime(activateTime);
        //发送的邮箱内容
        String content = "<p>您好 O(∩_∩)O~~<br><br>欢迎加入ContractSystem!<br><br>帐户需要激活才能使用，赶紧激活成为ContractSystem正式的一员吧:)<br><br>请在24小时内点击下面的链接立即激活帐户："
        +"<br><a href='"+URL+"/activatemail/?token="+token+"&email="+to+"'>"
        +URL+"/activatemail/?token="+token+"&email="+to+"</a></p>";
        //调用发送邮箱服务
        MailUtil.sendMail(to, TITLE, content);
        return u;
    }
    
    public static void sendMail(String to,String title,String content) throws AddressException, MessagingException {

        Properties props = new Properties(); //可以加载一个配置文件  
        // 使用smtp：简单邮件传输协议  
        props.put("mail.smtp.host", HOST);//存储发送邮件服务器的信息  
        props.put("mail.smtp.auth", "true");//同时通过验证  
        Session session = Session.getInstance(props);//根据属性新建一个邮件会话  
        //session.setDebug(true); //有他会打印一些调试信息。  
        MimeMessage message = new MimeMessage(session);//由邮件会话新建一个消息对象  
        message.setFrom(new InternetAddress(FROM));//设置发件人的地址  
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));//设置收件人,并设置其接收类型为TO  
        message.setSubject(title);//设置标题  
        //设置信件内容  
        //message.setText(mailContent); //发送 纯文本 邮件 todo  
        message.setContent(content, "text/html;charset=gbk"); //发送HTML邮件，内容样式比较丰富  
        message.setSentDate(new Date());//设置发信时间  
        message.saveChanges();//存储邮件信息  
        //发送邮件  
        Transport transport = session.getTransport(SMTP);  
        //Transport transport = session.getTransport();  
        transport.connect(FROM, PWD);
        transport.sendMessage(message, message.getAllRecipients());//发送邮件,其中第二个参数是所有已设好的收件人地址  
        transport.close();  
    }
    
}
