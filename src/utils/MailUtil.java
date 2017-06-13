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
	//--------------����---------------------
    private static final String FROM = "garciiaa@163.com";//�����˵�email
    private static final String PWD = "cs123456";//����������--��������
    private static final String URL = "http://localhost:8888/ContractSystem";//��Ŀ��ҳ
    private static final int TIMELIMIT = 1000*60*60*24; //�����ʼ�����ʱ��24Сʱ
    private static final String TITLE = "iClass�˻������ʼ�";
    private static final String HOST = "smtp.163.com";
    private static final String SMTP = "smtp";

    public static User activateMail(User u) throws AddressException, MessagingException, NoSuchAlgorithmException {
        //ע������
        String to  = u.getEmail();
        //��ǰʱ���
        Long curTime = System.currentTimeMillis();
        //�������Чʱ��
        Long activateTime = curTime+TIMELIMIT;
        //������--���ڼ��������˺�
        String token = to+curTime;
        u.setToken(SecurityUtil.md5(token));
        u.setCreateDate(new Date().toString());
        token = u.getToken();
        //����ʱ��
        u.setActivateTime(activateTime);
        //���͵���������
        String content = "<p>���� O(��_��)O~~<br><br>��ӭ����ContractSystem!<br><br>�ʻ���Ҫ�������ʹ�ã��Ͻ������ΪContractSystem��ʽ��һԱ��:)<br><br>����24Сʱ�ڵ��������������������ʻ���"
        +"<br><a href='"+URL+"/activatemail/?token="+token+"&email="+to+"'>"
        +URL+"/activatemail/?token="+token+"&email="+to+"</a></p>";
        //���÷����������
        MailUtil.sendMail(to, TITLE, content);
        return u;
    }
    
    public static void sendMail(String to,String title,String content) throws AddressException, MessagingException {

        Properties props = new Properties(); //���Լ���һ�������ļ�  
        // ʹ��smtp�����ʼ�����Э��  
        props.put("mail.smtp.host", HOST);//�洢�����ʼ�����������Ϣ  
        props.put("mail.smtp.auth", "true");//ͬʱͨ����֤  
        Session session = Session.getInstance(props);//���������½�һ���ʼ��Ự  
        //session.setDebug(true); //�������ӡһЩ������Ϣ��  
        MimeMessage message = new MimeMessage(session);//���ʼ��Ự�½�һ����Ϣ����  
        message.setFrom(new InternetAddress(FROM));//���÷����˵ĵ�ַ  
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));//�����ռ���,���������������ΪTO  
        message.setSubject(title);//���ñ���  
        //�����ż�����  
        //message.setText(mailContent); //���� ���ı� �ʼ� todo  
        message.setContent(content, "text/html;charset=gbk"); //����HTML�ʼ���������ʽ�ȽϷḻ  
        message.setSentDate(new Date());//���÷���ʱ��  
        message.saveChanges();//�洢�ʼ���Ϣ  
        //�����ʼ�  
        Transport transport = session.getTransport(SMTP);  
        //Transport transport = session.getTransport();  
        transport.connect(FROM, PWD);
        transport.sendMessage(message, message.getAllRecipients());//�����ʼ�,���еڶ�����������������õ��ռ��˵�ַ  
        transport.close();  
    }
    
}
