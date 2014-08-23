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


import net.sourceforge.jwbf.mapper.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.List;

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
  private boolean initialReq = true;
  private boolean hasMoreResults = true;
  private String continueFrom = "0";
  private String searchterm = "";


  // Constructor
  public Search(MediaWikiBot bot, String from, String prefix,
      int... namespaces) {
    super(bot);
    this.bot = bot;
    this.prefix = prefix;
    this.namespaces = namespaces;
    this.from = from;

  } 

/**
* TODO.
* figure out json parsing
* figure out what I want to do with overloading generateRequest
* figure out what I'm doing with storing search results
* write/modify an integration test? 
* figure out how to call it?
*/

  //generates the search request as a Get HttpAction

  private HttpAction generateRequest(String searchterm, String continueFrom) {

    Get searchRequest = new ApiRequestBuilder().action("query") //
          .formatJson() //
          .paramNewContinue(bot.getVersion()) //
          .param("list", "search") //
          .param("srsearch", searchterm) //
          .param("srbackend", "CirrusSearch") // or LuceneSearch
          .param("sroffset", continueFrom)
          .buildGet();
// http://www.mediawiki.org/w/api.php?action=query&list=search
// &srsearch=wikipedia&srbackend=CirrusSearch&format=json&continue=-||
    return searchRequest;

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
  // generates a new Get request that starts from continueFrom.
  // once this is called it is no longer the initial run.
  protected HttpAction prepareCollection() {
    initialReq = false;
    return generateRequest(searchterm, continueFrom);
  }

}


//experimenting with JSON mapping...
/*

  protected void responseMap(String response) {
    JsonMapper mapper = new JsonMapper();
    SearchData searchData = mapper.get(response, SearchData.class);
    return;
  }
*/




// copying JsonMapperTest's SiteInfoData class; not sure if this is the right track 
// FIXME

class SearchData {
  private final String searchterm;

  public SearchData(String searchterm) {
    this.searchterm = searchterm;
  }

  @JsonCreator
  private static SearchData newSearchData(Map<String, Object> data) {

    Map<String, Object> query = data.get("query");
    List<Object> search = query.get("search");

    Map<String, Object> continuation = data.get("continue");
    int continueFrom = continuation.get("sroffset");
  }

  String getSearchterm() {
    return searchterm;
  }
}
/**
Method from JsonMapperTest's SiteInfoData class:
@JsonCreator
private static SiteInfoData newSiteInfoData(Map<String, Object> data) {
Map<String, Object> query = (Map<String, Object>) data.get("query");
Map<String, String> general = (Map<String, String>) query.get("general");
String mainpage = general.get("mainpage");
return new SiteInfoData(mainpage);
}
String getMainpage() {
return mainpage;
/**
*JSON response format.
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
