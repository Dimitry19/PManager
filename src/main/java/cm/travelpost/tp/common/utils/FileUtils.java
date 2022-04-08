package cm.travelpost.tp.common.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;


@Component
public class FileUtils {
    protected final Log logger = LogFactory.getLog(FileUtils.class);

    private static final FileNameMap MIMETYPES = URLConnection.getFileNameMap();


    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public String generateFilename(String  filename) throws Exception {

        String randomString=randomString(5);
        return  randomString+filename;
    }

    public static String getExtensionByApacheCommonLib(String filename) {
        return FilenameUtils.getExtension(filename);
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

    /**
     * Get a file mime type based on its file path extension.
     *
     * Uses URLConnection.getFileNameMap().
     *
     * @param filename
     *            file path.
     * @return String mime type.
     */
    public static String getContentType(final String filename) {
        return MIMETYPES.getContentTypeFor(filename);
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
}
