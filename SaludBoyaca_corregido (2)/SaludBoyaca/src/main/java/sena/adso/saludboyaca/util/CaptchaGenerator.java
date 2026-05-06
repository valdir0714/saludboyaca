package sena.adso.saludboyaca.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;

public class CaptchaGenerator {

    private static final int WIDTH = 160;
    private static final int HEIGHT = 50;
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int LENGTH = 5;

    public static String generarTexto() {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(CHARS.charAt(rnd.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    public static BufferedImage generarImagen(String texto) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(230, 240, 250));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        SecureRandom rnd = new SecureRandom();
        // Líneas de ruido
        g.setStroke(new BasicStroke(1.5f));
        for (int i = 0; i < 6; i++) {
            g.setColor(new Color(rnd.nextInt(180), rnd.nextInt(180), rnd.nextInt(180)));
            g.drawLine(rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT),
                    rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT));
        }

        // Texto
        g.setFont(new Font("Courier New", Font.BOLD, 26));
        int x = 10;
        for (char c : texto.toCharArray()) {
            g.setColor(new Color(20 + rnd.nextInt(80), 20 + rnd.nextInt(80), 100 + rnd.nextInt(100)));
            g.drawString(String.valueOf(c), x, 34 + rnd.nextInt(8));
            x += 28;
        }

        // Puntos de ruido
        for (int i = 0; i < 40; i++) {
            g.setColor(new Color(rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200)));
            g.fillOval(rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT), 2, 2);
        }

        g.dispose();
        return img;
    }

    public static boolean validar(String ingresado, String guardado) {
        return ingresado != null && guardado != null
                && ingresado.trim().equalsIgnoreCase(guardado.trim());
    }
}
