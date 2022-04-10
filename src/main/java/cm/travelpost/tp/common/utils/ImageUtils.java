package cm.travelpost.tp.common.utils;

import cm.travelpost.tp.configuration.filters.AuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.validation.ValidationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


//https://blog.blebail.com/java/spring/2020/03/23/upload-resize-and-compress-images-with-spring-and-imgscalr.html
public class ImageUtils {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    static boolean initialized=false;

    public static String IMG_JPEG="image/jpeg";
    public static String IMG_PNG="image/png";

    @Value("${image.dimension.limit.width}")
    private static int limitWidth;

    @Value("${image.dimension.limit.heigth}")
    private static int limitHeight;


    @Value("${image.dimension.resize.width}")
    private static int resizedWidth;

    @Value("${image.dimension.resize.heigth}")
    private  static int resizedHeight;


    /**
     * Gets image dimensions for given file
     * @param imgFile image file
     * @return dimensions of image
     * @throws IOException if the file is not a known image
     */
    public static Dimension getImageDimension(File imgFile) throws IOException {
        int pos = imgFile.getName().lastIndexOf(".");
        if (pos == -1)
            throw new IOException("No extension for file: " + imgFile.getAbsolutePath());
        String suffix = imgFile.getName().substring(pos + 1);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        while(iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                ImageInputStream stream = new FileImageInputStream(imgFile);
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                return new Dimension(width, height);
            } catch (IOException e) {
                logger.warn("Error reading: " + imgFile.getAbsolutePath(), e);
            } finally {
                reader.dispose();
            }
        }

        throw new IOException("Not a known image file: " + imgFile.getAbsolutePath());
    }

    public static Point getImageSizeFromStream(InputStream is) {
        if (!initialized) {
            // Stops ImageIO from creating temp files when reading images
            // from input stream.
            ImageIO.setUseCache(false);
            initialized = true;
        }
        try {
            ImageInputStream imageStream = ImageIO.createImageInputStream(is);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageStream);
            if (!readers.hasNext()) return null;
            ImageReader reader = readers.next();
            try {
                reader.setInput(imageStream);
                return new Point(reader.getWidth(0), reader.getHeight(0));
            } finally {
                reader.dispose();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Dimension getImageDimensionFromStream(InputStream is) {
        if (!initialized) {
            // Stops ImageIO from creating temp files when reading images
            // from input stream.
            ImageIO.setUseCache(false);
            initialized = true;
        }
        try {
            ImageInputStream imageStream = ImageIO.createImageInputStream(is);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageStream);
            if (!readers.hasNext()) return null;
            ImageReader reader = readers.next();
            try {
                reader.setInput(imageStream);
                return new Dimension(reader.getWidth(0), reader.getHeight(0));
            } finally {
                reader.dispose();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static InputStream resizeImage(InputStream input, String contentType) throws IOException {

        BufferedImage bi=resizeImage(input,resizedWidth,resizedHeight);
        return convertToInputStream(bi,contentType);

    }

    public static BufferedImage resizeImage(InputStream input, Integer targetWidth, Integer targetHeight) throws IOException {

        BufferedImage originalImage = ImageIO.read(input);


        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    public void resize(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);

        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    public  static byte[] decompressImage(byte[] data) {

        if(data==null) return null;
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

        if(data==null) return null;

        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[1024];
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


    public static void validate(MultipartFile file) throws Exception {

        checkType(file);
        //validateImageSize(file.getInputStream());
    }

    private static void validateImageSize(InputStream is){

        Dimension dimension=getImageDimensionFromStream(is);
        boolean isValid= dimension!=null && dimension.getHeight()<=limitHeight && dimension.getWidth()<=limitWidth;

        if(!isValid){
            throw new ValidationException("Les dimensions de l'image ne sont pas valides");
        }

    }

    private static void checkType(MultipartFile file) throws Exception {

        if (!StringUtils.equals(file.getContentType(),ImageUtils.IMG_JPEG) &&  !StringUtils.equals(file.getContentType(),ImageUtils.IMG_PNG)) {
            throw new Exception("Please select a picture in .png/jpg format");
        }
    }

    public static String getExtension(MultipartFile file) throws Exception {

        switch (file.getContentType()){

            case "image/jpeg":
                 return "jpeg";

            case "image/png":
                return  "png";
            default:
                throw new IllegalStateException("Unexpected value: " + file.getContentType());
        }

    }

    public static InputStream convertToInputStream(BufferedImage bufferedImage, String contentType){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String extension="";

        switch (contentType){
            case "image/jpeg":
                extension ="jpeg";
                break;
            case "image/png":
                extension ="png";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + contentType);
        }

       try {

           ImageIO.write(bufferedImage,extension, os);// Passing: ​(RenderedImage im, String formatName, OutputStream output)
           InputStream is = new ByteArrayInputStream(os.toByteArray());

           return  is;
        } catch (IOException e) {
            e.printStackTrace();
        }

       return null;
    }

    public static byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }
}
