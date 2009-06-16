package net.sourceforge.jwbf.live.mediawiki;

import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_09;
import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_10;
import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_11;
import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_12;
import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_13;
import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_14;
import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_15;
import net.sourceforge.jwbf.live.ArticleTest;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
/**
 * 
 * @author Thomas Stock
 *
 */
public class BasicTest extends ArticleTest {
	/**
	 * Do.
	 * @throws Exception a
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		PropertyConfigurator.configureAndWatch("test4log4j.properties",
				60 * 1000);
		
		bots.add(getMediaWikiBot(MW1_09, true));
		bots.add(getMediaWikiBot(MW1_10, true));
		bots.add(getMediaWikiBot(MW1_11, true));
		bots.add(getMediaWikiBot(MW1_12, true));
		bots.add(getMediaWikiBot(MW1_13, true));
		bots.add(getMediaWikiBot(MW1_14, true));
		bots.add(getMediaWikiBot(MW1_15, true));
	}
	/**
	 * Required for extension.
	 * @throws Exception a
	 */
	@Before
	public void doNothing() throws Exception  {
		  
	}
	
	

}
