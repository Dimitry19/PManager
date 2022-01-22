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
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


@Component
public class FileUtils {
    protected final Log logger = LogFactory.getLog(FileUtils.class);


    public String getExtension(String data) throws Exception {

        JSONObject json = new JSONObject(data);
        String type = json.getString("type");

        if (StringUtils.isNotEmpty(type) && StringUtils.equals(type, "image/png")) {
            return StringUtils.concatenate(".png");
        }

        if (StringUtils.isNotEmpty(type) && StringUtils.equals(type, "image/jpeg")) {
            return StringUtils.concatenate(".jpeg");
        }
        throw new Exception("Please select a picture in .png.jpg format");

    }

    public String getFilename(String data) throws Exception {
        String filename = data.substring(data.indexOf("filename=")+10, data.indexOf("Content-Type")-3);
        return filename;
    }

    public String retrieveImageExtension(String base64File) throws Exception {
        if (io.micrometer.core.instrument.util.StringUtils.isBlank(base64File)) {
            throw new Exception("Please select a picture in .png.jpg format");
        } else if (base64File.indexOf("image/png;") != -1) {
            return StringUtils.concatenate(".png");
        } else if (base64File.indexOf("image/jpeg;") != -1) {
            return StringUtils.concatenate(".jpeg");
        } else {
            throw new Exception("Please select a picture in .png.jpg format");
        }
    }

    public String clean(String base64File) throws Exception {
        if (io.micrometer.core.instrument.util.StringUtils.isBlank(base64File)) {
            throw new Exception("Please select a picture in .png.jpg format");

        } else if (base64File.indexOf("data:image/png;") != -1) {
            return base64File.replace("data:image/png;base64,", "");
        } else if (base64File.indexOf("data:image/jpeg;") != -1) {
            return base64File.replace("data:image/jpeg;base64,", "");
        } else {
            throw new Exception("Please select a picture in .png.jpg format");
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

    // compress the image bytes before storing it in the database
    public byte[] compressBytes(byte[] data) {

        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {

        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ioe) {

        }
        return outputStream.toByteArray();
    }

    public String encodeBase64Image(String imagePath) {
        File file = new File(imagePath);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a Image file from file system
            String base64Image = "";
			byte[] imageData = new byte[(int) file.length()];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
            return base64Image;
        } catch (FileNotFoundException e) {
            logger.error("Image not found", e);
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            logger.error("Exception while reading the Image ", ioe);
            System.out.println("Exception while reading the Image " + ioe);
        }
        return null;
    }

    public void decodeBase64Image(String base64Image, String pathFile) {
        try (FileOutputStream imageOutFile = new FileOutputStream(pathFile)) {
            // Converting a Base64 String into Image byte array
            byte[] imageByteArray = Base64.getDecoder().decode(base64Image);
            imageOutFile.write(imageByteArray);
        } catch (FileNotFoundException e) {
            logger.error("Image not found", e);
        } catch (IOException ioe) {
            logger.error("Exception while reading the Image " + ioe);
        }
    }

    public InputStream convertFileToInputStream(String pathName) throws IOException {
        File initialFile = new File(pathName);
        InputStream targetStream = new FileInputStream(initialFile);
        return targetStream;
    }

    public void convertFileToInputStreamWithCommonIO(String pathName)
            throws IOException {
        File initialFile = new File(pathName);
        InputStream targetStream = org.apache.commons.io.FileUtils.openInputStream(initialFile);
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
