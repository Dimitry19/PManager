package cm.travelpost.tp.image.ent.bo;


import java.io.File;
import java.io.IOException;

public interface ImageSourceBO {

    File asFile() throws IOException;
}