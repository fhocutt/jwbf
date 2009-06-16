package net.sourceforge.jwbf.bots;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Vector;

import net.sourceforge.jwbf.actions.ContentProcessable;
import net.sourceforge.jwbf.actions.mediawiki.MediaWiki;
import net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version;
import net.sourceforge.jwbf.actions.mediawiki.editing.GetRevision;
import net.sourceforge.jwbf.actions.mediawiki.editing.PostDelete;
import net.sourceforge.jwbf.actions.mediawiki.editing.PostModifyContent;
import net.sourceforge.jwbf.actions.mediawiki.login.PostLogin;
import net.sourceforge.jwbf.actions.mediawiki.login.PostLoginOld;
import net.sourceforge.jwbf.actions.mediawiki.meta.GetUserinfo;
import net.sourceforge.jwbf.actions.mediawiki.meta.GetVersion;
import net.sourceforge.jwbf.actions.mediawiki.meta.Siteinfo;
import net.sourceforge.jwbf.actions.mediawiki.util.VersionException;
import net.sourceforge.jwbf.actions.util.ActionException;
import net.sourceforge.jwbf.actions.util.ProcessException;
import net.sourceforge.jwbf.bots.util.CacheHandler;
import net.sourceforge.jwbf.bots.util.JwbfException;
import net.sourceforge.jwbf.contentRep.Article;
import net.sourceforge.jwbf.contentRep.ContentAccessable;
import net.sourceforge.jwbf.contentRep.SimpleArticle;
import net.sourceforge.jwbf.contentRep.Userinfo;
import net.sourceforge.jwbf.contentRep.mediawiki.LoginData;

import org.apache.log4j.Logger;

/**
 * This class helps you to interact with each 
 * <a href="http://www.mediawiki.org" target="_blank">MediaWiki</a>. This class offers
 * a <b>basic set</b> of methods which are defined in the package net.sourceforge.jwbf.actions.mw.*
 * 
 * If you need more options, use these classes directly or you can also use the 
 * class {@link MediaWikiAdapterBot} which provides a bigger set of methods.
 *
 * How to use:
 *
 * <pre>
 * MediaWikiBot b = new MediaWikiBot(&quot;http://yourwiki.org&quot;);
 * b.login(&quot;Username&quot;, &quot;Password&quot;);
 * System.out.writeln(b.readContentOf(&quot;Main Page&quot;).getText());
 * </pre>
 * 
 * @author Thomas Stock
 * @author Tobias Knerr
 * @author Justus Bisser
 * 
 * @see MediaWikiAdapterBot
 *
 */
public class MediaWikiBot extends HttpBot implements WikiBot {


	private static Logger log = Logger.getLogger(MediaWikiBot.class);
	private LoginData login = null;

	private CacheHandler store = null;

	private Version version = null;
	private Userinfo ui = null;
	
	private boolean loginChangeUserInfo = false;
	private boolean loginChangeVersion = false;
	private boolean useEditApi = true;
	
	/**
	 * These chars are not allowed in article names.
	 */
	public static final char [] INVALID_LABEL_CHARS = "[]{}<>|".toCharArray();
	
	
	/**
	 * @param u
	 *            wikihosturl like "http://www.mediawiki.org/wiki/"
	 */
	public MediaWikiBot(final URL u) {
		super(u);

	}
	
	/**
	 * @param url
	 *            wikihosturl like "http://www.mediawiki.org/wiki/"
	 * @throws MalformedURLException
	 *            if param url does not represent a well-formed url
	 */
	
	public MediaWikiBot(final String url) throws MalformedURLException {
		super(url);
		if (!(url.endsWith(".php") || url.endsWith("/"))) {
			throw new MalformedURLException("(" + url + ") url must end with slash or .php");
		}
		setConnection(url);

	}

	/**
	 * 
	 * @param url wikihosturl like "http://www.mediawiki.org/wiki/"
	 * @param testHostReachable if true, test if host reachable
	 * @throws IOException a
	 */
	public MediaWikiBot(URL url, boolean testHostReachable) throws IOException {
		super(url);
		if (testHostReachable) {
			try {
				getPage(url.toExternalForm());
			} catch (ActionException e) {
				throw new UnknownHostException(url.toExternalForm());
			}
		}
		setConnection(url);
	}


