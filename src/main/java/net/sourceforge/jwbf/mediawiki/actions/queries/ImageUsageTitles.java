/*
 * Copyright 2007 Tobias Knerr.
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
 * Tobias Knerr
 *
 */
package net.sourceforge.jwbf.mediawiki.actions.queries;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.sourceforge.jwbf.core.actions.Get;
import net.sourceforge.jwbf.core.actions.RequestBuilder;
import net.sourceforge.jwbf.core.actions.util.HttpAction;
import net.sourceforge.jwbf.mediawiki.ApiRequestBuilder;
import net.sourceforge.jwbf.mediawiki.MediaWiki;
import net.sourceforge.jwbf.mediawiki.actions.util.MWAction;
import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * action class using the MediaWiki-api's "list=imagelinks" and later imageUsage.
 *
 * @author Tobias Knerr
 * @author Thomas Stock
 * @since MediaWiki 1.9.0
 */
public class ImageUsageTitles extends TitleQuery<String> {

  private static final Logger log = LoggerFactory.getLogger(ImageUsageTitles.class);

  /**
   * constant value for the illimit-parameter. *
   */
  private static final int LIMIT = 50;

  private final MediaWikiBot bot;

  private final String imageName;
  private final int[] namespaces;
  private final VersionHandler handler;

  @Deprecated
  private static final Pattern CONTINUE_PATTERN =
      Pattern.compile("<query-continue>.*?<imageusage *iucontinue=\"([^\"]*)\" */>" +
          ".*?</query-continue>", Pattern.DOTALL | Pattern.MULTILINE);

  @Deprecated
  private static final Pattern TITLE_PATTERN =
      Pattern.compile("<iu pageid=\".*?\" ns=\".*?\" title=\"(.*?)\" />");

  /**
   * The public constructor. It will have an MediaWiki-request generated, which is then added to
   * msgs. When it is answered, the method processAllReturningText will be called (from outside this
   * class). For the parameters, see {@link ImageUsageTitles#generateRequest(String, String,
   * String)}
   */
  public ImageUsageTitles(MediaWikiBot bot, String imageName, int... namespaces) {
    super(bot);
    this.bot = bot;
    this.imageName = imageName;
    this.namespaces = namespaces;
    handler = new DefaultHandler();
  }

  public ImageUsageTitles(MediaWikiBot bot, String nextPageInfo) {
    this(bot, nextPageInfo, MediaWiki.NS_ALL);
  }

  /**
   * generates the next MediaWiki-request (GetMethod) and adds it to msgs.
   *
   * @param imageName  the title of the image, not null
   * @param namespace  the namespace(s) that will be searched for links, as a string of numbers
   *                   separated by '|'; if null, this parameter is omitted
   * @param ilcontinue the value for the ilcontinue parameter, null for the generation of the
   *                   initial request
   * @return a
   */
  private Get generateRequest(String imageName, String namespace, String ilcontinue) {
    if (ilcontinue == null) {
      return handler.generateRequest(imageName, namespace);
    } else {
      return handler.generateContinueRequest(imageName, namespace, ilcontinue);
    }
  }

  /**
   * gets the information about a follow-up page from a provided api response. If there is one, a
   * new request is added to msgs by calling generateRequest.
   *
   * @param s text for parsing
   */
  @Override
  protected String parseHasMore(final String s) {
    return handler.parseHasMore(s);
  }

  /**
   * picks the article name from a MediaWiki api response.
   *
   * @param s text for parsing
   */
  @Override
  protected ImmutableList<String> parseArticleTitles(String s) {
    return ImmutableList.copyOf(handler.parseArticleTitles(s));
  }

  @Override
  protected HttpAction prepareCollection() {
    if (hasNextPageInfo()) {
      return generateRequest(imageName, null, getNextPageInfo());
    } else {
      return generateRequest(imageName, MWAction.createNsString(namespaces), null);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object clone() throws CloneNotSupportedException {
    return new ImageUsageTitles(bot, imageName, namespaces);
  }

  private abstract class VersionHandler {
    VersionHandler() {

    }

    public abstract Get generateRequest(String imageName, String namespace);

    public abstract Get generateContinueRequest(String imageName, String namespace,
        String ilcontinue);

    public abstract String parseHasMore(final String s);

    public abstract Collection<String> parseArticleTitles(String s);
  }

  private RequestBuilder newRequestBuilder() {
    return new ApiRequestBuilder() //
        .action("query") //
        .formatXml() //
        .param("list", "imageusage") //
        .param("iulimit", LIMIT) //
        ;
  }

  private class DefaultHandler extends VersionHandler {

    @Override
    public Get generateContinueRequest(String imageName, String namespace, String ilcontinue) {
      return newRequestBuilder() //
          .param("iucontinue", MediaWiki.urlEncode(ilcontinue)) //
          .param("iutitle", MediaWiki.urlEncode(imageName)) //
          .buildGet();
    }

    @Override
    public Get generateRequest(String imageName, String namespace) {
      RequestBuilder requestBuilder = newRequestBuilder();
      requestBuilder.param("iutitle", MediaWiki.urlEncode(imageName));

      if (!Strings.isNullOrEmpty(namespace)) {
        requestBuilder.param("iunamespace", MediaWiki.urlEncode(namespace));
      }
      return requestBuilder.buildGet();

    }

    @Override
    public Collection<String> parseArticleTitles(String s) {
      Collection<String> titleCollection = Lists.newArrayList();
      Matcher m = TITLE_PATTERN.matcher(s);
      while (m.find()) {
        titleCollection.add(m.group(1));
      }
      return titleCollection;
    }

    @Override
    public String parseHasMore(String s) {
      Matcher m = CONTINUE_PATTERN.matcher(s);
      if (m.find()) {
        return m.group(1);
      } else {
        return "";
      }

    }

  }

}
