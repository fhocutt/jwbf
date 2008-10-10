/**
 * 
 */
package net.sourceforge.jwbf.live;

import net.sourceforge.jwbf.LiveTestFather;
import net.sourceforge.jwbf.actions.mw.util.ActionException;
import net.sourceforge.jwbf.actions.mw.util.ProcessException;
import net.sourceforge.jwbf.actions.mw.util.VersionException;
import net.sourceforge.jwbf.bots.MediaWikiBot;
import net.sourceforge.jwbf.contentRep.mw.ContentAccessable;
import net.sourceforge.jwbf.contentRep.mw.SimpleArticle;
import net.sourceforge.jwbf.contentRep.mw.Version;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Thomas
 *
 */
public class DeleteTest extends LiveTestFather {
	private MediaWikiBot bot = null;
	private static final int COUNT = 5;
	@BeforeClass
	public static void setUp() throws Exception {
		PropertyConfigurator.configureAndWatch("test4log4j.properties",
				60 * 1000);
	}
	private final void prepare(MediaWikiBot bot) throws Exception {
		SimpleArticle a = new SimpleArticle("Delete", "0");
		
		for (int i = 0; i < COUNT; i++) {
			a.setLabel("Delete " + i);
			bot.writeContent(a);
		}
	}
	
	private final void delete(MediaWikiBot bot) throws ActionException, ProcessException {
		
		for (int i = 0; i < COUNT + 1; i++) {
			bot.postDelete("Delete " + i);
		}
	}
	
	private final void test(MediaWikiBot bot) throws ActionException, ProcessException {
	
		for (int i = 0; i < COUNT + 1; i++) {
			ContentAccessable ca = bot.readContent("Delete " + i);
			Assert.assertNull(ca);
		}
	}
	
	
	/**
	 * Test category read. Test category must have more then 50 members.
	 * @throws Exception a
	 */
	@Test(expected=VersionException.class)
	public final void deleteWikiMW1_09() throws Exception {
		
		bot = new MediaWikiBot(getValue("wikiMW1_09_url"));
		bot.login(getValue("wikiMW1_09_user"), getValue("wikiMW1_09_pass"));
	
		prepare(bot);
		delete(bot);
		test(bot);
		
		Assert.assertTrue( "Wrong Wiki Version " + bot.getVersion() , Version.MW1_09.equals(bot.getVersion()));
	}
	/**
	 * Test category read. Test category must have more then 50 members.
	 * @throws Exception a
	 */
	@Test(expected=VersionException.class)
	public final void deleteWikiMW1_10() throws Exception {
		
		bot = new MediaWikiBot(getValue("wikiMW1_10_url"));
		bot.login(getValue("wikiMW1_10_user"), getValue("wikiMW1_10_pass"));
		
		prepare(bot);
		delete(bot);
		test(bot);
		
		Assert.assertTrue( "Wrong Wiki Version " + bot.getVersion() , Version.MW1_10.equals(bot.getVersion()));
	}
	
	/**
	 * Test category read. Test category must have more then 50 members.
	 * @throws Exception a
	 */
	@Test(expected=VersionException.class)
	public final void deleteWikiMW1_11() throws Exception {
		
		bot = new MediaWikiBot(getValue("wikiMW1_11_url"));
		bot.login(getValue("wikiMW1_11_user"), getValue("wikiMW1_11_pass"));
		
		prepare(bot);
		delete(bot);
		test(bot);
		
		Assert.assertTrue( "Wrong Wiki Version " + bot.getVersion() , Version.MW1_11.equals(bot.getVersion()));
	}
	
	/**
	 * Test category read. Test category must have more then 50 members.
	 * @throws Exception a
	 */
	@Test
	public final void deleteWikiMW1_12() throws Exception {
		
		bot = new MediaWikiBot(getValue("wikiMW1_12_url"));
		bot.login(getValue("wikiMW1_12_user"), getValue("wikiMW1_12_pass"));
		
		prepare(bot);
		delete(bot);
		test(bot);
		
		Assert.assertTrue( "Wrong Wiki Version " + bot.getVersion() , Version.MW1_12.equals(bot.getVersion()));
	}
	/**
	 * Test category read. Test category must have more then 50 members.
	 * @throws Exception a
	 */
	@Test
	public final void deleteWikiMW1_13() throws Exception {
		bot = new MediaWikiBot(getValue("wikiMW1_13_url"));
		bot.login(getValue("wikiMW1_13_user"), getValue("wikiMW1_13_pass"));
		
		prepare(bot);
		delete(bot);
		test(bot);
		
		Assert.assertTrue( "Wrong Wiki Version " + bot.getVersion() , Version.MW1_13.equals(bot.getVersion()));
	}
	
	
}
