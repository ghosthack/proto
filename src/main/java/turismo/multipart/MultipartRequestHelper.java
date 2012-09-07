package turismo.multipart;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MultipartRequestHelper {

    /** Multipart form data boundary key */
    public static final String MULTIPART_FORM_DATA_BOUNDARY = "multipart/form-data; boundary=";

    private static final String BOUNDARY_HEAD = "--";
    private static final int MULTIPART_LENGTH = MULTIPART_FORM_DATA_BOUNDARY
            .length();

    public static String extractBoundary(HttpServletRequest req) throws IOException {
        final String contentType = req.getContentType();
        if (contentType != null && contentType.length() > MULTIPART_LENGTH) {
          return BOUNDARY_HEAD + contentType.substring(MULTIPART_LENGTH);
        } else {
          throw new IOException("Unexpected content type: " + contentType);
        }
    }


}