	/**
	 * Performs a Login. 
	 *
	 * @param username
	 *            the username
	 * @param passwd
	 *            the password
	 * @param domain
	 *            login domain (Special for LDAPAuth extention to authenticate against LDAP users)
	 * @throws ActionException
	 *             on problems with http, cookies and io
	 * @see PostLogin
	 * @see PostLoginOld
	 */
	public void login(final String username, final String passwd, final String domain)
			throws ActionException {
		try {
			LoginData login = new LoginData();
			switch (getVersion()) {
			case MW1_09:
			case MW1_10:
			case MW1_11:
			case MW1_12:
				performAction(new PostLoginOld(username, passwd, domain, login));
				break;

			default:
				performAction(new PostLogin(username, passwd, domain, login));
				break;
			}

			this.login = login;
			loginChangeUserInfo = true;
			loginChangeVersion = true;
		} catch (ProcessException e) {
			throw new ActionException(e.getLocalizedMessage(), getClass());
		} catch (RuntimeException e) {
			throw new ActionException(e.getMessage(), getClass());
		}
	
	}
	/**
	 * Performs a Login. Actual old cookie login works right, because is pending
	 * on {@link #writeContent(ContentAccessable)}
	 *
	 * @param username
	 *            the username
	 * @param passwd
	 *            the password
	 * @throws ActionException
	 *             on problems with http, cookies and io
	 * @see PostLogin
	 * @see PostLoginOld
	 */
	public void login(final String username, final String passwd)
			throws ActionException {

		login(username, passwd, null);
	}
	/**
	 *
	 * @param name
	 *            of article in a mediawiki like "Main Page"
	 * @param properties {@link GetRevision} TODO Where are the properties
	 * @return a content representation of requested article, never null
	 * @throws ActionException
	 *             on problems with http, cookies and io
	 * @throws ProcessException on access problems
	 * @see GetRevision
	 */
	public synchronized Article readContent(final String name, final int properties)
			throws ActionException, ProcessException {
			return new Article(this, readData(name, properties));
	}
	/**
	 * {@inheritDoc}
	 */
	public synchronized SimpleArticle readData(final String name, final int properties)
			throws ActionException, ProcessException {

		GetRevision ac;
		if (store != null) {
			if (store.containsKey(name)) {
				ac = new GetRevision(this, name, GetRevision.TIMESTAMP);
				performAction(ac);
				
				SimpleArticle storeSa = store.get(name);
				if (log.isDebugEnabled()) {
					log.debug("stored article (" + storeSa.getTitle() + ") revid: " + storeSa.getRevisionId());
				}
				SimpleArticle liveSa = ac.getArticle();
				if (log.isDebugEnabled()) {
					log.debug("live article revid: " + liveSa.getRevisionId());
				}
				if (liveSa.getRevisionId().equals(storeSa.getRevisionId())) {

					return store.get(name);
				}
			}

			ac = new GetRevision(this, name, properties);

			performAction(ac);
			log.debug("update cache (read)");
			store.put(ac.getArticle());

		} else {
			ac = new GetRevision(this, name, properties);

			performAction(ac);
		}
		return ac.getArticle();

	}
	/**
	 * {@inheritDoc}
	 */
	public SimpleArticle readData(String name) throws ActionException,
			ProcessException {
		
		return readData(name, GetRevision.CONTENT
				| GetRevision.COMMENT | GetRevision.USER | GetRevision.TIMESTAMP);
	}
	/**
	 * 
	 * @param ch a
	 */
	public void setCacheHandler(CacheHandler ch) {
		this.store = ch;
	}

	/**
	 *
	 * @param name
	 *            of article in a mediawiki like "Main Page"
	 * @return a content representation of requested article, never null
	 * @throws ActionException
	 *             on problems with http, cookies and io
	 * @throws ProcessException on access problems
	 * @see GetRevision
	 */
	public synchronized Article readContent(final String name)
			throws ActionException, ProcessException {
		return readContent(name, GetRevision.CONTENT
				| GetRevision.COMMENT | GetRevision.USER | GetRevision.TIMESTAMP);

	}
	
