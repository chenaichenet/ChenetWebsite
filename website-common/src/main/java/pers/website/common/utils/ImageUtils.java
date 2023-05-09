package pers.website.common.utils;

import org.springframework.util.Assert;
import pers.website.common.exceptions.CustomException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static pers.website.common.constants.Constants.ImageUtilsConf.*;

/**
 * 图片工具类
 *
 * @author ChenetChen
 * @since 2023/4/19 9:29
 */
public class ImageUtils {
    /**
     * 添加水印
     * @param watermarkType 水印样式（1：全图水印；2：右下角水印）
     * @param markText 文字水印
     * @param srcImgPath 图片源路径
     * @param targetPath 目标路径
     * @param imgFormat 图片格式
     */
    public static void addWatermark(Integer watermarkType, String markText, Color color, String srcImgPath, String targetPath, String imgFormat) {
        addWatermark(watermarkType, markText, COLOR, srcImgPath, targetPath, FONT_SIZE, MASK_DEGREE, MASK_OPACITY, imgFormat);
    }

    /**
     * 添加水印
     * @param watermarkType 水印样式（1：全图水印；2：右下角水印）
     * @param maskText 水印文字
     * @param srcImgPath 图片源路径
     * @param targetPath 图片输出路径
     * @param fontSize 水印字体大小
     * @param degree 水印旋转角度
     * @param opacity 水印不透明度
     * @param imgFormat 输出图片格式
     */
    public static void addWatermark(Integer watermarkType, String maskText, Color color, String srcImgPath, String targetPath,
                                    Integer fontSize, Integer degree, float opacity, String imgFormat) {
        try {
            BufferedImage bufferedImage = getBufferedImage(srcImgPath);
            Assert.notNull(bufferedImage, "图片对象为空");
            drawWatermark(watermarkType, maskText, color, fontSize, degree, opacity, bufferedImage);
            try (OutputStream outputStream = Files.newOutputStream(Paths.get(targetPath))) {
                ImageIO.write(bufferedImage, imgFormat, outputStream);
            }
        } catch (IOException e) {
            throw new CustomException("添加水印，IO异常");
        }
    }

    /**
     * 绘制水印
     * @param watermarkType 水印样式（1：全图水印；2：右下角水印）
     * @param markText 水印文字
     * @param color 水印颜色
     * @param fontSize 字体大小
     * @param degree 旋转角度
     * @param opacity 不透明度
     * @param bufferedImage 图片对象
     */
    private static void drawWatermark(Integer watermarkType, String markText, Color color, Integer fontSize,
                                      Integer degree, float opacity, BufferedImage bufferedImage) {
        // 基于图片对象创建画笔
        Graphics2D graphics2D = bufferedImage.createGraphics();
        // 消除文字锯齿、设置内插模式
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // 设置旋转角度、颜色、字体、透明度
        graphics2D.setColor(color);
        graphics2D.setFont(new Font(Font.DIALOG, Font.ITALIC, fontSize));
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, opacity));
        
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        switch (watermarkType) {
            case 1: {
                // 全图水印
                if (null != degree) {
                    graphics2D.rotate(Math.toRadians(degree));
                }
                int x = -width / 2, y, length = getTextLength(markText);
                while (x < width * 2) {
                    y = -height / 2;
                    while (y < height * 2) {
                        graphics2D.drawString(markText, x, y);
                        y += fontSize + 80;
                    }
                    x += fontSize * length + 80;
                }
            }
            break;
            case 2: {
                // 右下角水印
                int length = getTextLength(markText);
                graphics2D.drawString(markText, width-((length + 2) * fontSize), height-20);
            }
            break;
            default: break;
        }
        graphics2D.dispose();
    }

    /**
     * 获取图片对象
     * @param srcImgPath 图片路径
     * @return 图片对象
     * @throws IOException IO异常
     */
    private static BufferedImage getBufferedImage(String srcImgPath) throws IOException {
        return ImageIO.read(new File(srcImgPath));
    }

    /**
     * 获取文本长度
     * 汉字为1:1，英文和数字为2:1
     * @param text 文本
     * @return 长度
     */
    private static int getTextLength (String text) {
        int length = text.length();
        for (int i = 0; i < text.length(); i++) {
            String s = String.valueOf(text.charAt(i));
            if (s.getBytes().length > 1) {
                length++;
            }
        }
        length = length % 2 == 0 ? length / 2 : length / 2 + 1;
        return length;
    }
}
