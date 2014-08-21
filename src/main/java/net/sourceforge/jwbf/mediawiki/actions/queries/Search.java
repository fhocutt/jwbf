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
import net.sourceforge.jwbf.mediawiki.contentRep.SearchResult;

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

  //when we start, it is the initial request
  private boolean initial = true;
  private boolean hasMoreResults = true;
  private String continueOffset = "0";

  // Constructor
  public Search(MediaWikiBot bot, String from, String prefix,
      int... namespaces) {
    super(bot);
    this.bot = bot;
    this.prefix = prefix;
    this.namespaces = namespaces;
    this.from = from;
  }




  // Three overridden abstract methods from TitleQuery


  // implementing abstract mewhod, TODO actually write this
  // Parsing results to find "continue" 
  @Override
  protected String parseHasMore(final String s) {
    // parse json for "continue":{ "continue":"-||" }, does it have it?
    // hasMoreResults = the above answer

    //does this return "sroffset": stringify the int in the json
    // continueOffset = sroffset.toString(); <-- ??? 
    return "";
  }

  /**
   * Takes the JSON response String s, parses it, and returns a list
   * of the articles as SearchResults. 
   */
  @Override
  protected ImmutableList<SearchResult> parseArticleTitles(String s) {
    //for each entry in the JSON {"query"}["search"] list, make a new SearchResult
    //add these SearchResults to a list
    //consider adding each chunk of article titles to a separate list?
//also deal with API errors in this method
    ImmutableList<SearchResult> searchResults = ImmutableList.<SearchResult>of();
    return searchResults;
  }

  //implementing abstract method, tells it whether there's a continue or not, 
  //this should be refactored eventually
  protected HttpAction prepareCollection() {
    if (initial) {
      initial = false;
//      return generateFirstRequest();
    } else {
//      return generateContinueRequest(continueOffset);
    }
//just for now so that it compiles
  RequestBuilder requestBuilder = new ApiRequestBuilder();
  return requestBuilder.buildGet();
  }



/**
 * Actual useful code to be added, possibly to RequestGenerator?

...
Get searchRequest = new ApiRequestBuilder().action("query") //
        .formatJson() //
        .paramNewContinue(mediaWikiVersion) //
        .param("list", "search") //
        .param("srsearch", searchTerm) //
        .param("srbackend", "CirrusSearch") // or LuceneSearch
// http://www.mediawiki.org/w/api.php?action=query&list=search
// &srsearch=wikipedia&srbackend=CirrusSearch&format=json&continue=-||
*/

}

/**
*JSON response format:
*{
*    "continue": {
*        "sroffset": 150,
*        "continue": "-||"
*    },
*    "query": {
*        "searchinfo": {
*            "totalhits": 153
*        },
*        "search": [
*            {
*                "ns": 0,
*                "title": "Roadmap/2012/May",
*                "snippet": "site views   Start Summer of Code 2012 students 
*[Sumana] [DONE] Prepare <span class=\"searchmatch\">tutorials</span> for Berlin 
*Hackathon 2012 [Sumana, Danielle Benoit] [DONE] Wikimedia blog",
*                "size": 8273,
*                "wordcount": 1156,
*                "timestamp": "2012-06-13T17:42:09Z"
*            }
*            ...
*        ]
*    }
*}
*
*/

