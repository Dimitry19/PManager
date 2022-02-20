package cm.packagemanager.pmanager.image.ent.bo;


import java.io.File;
import java.io.IOException;

public interface ImageSourceBO {

    File asFile() throws IOException;
}