/*
 * Copyright 2014 Frances Hocutt.
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
package net.sourceforge.jwbf.mediawiki.actions.queries;



import net.sourceforge.jwbf.mediawiki.actions.util.RedirectFilter;

import net.sourceforge.jwbf.core.actions.Get;
import net.sourceforge.jwbf.mediawiki.ApiRequestBuilder; 
import net.sourceforge.jwbf.core.actions.RequestBuilder;


import java.util.Iterator;
import com.google.common.annotations.Beta;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.sourceforge.jwbf.core.Optionals;
import net.sourceforge.jwbf.core.actions.util.HttpAction;
import net.sourceforge.jwbf.mediawiki.actions.util.MWAction;
import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Search extends TitleQuery<SearchResult> {

  private static final Logger log = LoggerFactory.getLogger(Search.class);

  private final String prefix;
  private final int[] namespaces;
  private final MediaWikiBot bot;
  private final String from;
  private final RedirectFilter rf;

  // implementing abstract mewhod, TODO actually write this
  @Override
  protected String parseHasMore(final String s) {

    return "";
  }



// figure out how to get it an ImmutableList of SearchResults! (or just write the method)
  @Override
  protected ImmutableList<SearchResult> parseArticleTitles(String s) {
    SearchResult sResult = new SearchResult;
    ImmutableList.Builder<SearchResult> searchResults = ImmutableList.builder();
    return searchResults;
  }



  // blank request to implement abstract method, FIXME
  @Override
  protected HttpAction prepareCollection() {

    RequestBuilder requestBuilder = new ApiRequestBuilder();
    return requestBuilder.buildGet();

  }




/**
 * Actual useful code to be added

...
Get searchRequest = new ApiRequestBuilder().action("query") //
        .formatJson() //
        .paramNewContinue(mediaWikiVersion) //
        .param("list", "search") //
        .param("srsearch", "wikipedia") //
        .param("srbackend", "CirrusSearch") // or LuceneSearch
// http://www.mediawiki.org/w/api.php?action=query&list=search
// &srsearch=wikipedia&srbackend=CirrusSearch&format=json&continue=-||
*/


}
