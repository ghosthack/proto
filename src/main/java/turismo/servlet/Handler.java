package turismo.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Handler {

    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException;

}
