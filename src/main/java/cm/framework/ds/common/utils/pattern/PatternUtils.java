package cm.framework.ds.common.utils.pattern;

import cm.travelpost.tp.announce.enums.Source;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PatternUtils {

    final Pattern sharedUrlPattern = Pattern.compile("^/services/ws/announce/announce?id=[0-9]+&source="+ Source.SHARE, Pattern.CASE_INSENSITIVE);

    public  boolean isShareUrl(final String input) {

        final Matcher matcher = sharedUrlPattern.matcher(input);
        return matcher.matches();

    }
}
