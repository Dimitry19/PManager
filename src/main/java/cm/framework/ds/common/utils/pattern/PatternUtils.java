package cm.framework.ds.common.utils.pattern;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Component
public class PatternUtils {

    final Pattern sharedUrlPattern = Pattern.compile("^/services/ws/announce/announce/id=[0-9]+&source=OTHER&share=[a-zA-Z]+$", Pattern.CASE_INSENSITIVE);

    public  boolean isShareUrl(final String input) {

        final Matcher matcher = sharedUrlPattern.matcher(input);
        return matcher.matches();

    }
}
