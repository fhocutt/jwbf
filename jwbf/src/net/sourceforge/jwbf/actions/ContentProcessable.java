/*
 * Copyright 2007 Thomas Stock.
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
 * 
 * Contributors:
 * 
 */
package net.sourceforge.jwbf.actions;


import net.sourceforge.jwbf.actions.util.CookieException;
import net.sourceforge.jwbf.actions.util.HttpAction;
import net.sourceforge.jwbf.actions.util.ProcessException;

import org.apache.commons.httpclient.Cookie;
/**
 * 
 * @author Thomas Stock
 *
 */
public interface ContentProcessable {

	/**
	 * 
	 * @param cs a
	 * @param hm a
	 * @throws CookieException on problems with cookies
	 */
	void validateReturningCookies(final Cookie[] cs, HttpAction hm) throws CookieException;

	/**
	 * 
	 * @param s the returning text
	 * @param hm a
	 * @return the retruning text or a modification of it
	 * @throws ProcessException on internal problems of implementing class
	 */
	String processReturningText(final String s, HttpAction hm) throws ProcessException;
	
	/**
	 * @return the of messages in this action
	 * 
	 */
	HttpAction getNextMessage();

	/**xt
	 * 
	 * @return
	 */
	boolean hasMoreMessages();

}
