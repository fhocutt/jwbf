package net.sourceforge.jwbf.mediawiki.actions.editing;

import com.google.common.annotations.VisibleForTesting;
import net.sourceforge.jwbf.core.actions.Post;
import net.sourceforge.jwbf.core.actions.RequestBuilder;
import net.sourceforge.jwbf.core.actions.util.HttpAction;
import net.sourceforge.jwbf.core.actions.util.ProcessException;
import net.sourceforge.jwbf.core.contentRep.Userinfo;
import net.sourceforge.jwbf.mapper.XmlElement;
import net.sourceforge.jwbf.mediawiki.ApiRequestBuilder;
import net.sourceforge.jwbf.mediawiki.MediaWiki;
import net.sourceforge.jwbf.mediawiki.actions.util.MWAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action class using the MediaWiki-API's to allow your bot to delete articles in your MediaWiki add
 * the following line to your MediaWiki's  LocalSettings.php:<br>
 * <pre>
 * $wgEnableWriteAPI = true;
 * $wgGroupPermissions['bot']['delete'] = true;
 * </pre>
 * Delete an article with
 * <pre>
 * String name = ...
 * MediaWikiBot bot = ...
 * Siteinfo si = bot.getSiteinfo();
 * Userinfo ui = bot.getUserinfo();
 * bot.performAction(new PostDelete(name, si, ui));
 * </pre>
 *
 * @author Max Gensthaler
 * @see <a href="http://www.mediawiki.org/wiki/API:Edit_-_Delete">"action=delete"</a>
 */
public class PostDelete extends MWAction {

  private static final Logger log = LoggerFactory.getLogger(PostDelete.class);

  private final String title;
  private String reason;

  private final GetApiToken tokenAction;
  private boolean delToken = true;

  /**
   * Constructs a new <code>PostDelete</code> action.
   */
  public PostDelete(Userinfo userinfo, String title) {
    this.title = title;
    if (title == null || title.length() == 0) {
      throw new IllegalArgumentException("The argument 'title' must not be null or empty");
    }
    if (!userinfo.getRights().contains("delete")) {
      throw new ProcessException("The given user doesn't have the rights to delete. " +
          "Add '$wgGroupPermissions['bot']['delete'] = true;' " +
          "to your MediaWiki's LocalSettings.php might solve this problem.");
    }
    tokenAction = new GetApiToken(GetApiToken.Intoken.DELETE, title);

  }

  /**
   * Constructs a new <code>PostDelete</code> action.
   *
   * @param title  the title of the page to delete
   * @param reason reason for the deletion (may be null) in case of a precessing exception in case
   *               of an action exception
   */
  public PostDelete(Userinfo userinfo, String title, String reason) {
    this(userinfo, title);
    this.reason = reason;
  }

  /**
   * @return the delete action
   */
  private HttpAction getSecondRequest() {
    log.trace("enter PostDelete.generateDeleteRequest(String)");
    RequestBuilder requestBuilder = new ApiRequestBuilder() //
        .action("delete") //
        .formatXml() //
        .param("title", MediaWiki.urlEncode(title)) //
        .param(tokenAction.get().urlEncodedToken()) //
        ;

    if (reason != null) {
      requestBuilder.param("reason", MediaWiki.urlEncode(reason));
    }
    Post msg = requestBuilder.buildPost();
    log.debug("delete url: \"{}\"", msg.getRequest());

    return msg;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String processReturningText(String s, HttpAction hm) {
    super.processReturningText(s, hm);

    if (delToken) {
      tokenAction.processReturningText(s, hm);
      delToken = false;
    } else {
      parseXml(s);
      setHasMoreMessages(false);
    }

    return "";
  }

  @VisibleForTesting
  void parseXml(String xml) {
    log.trace("enter PostDelete.processAllReturningText(String)");
    log.debug("Got returning text: \"{}\"", xml);
    try {
      XmlElement doc = getRootElementWithError(xml);
      if (getErrorElement(doc) == null) {
        process(doc);
      }
    } catch (IllegalArgumentException e) {
      String msg = e.getMessage();
      log.error(msg, e);
      if (xml.startsWith("unknown_action:")) {
        String eMsg = "unknown_action; Adding '$wgEnableWriteAPI = true;' to your MediaWiki's " + //
            "LocalSettings.php might remove this problem.";
        throw new ProcessException(eMsg);
      }
      throw new ProcessException(msg);
    }
  }

  /**
   * Determines if the given XML Document contains an error message which then would printed by the
   * logger.
   */
  @Override
  protected XmlElement getErrorElement(XmlElement rootXmlElement) {
    XmlElement containsError = super.getErrorElement(rootXmlElement);
    if (containsError != null) {
      log.warn("{}", containsError.getAttributeValue("info"));
      if ("inpermissiondenied".equals(containsError.getAttributeValue("code"))) {
        log.error("Adding '$wgGroupPermissions['bot']['delete'] = true;'" + //
            " to your MediaWiki's LocalSettings.php might remove this problem.");
      }
    }
    return containsError;
  }

  private void process(XmlElement rootXmlElement) {
    XmlElement elem = rootXmlElement.getChild("delete");
    if (elem != null) {
      // process reply for delete request
      String title = elem.getAttributeValue("title");
      String reason = elem.getAttributeValue("reason");
      logDeleted(title, reason);
    } else {
      log.error("Unknow reply. This is not a reply for a delete action.");
    }
  }

  @VisibleForTesting
  void logDeleted(String title, String reason) {
    log.debug("Deleted article '{}'  with reason '{}'", title, reason);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HttpAction getNextMessage() {
    if (tokenAction.hasMoreActions()) {
      setHasMoreMessages(true);
      return tokenAction.popAction();
    }
    return getSecondRequest();
  }

}
