package cm.packagemanager.pmanager.image.ent.bo;

import org.springframework.stereotype.Component;

@Component("imageFormatBO")
public interface ImageFormatBO {

    int width();

    int height();

    float compression();
}
