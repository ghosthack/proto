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

package turismo.action.behavior;

import java.io.IOException;

import turismo.action.ActionException;
import turismo.servlet.Env;

public final class StringPrinter {

    public void print(String string) {
        try {
            Env.res().getWriter().write(string);
        } catch (IOException e) {
            throw new ActionException(e);
        }
    }

}
