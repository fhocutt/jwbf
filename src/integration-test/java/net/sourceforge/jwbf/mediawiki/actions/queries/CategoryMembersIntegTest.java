package net.sourceforge.jwbf.mediawiki.actions.queries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableList;
import net.sourceforge.jwbf.GAssert;
import net.sourceforge.jwbf.mediawiki.ApiMatcherBuilder;
import net.sourceforge.jwbf.mediawiki.MediaWiki;
import net.sourceforge.jwbf.mediawiki.MocoIntegTest;
import net.sourceforge.jwbf.mediawiki.contentRep.CategoryItem;
import org.junit.Test;

public class CategoryMembersIntegTest extends MocoIntegTest {

  public CategoryMembersIntegTest(MediaWiki.Version version) {
    super(version);
  }

  ApiMatcherBuilder newBaseMatcher() {
    return ApiMatcherBuilder.of() //
        .param("action", "query")
        .param("cmlimit", "50") //
            //.param("cmnamespace", "0") //
        .param("format", "xml") //
        .param("cmtitle", "Category:TestCat") //
        .param("list", "categorymembers") //
        // .paramNewContinue(version()) //
        ;
  }

  @Test
  public void test() {
    // GIVEN
    server.request(newBaseMatcher().build()).response(mwFileOf(version(), "category0.xml"));
    server.request(newBaseMatcher().param("cmcontinue", "token|to1|") //
        .build())
        .response(mwFileOf(version(), "category1.xml"));
    server.request(newBaseMatcher().param("cmcontinue", "token|to|2") //
        .build())
        .response(mwFileOf(version(), "category2.xml"));

    // WHEN
    CategoryMembersFull testee = new CategoryMembersFull(bot(), "TestCat");
    ImmutableList<CategoryItem> actual = CategoryMembersLiveIntegTest
        .copyWithoutDuplicatesOf(testee, 3);

    // THEN
    ImmutableList<CategoryItem> expected = ImmutableList.of( //
        new CategoryItem("CategoryTest0", MediaWiki.NS_MAIN, 1000), //
        new CategoryItem("CategoryTest1", MediaWiki.NS_MAIN, 1001), //
        new CategoryItem("CategoryTest10", MediaWiki.NS_MAIN, 1002) //
    );
    GAssert.assertEquals(expected, actual);
  }

  @Test
  public void testWithoutPermission() {
    // GIVEN
    server.request(newBaseMatcher().build()).response(mwFileOf(version(), "readapidenied.xml"));
    CategoryMembersFull testee = new CategoryMembersFull(bot(), "TestCat");

    // WHEN
    try {
      CategoryMembersLiveIntegTest.copyWithoutDuplicatesOf(testee, 3);
      fail();
    } catch (IllegalStateException e) {
      // THEN
      assertEquals("You need read permission to use this module", e.getMessage());
    }
  }

  @Test
  public void doFail() {
    // GIVEN
    // nothing
    try {
      // WHEN
      CategoryMembersLiveIntegTest
          .copyWithoutDuplicatesOf(new CategoryMembersFull(bot(), "TestCat"), 3);
      fail();
    } catch (IllegalStateException e) {
      // THEN
      GAssert.assertStartsWith("invalid status: HTTP", e.getMessage());
    }
  }
}
