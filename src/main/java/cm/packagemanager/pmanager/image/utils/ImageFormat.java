package cm.packagemanager.pmanager.image.utils;


import cm.packagemanager.pmanager.common.utils.FileUtils;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.image.ent.bo.ImageFormatBO;
import org.imgscalr.Scalr;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public  class ImageFormat {

    public static final String JPG_EXTENSION = "jpg";
    public static final String JPEG_EXTENSION = "jpeg";
    public static final String PNG_EXTENSION = "png";

    private final File source;

    private  ImageFormatBO format;


    public ImageFormat(File source, ImageFormatBO format) {
        this.source = Objects.requireNonNull(source);
        this.format=format;
    }

    public void compressTo(File target) throws IOException {
        FileImageOutputStream targetOutputStream = new FileImageOutputStream(target);
        BufferedImage resizedImage = resize(source);

        ImageWriter writer = getWriter(FileUtils.getExtensionByApacheCommonLib(target.getName()));
        ImageWriteParam writerSettings = getWriterSettings(writer);

        try {
            writer.setOutput(targetOutputStream);
            writer.write(null, new IIOImage(resizedImage, null, null), writerSettings);
        } finally {
            writer.dispose();
            targetOutputStream.close();
            resizedImage.flush();
        }
    }

    private BufferedImage resize(File imageFile) throws IOException {
        BufferedImage sourceImage = ImageIO.read(imageFile);

        return Scalr.resize(sourceImage, Scalr.Mode.FIT_EXACT, format.width(), format.height());
    }

    private ImageWriter getWriter(String ext) {


        String EXTENSION=(StringUtils.equals(ext,JPG_EXTENSION) || StringUtils.equals(ext,JPEG_EXTENSION))?JPEG_EXTENSION:PNG_EXTENSION;
        Iterator<ImageWriter> imageWritersIterator =ImageIO.getImageWritersByFormatName(EXTENSION);

        if (!imageWritersIterator.hasNext()) {
            throw new NoSuchElementException(
                    String.format("Could not find an image writer for %s format", EXTENSION));
        }

        return imageWritersIterator.next();
    }

    private ImageWriteParam getWriterSettings(ImageWriter imageWriter) {
        ImageWriteParam imageWriteParams = imageWriter.getDefaultWriteParam();

        imageWriteParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParams.setCompressionQuality(format.compression());

        return imageWriteParams;
    }
}
