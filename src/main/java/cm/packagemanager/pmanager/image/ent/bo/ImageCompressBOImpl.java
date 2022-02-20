package cm.packagemanager.pmanager.image.ent.bo;

import cm.packagemanager.pmanager.image.utils.ImageFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageCompressBOImpl implements ImageCompressBO {

    @Value("${file.storage.upload.folder}")
    private String FILE_DIRECTORY;

    @Override
    public  ImageFormat compress(ImageSourceBO imageSource, String imageName,String path) {
        return compressImage(imageSource, imageName, path);
    }

    @Override
    public void compress(ImageSourceBO imageSource, String imageName) {
        compressImage(imageSource, imageName, FILE_DIRECTORY);
    }

    private ImageFormat compressImage(ImageSourceBO imageSource, String imageName, String file_directory) {
        try {
            Path imagesPath = Files.createDirectories(Paths.get(file_directory));
            String compressedImageFileName = imageName;
            File compressedImageFile = imagesPath.resolve(compressedImageFileName).toFile();

            File imageSourceFile = imageSource.asFile();

            ImageFormat imgFmt=new ImageFormat(imageSourceFile, new SquareCompressed(200));
            imgFmt.compressTo(compressedImageFile);

            imageSourceFile.delete();
            return imgFmt;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
