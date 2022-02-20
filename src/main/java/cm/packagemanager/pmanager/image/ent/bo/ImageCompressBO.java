package cm.packagemanager.pmanager.image.ent.bo;

import cm.packagemanager.pmanager.image.utils.ImageFormat;

public interface ImageCompressBO {

    ImageFormat compress(ImageSourceBO imageSource, String imageName,String path);
    void compress(ImageSourceBO imageSource, String imageName);
}