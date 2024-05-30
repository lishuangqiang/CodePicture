package com.example.codepicture.CodePicture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class CreateImageCode {
    private int width = 160; // 图片的宽度。
    private int height = 40; // 图片的高度。
    private int codeCount = 4; // 验证码字符个数。
    private int lineCount = 20; // 验证码干扰线数。
    private String code; // 验证码。
    private BufferedImage buffImg = null; // 验证码图片Buffer。
    private Random random = new Random();

    // 默认构造方法
    public CreateImageCode() {
        this(160, 40, 4, 20);
    }

    // 构造方法，允许自定义图片大小和验证码字符个数
    public CreateImageCode(int width, int height, int codeCount) {
        this(width, height, codeCount, 20);
    }

    // 构造方法，允许自定义所有参数
    public CreateImageCode(int width, int height, int codeCount, int lineCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        creatImage();
    }

    // 生成图片的方法
    private void creatImage() {
        int fontWidth = width / codeCount; // 字体的宽度
        int fontHeight = height - 5; // 字体的高度
        int codeY = height - 8;

        // 图像buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffImg.getGraphics();
        // 设置背景色
        g.setColor(getRandColor());
        g.fillRect(0, 0, width, height);
        // 设置字体
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        g.setFont(font);

        // 设置干扰线
        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width / 10);
            int ye = ys + random.nextInt(height / 10);
            g.setColor(getRandColor());
            g.drawLine(xs, ys, xe, ye);
        }

        // 添加噪点
        float yawpRate = 0.01f; // 噪声率
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            buffImg.setRGB(x, y, random.nextInt(255));
        }

        // 生成验证码字符并绘制到图片上
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < codeCount; i++) {
            String strRand = randomStr();
            g.setColor(getRandColor());
            g.drawString(strRand, i * fontWidth + 3, codeY);
            codeBuilder.append(strRand);
        }
        this.code = codeBuilder.toString();
    }

    // 生成随机字符的方法
    private String randomStr() {
        String str1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        return str1.charAt(random.nextInt(str1.length())) + "";
    }

    // 生成随机颜色的方法
    private Color getRandColor() {
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    // 将图片写入到输出流的方法
    public void write(OutputStream sos) throws IOException {
        try (OutputStream os = sos) { // 使用try-with-resources自动关闭流
            ImageIO.write(buffImg, "png", os);
        }
    }

    // 将图片保存到本地文件的方法
    public void saveImageToFile(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }
        File file = new File(filePath);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            throw new IOException("Could not create directory: " + file.getParentFile().getAbsolutePath());
        }
        try (OutputStream os = new FileOutputStream(file)) { // 使用try-with-resources自动关闭流
            ImageIO.write(buffImg, "png", os);
        }
    }

    // 获取验证码图片BufferedImage对象的方法
    public BufferedImage getBuffImg() {
        return buffImg;
    }

    // 获取验证码字符串的方法
    public String getCode() {
        return code.toLowerCase();
    }
}