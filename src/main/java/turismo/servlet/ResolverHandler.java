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

import turismo.Resolver;
import turismo.action.ActionException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ResolverHandler implements Handler {

    protected ServletContext context;

    protected Resolver resolver;

    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ActionException {
        Env.create(req, res, context);
        try {
          Runnable resolve = resolve();
          resolve.run();
        } finally {
            Env.destroy();
        }
    }

    private Runnable resolve() {
        return resolver.resolve();
    }

    public ResolverHandler(ServletContext context, Resolver resolver) {
        this.context = context;
        this.resolver = resolver;
    }

}
