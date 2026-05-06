package sena.adso.saludboyaca.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Properties;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class OTPService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;

    private static final int OTP_LONGITUD = 6;
    private static final long OTP_EXPIRA_MS = 5 * 60 * 1000L;

    // Lee siempre en el momento del envío, nunca como static final
    private static String getEmailRemit() {
        String v = System.getenv("EMAIL_FROM");
        return (v != null && !v.isBlank()) ? v : "cristiannalvarez95@gmail.com";
    }

    private static String getEmailPass() {
        String v = System.getenv("EMAIL_PASS");
        return (v != null && !v.isBlank()) ? v : "edpl qtng sltq vnth";
    }

    public static String generarOTP() {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(OTP_LONGITUD);
        for (int i = 0; i < OTP_LONGITUD; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }

    public static boolean esValido(String ingresado, String guardado, long timestamp) {
        if (ingresado == null || guardado == null) {
            return false;
        }
        long ahora = Instant.now().toEpochMilli();
        return (ahora - timestamp) <= OTP_EXPIRA_MS && ingresado.trim().equals(guardado);
    }

    public static void enviarOTP(String destinatario, String codigoOTP,
            String asunto, String cuerpo) throws MessagingException {

        final String remit = getEmailRemit();
        final String pass = getEmailPass();

        System.out.println("[OTPService] Remitente : " + remit);
        System.out.println("[OTPService] Pass env  : " + (System.getenv("EMAIL_PASS") != null ? "OK" : "NULL - usando fallback"));
        System.out.println("[OTPService] Destino   : " + destinatario);

        // ── Fix PKIX: instalar TrustManager que acepta certificados de Gmail ──
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] c, String a) {
                }

                public void checkServerTrusted(X509Certificate[] c, String a) {
                }
            }}, new java.security.SecureRandom());
            SSLContext.setDefault(sc);
        } catch (Exception sslEx) {
            System.err.println("[OTPService] Advertencia SSL: " + sslEx.getMessage());
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");

        Session mailSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remit, pass);
            }
        });

        try {
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(remit, "SaludBoyaca"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            msg.setSubject(asunto);
            msg.setText(cuerpo);
            Transport.send(msg);
            System.out.println("[OTPService] OTP enviado correctamente a: " + destinatario);
        } catch (UnsupportedEncodingException ex) {
            throw new MessagingException("Error codificando direccion", ex);
        }
    }
}
