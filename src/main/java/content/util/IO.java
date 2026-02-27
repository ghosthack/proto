package content.util;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Minimal I/O helpers.
 */
public final class IO {

    private IO() {}

    /** Read an entire InputStream into a UTF-8 String. */
    public static String readString(InputStream in) throws IOException {
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }

    /** Read the full request body as a String, respecting the declared encoding. */
    public static String readBody(HttpServletRequest req) throws IOException {
        var encoding = req.getCharacterEncoding();
        var charset = encoding != null ? Charset.forName(encoding) : StandardCharsets.UTF_8;
        return new String(req.getInputStream().readAllBytes(), charset);
    }

    /** Stream all bytes from input to output. */
    public static void transfer(InputStream in, OutputStream out) throws IOException {
        in.transferTo(out);
    }
}
