package app.forget.forgetfulnessapp.MailSender;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender extends AsyncTask<Void, Void, Void> {
    private String eMail, subject, message;
    private ProgressDialog progressDialog;
    @SuppressLint("StaticFieldLeak")
    private Context context;


    public EmailSender(Context context,String eMail,String subject, String message){
        this.context = context;
        this.eMail = eMail;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Lütfen bekleyin", "E-posta gönderiliyor", false, false);
    }




    @Override
    protected Void doInBackground(Void... voids) {
        //Gmail SMTP SUNUCU AYARLARI
        String host = "smtp.gmail.com";
        String username = "your_email@gmail.com";
        String password = "your_password";
        int port = 587;

        //E-posta gönderme işlemini gerçekleştir
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port",port);

        javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(eMail));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            Transport.send(mimeMessage);



        }catch (MessagingException e){
            Log.e("EmailSender","E-posta gönderme hatası"+e.getMessage());
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        // E-posta gönderildiğinde yapılması gereken işlemler burada yapılabilir.
    }
}