	/**
	 *
	 * @param a a
	 * @throws ActionException
	 *             on problems with http, cookies and io
	 * @throws ProcessException on access problems
	 * @see PostModifyContent
	 * 
	 */
	public synchronized void writeContent(final ContentAccessable a)
			throws ActionException, ProcessException {
		if (!isLoggedIn()) {
			throw new ActionException("Please login first", getClass());
		}

		for (char invChar : INVALID_LABEL_CHARS) {
			if (a.getTitle().contains(invChar + "")) {
				throw new ActionException("Invalid character in label\"" 
						+ a.getTitle() + "\" : \"" + invChar + "\"", getClass());
			}
		}
		
			
		performAction(new PostModifyContent(this, a));
		if (store != null) {
			String label = a.getTitle();
			SimpleArticle sa;
			if (store.containsKey(label)) {
				log.debug("contains article: " + label); // TODO RM
				sa = store.get(label);
			} else {
				sa = new SimpleArticle(label);
			}
			sa.setText(a.getText());
			sa.setEditor(getUserinfo().getUsername());
			sa.setEditSummary(a.getEditSummary());
			sa.setMinorEdit(a.isMinorEdit());
//			sa.setRevisionId((Integer.parseInt(sa.getRevisionId()) + 1) + "");
			log.debug("update cache (write)");
			store.put(sa);
		}
		if (a.getText().trim().length() < 1) 
			throw new RuntimeException("Content is empty, still written");
	}
	
	/**
	 *
	 * @return true if
	 */
	public final boolean isLoggedIn() {

		if (login != null) {
			return login.isLoggedIn();
		}
		return false;

	}
	/**
	 * 
	 * @return a
	 * @throws ActionException
	 *             on problems with http, cookies and io
	 * @throws ProcessException on access problems
	 */
	public Userinfo getUserinfo() throws ActionException, ProcessException {
		log.debug("get userinfo");
		if (ui == null || loginChangeUserInfo) {
			GetUserinfo a;
			try {
				a = new GetUserinfo(getVersion());
				
				performAction(a);
				ui = a;
				loginChangeUserInfo = false;
			} catch (VersionException e) {
				e.printStackTrace();
				if (login != null && login.getUserName().length() > 0) {
					ui = new Userinfo() {
					
						public String getUsername() {
							return login.getUserName();
						}
					
						public Collection<String> getRights() {
							return new Vector<String>();
						}
					
						public Collection<String> getGroups() {
							return new Vector<String>();
						}
					};
				} else {
					ui = new Userinfo() {
						
						public String getUsername() {
							return "unknown";
						}
					
						public Collection<String> getRights() {
							return new Vector<String>();
						}
					
						public Collection<String> getGroups() {
							return new Vector<String>();
						}
					};
				}
			}
			
			
		}
		return ui;
	}
	
	
	/**
	 * @param title to delete
	 * @throws ActionException if
	 * @throws ProcessException if
	 */
	public void postDelete(String title) throws ActionException, ProcessException {
		
		performAction(new PostDelete(this, title));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized String performAction(ContentProcessable a)
			throws ActionException, ProcessException {
		return super.performAction(a);
	}

	/**
	 * 
	 * @return the
	 * @throws RuntimeException if no version was found.
	 * @see #getSiteinfo()
	 */
	public final Version getVersion() throws RuntimeException {
		if (version == null || loginChangeVersion) {
			

			try {
				GetVersion gs = new GetVersion();
				performAction(gs);

				version = gs.getVersion();
				loginChangeVersion = false;
			} catch (JwbfException e) {
				log.error(e.getClass().getName() + e.getLocalizedMessage());
				throw new RuntimeException(e.getLocalizedMessage());
			}
			log.debug("Version is: " + version.name());

		}
		return version;
	}
	
	/**
	 *
	 * @return a
	 * @throws ActionException
	 *             on problems with http, cookies and io
	 * @see Siteinfo
	 */
	public Siteinfo getSiteinfo() throws ActionException {
		
		Siteinfo gs = null;
		try {
			gs = new Siteinfo();
			performAction(gs);
		} catch (ProcessException e) {
			e.printStackTrace();
		}

		return gs;

	}
	/**
	 * 
	 * @return the
	 */
	public final boolean isEditApi() {
		return useEditApi;
	}

	/**
	 * Set to false, to force editing without the API.
	 * @param useEditApi if
	 * 
	 */
	public final void useEditApi(boolean useEditApi) {
		this.useEditApi = useEditApi;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getWikiType() {
		return MediaWiki.class.getName();
	}


}
