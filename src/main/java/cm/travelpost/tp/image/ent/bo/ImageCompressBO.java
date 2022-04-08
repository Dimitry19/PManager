package cm.travelpost.tp.image.ent.bo;

import cm.travelpost.tp.image.utils.ImageFormat;

public interface ImageCompressBO {

    ImageFormat compress(ImageSourceBO imageSource, String imageName,String path);
    void compress(ImageSourceBO imageSource, String imageName);
}