package pers.website.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import pers.website.common.exceptions.CustomException;

import java.io.*;

import static pers.website.common.constants.Constants.FileUtilsConf.*;

/**
 * 文件工具类
 *
 * @author ChenetChen
 * @since 2023/5/8 14:18
 */
@Slf4j
public class FileUtils {
    
    /**
     * 上传文件
     * @param type 文件分类——1-文件；2-书籍；3-音乐
     * @param file 文件
     * @return 文件全路径
     */
    public static String uploadFile(Integer type, MultipartFile file) {
        File fileDir;
        String filePath;
        // 通过系统时间生成文件名
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        switch (type) {
            case 1: {
                filePath = FILE_PATH + fileName;
                fileDir = new File(ROOT_PATH + FILE_PATH);
            } break;
            case 2: {
                filePath = BOOK_PATH + fileName;
                fileDir = new File(ROOT_PATH + BOOK_PATH);
            } break;
            case 3: {
                filePath = MUSIC_PATH + fileName;
                fileDir = new File(ROOT_PATH + MUSIC_PATH);
            } break;
            default: {
                log.error("文件分类指定失败，无法创建文件目录");
                throw new CustomException("文件分类指定失败，无法创建文件目录");
            }
        }
        if (!fileDir.exists()) {
            boolean makeStatus = fileDir.mkdirs();
            if (!makeStatus) {
                log.error("上传文件创建文件夹失败");
                throw new CustomException("上传文件创建文件夹失败");
            }
        }
        try {
            File newFile = new File(fileDir.getAbsolutePath() + File.separator + fileName);
            // 上传文件
            file.transferTo(newFile);
        } catch (IOException e) {
            throw new CustomException("文件上传失败");
        } 
        return filePath;
    }

    /**
     * 文件下载
     * @param response 响应
     * @param filePath 文件路径
     * @return 是否下载成功
     */
    public static Boolean downloadFile(HttpServletResponse response, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            // 获取文件名
            String[] split = filePath.split("/");
            String fileName = split[split.length - 1];
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName="+fileName);
            try (FileInputStream fis = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(fis)) {
                byte[] buffer = new byte[1024];
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                return true;
            } catch (IOException e) {
                log.error("文件下载失败");
                throw new CustomException("文件下载失败");
            }
        }
        return false;
    }

    /**
     * 文件删除
     * @param filePath 文件路径
     * @return 文件是否删除成功
     */
    public static Boolean deleteFile(String filePath){
        File file = new File(filePath);
        if (file.exists()){
            return file.delete();
        } else {
            log.info("文件不存在");
        }
        return false;
    }
}
