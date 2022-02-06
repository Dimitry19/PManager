package cm.packagemanager.pmanager.common.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


@Component
public class FileUtils {
    protected final Log logger = LogFactory.getLog(FileUtils.class);

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
    public String generateFilename(String  filename) throws Exception {

        String randomString=randomString(filename.length());
        return  randomString+filename;
    }

    public void checkType(MultipartFile file) throws Exception {

        if (!StringUtils.equals(file.getContentType(),"image/png") &&  !StringUtils.equals(file.getContentType(),"image/jpeg")) {
            throw new Exception("Please select a picture in .png/jpg format");
        }
    }

    public void saveFileToFileSystem(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public void saveFileToFileSystem(String uploadDir, String fileName, String data) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        File fileSaved = new File(StringUtils.concatenate(uploadDir, fileName));
        byte[] buffer = convertByteArray( uploadDir, fileName);//(DatatypeConverter.parseBase64Binary(data));

        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileSaved))) {
            outputStream.write(buffer);
        } catch (IOException e) {
            logger.error("Erreur durant la sauvegarde du file " + fileName, e);
            e.printStackTrace();
        }
    }


    public  byte[] convertByteArray(String uploadDir,String fileName) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        ClassPathResource imageFile = new ClassPathResource(StringUtils.concatenate(uploadDir,fileName));
        return StreamUtils.copyToByteArray(imageFile.getInputStream());
    }


    public  static byte[] decompressImage(byte[] data) {

        if (data== null) return null;
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception exception) {
        }
        return outputStream.toByteArray();
    }

    public static  byte[] compressImage(byte[] data) {

        if (data== null) return null;


        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception e) {
        }
        return outputStream.toByteArray();
    }
}
