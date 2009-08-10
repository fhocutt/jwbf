package net.sourceforge.jwbf.live.mediawiki;

import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_09;
import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_10;
import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_11;
import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_12;
import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_13;
import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_14;
import static net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version.MW1_15;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import net.sourceforge.jwbf.LiveTestFather;
import net.sourceforge.jwbf.actions.mediawiki.util.VersionException;
import net.sourceforge.jwbf.bots.MediaWikiBot;
import net.sourceforge.jwbf.contentRep.Article;

import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class ArticleTest extends LiveTestFather {

	protected static Collection<MediaWikiBot> bots = new Vector<MediaWikiBot>();

	public ArticleTest() {
	}

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
	 * 
	 * @throws Exception a
	 */
	@Test
	public final void readWriteDelete() throws Exception {
		
		for (MediaWikiBot bot : bots) {

			String title = "z" + getRandomAlph(6); // create random title
			String user = bot.getUserinfo().getUsername();
			Article a = new Article(bot, title);
			for (int i = 0; i <= 2; i++) {
				String editSum = getRandomAlph(6); // create random edit sum
				a = new Article(bot, title); // create new article with given title
				a.setText(getRandom(42)); // set random text
				Date saveDate = new Date(System.currentTimeMillis());
				a.save(editSum); // save article a with given comment

				Article b = new Article(bot, title); // create new article b
				assertEquals(a.getTitle(), b.getTitle()); // compare title, must work -- see constructor
				assertEquals("text compair fails ", a.getText(), b.getText()); // force bot to load this from wiki
				assertEquals(user, b.getEditor());
				if (i > 1) {
					assertEquals(editSum, b.getEditSummary());
				}
				assertEquals(saveDate.getTime(), b.getEditTimestamp().getTime(), 5000); // max. 5 seconds delta
				
					

			}
			try {
				a.delete(); // clean up
			} catch (VersionException e) {
				e.printStackTrace();
			}

		}
		
	}
	
	/**
	 * 
	 * @throws Exception a
	 */
	@Test
	public final void meta() throws Exception {
		
		for (MediaWikiBot bot : bots) {

		
				String title = "z" + getRandomAlph(6);
				String user = bot.getUserinfo().getUsername();
				String editSum = getRandomAlph(6);
				Article a = new Article(bot, title);
				a.setText(getRandom(42));
				a.setMinorEdit(false);
				
				Date saveDate = new Date(System.currentTimeMillis());
				a.save(editSum); // save article a
				String revIdA = a.getRevisionId();
				
				Article b = new Article(bot, title);
				assertEquals(a.getTitle(), b.getTitle());
				assertEquals(a.getText(), b.getText());
				assertEquals(a.isMinorEdit(), b.isMinorEdit()); // because false is default value
				assertEquals(user, b.getEditor());
				assertEquals(editSum, b.getEditSummary());
				assertEquals(saveDate.getTime(), b.getEditTimestamp().getTime(), 5000); // max. 5 seconds delta
				
				
				
				a.setMinorEdit(true);
				a.save(); // do nothing because no content change
				String revIdAp = a.getRevisionId();
				assertEquals("no change " + bot.getWikiType(), revIdA, revIdAp);
				a.addText(getRandom(48));
				a.save();
				String revIdApp = a.getRevisionId();
				assertNotSame("change expected " + bot.getWikiType(), revIdA, revIdApp);
				assertEquals("minor edit @ " + bot.getWikiType(), a.isMinorEdit(), b.isMinorEdit());
				
				try {
					a.delete(); // clean up
				} catch (VersionException e) {
					e.printStackTrace();
				}
					

			

		}
		
	}
	
	@Test
	public final void articleTest() throws Exception {
		System.out.println("-- > Begin articleTest");
		// get a MediaWikiBot
		MediaWikiBot bot = bots.iterator().next();
		// create new article on this wikibot
		Article a = new Article(bot, "Test");
		a.setText("a");
		final String aText = a.getText();
		// save content
		a.save();
		assertFalse(a.isMinorEdit());
		final String firstEdit = a.getRevisionId();
		a.setMinorEdit(true);
		a.save("comment");
		final String secondEdit = a.getRevisionId();
		assertEquals("same rev ID", firstEdit, secondEdit);
		a.addText(getRandom(16));
		String aaText = a.getText();
		a.save();
		final String thirdEdit = a.getRevisionId();
		assertTrue(a.isMinorEdit());
		assertFalse("text should be differ:\n" + aaText + "\n" + aText , aaText.equals(aText));
		assertTrue("dif rev ID, both: " + thirdEdit
				, Integer.parseInt(firstEdit) != Integer.parseInt(thirdEdit));
		
		System.out.println("-- > End articleTest");
	}
		
	
}