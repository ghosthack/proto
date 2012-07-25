/*
 * Copyright (c) 2011 Adrian Fernandez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package turismo.servlet;

import turismo.Routes;
import turismo.action.ActionException;
import turismo.util.ClassForName.ClassForNameException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static turismo.util.ClassForName.createInstance;


public class BaseHandler implements Handler {

    protected Routes routes;

    protected ServletContext context;

    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Env.create(req, res, context);
        try {
            final Runnable action = routes.getResolver().resolve();
            action.run();
        } catch (ActionException e) {
            throw new ServletException(e);
        } finally {
            Env.destroy();
        }
    }

    public BaseHandler(ServletContext context, String routesParam) throws ServletException {
        this.context = context;
        try {
            routes = createInstance(routesParam, Routes.class);
        } catch (ClassForNameException e) {
            throw new ServletException(e);
        }
    }

}
