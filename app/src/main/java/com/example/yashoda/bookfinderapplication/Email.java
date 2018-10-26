//package com.example.yashoda.bookfinderapplication;
////
////import android.os.AsyncTask;
////import android.os.Message;
////
////import javax.mail.MessagingException;
////import javax.mail.Transport;
////import javax.mail.internet.InternetAddress;
////import javax.mail.internet.MimeMessage;
////
////
////public class Email extends AsyncTask<String, Void, String>
////{
////    @Override
////    protected String doInBackground(String... params) {
////
////        try
////        {
////            Message message = new MimeMessage(session);
////            message.setFrom(new InternetAddress("karesh2403@gmail.com"));
////            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
////            message.setSubject(subject);
////            message.setContent(textMessage, "text/html; charset=utf-8");
////            Transport.send(message);
////        }
////        catch(MessagingException e) {
////            e.printStackTrace();
////        }
////        catch(Exception e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
////}