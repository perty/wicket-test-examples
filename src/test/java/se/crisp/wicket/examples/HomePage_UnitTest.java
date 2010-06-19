package se.crisp.wicket.examples;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import se.crisp.wicket.examples.CheckBoxPage;
import se.crisp.wicket.examples.DropDownPage;
import se.crisp.wicket.examples.HomePage;
import se.crisp.wicket.examples.RadioGroupPage;
import se.crisp.wicket.examples.WicketTestExamplesApplication;

/**
 * Simple test using the WicketTester
 */
public class HomePage_UnitTest
{
	private WicketTester tester;

    @Before
	public void setUp()
	{
		tester = new WicketTester(new WicketTestExamplesApplication());
	}

    @Test
    public void render()
	{
		//start and render the test page
		tester.startPage(HomePage.class);

		//assert rendered page class
		tester.assertRenderedPage(HomePage.class);
	}

    @Test
    public void whenClickingOnCheckBoxLinkThenGoToCheckBoxPage() {
        tester.startPage(HomePage.class);

        tester.clickLink(HomePage.CHECK_BOX_LINK);

        tester.assertRenderedPage(CheckBoxPage.class);
    }

    @Test
    public void whenClickingOnDropDownLinkThenGotoDropDownPage() {
        tester.startPage(HomePage.class);

        tester.clickLink(HomePage.DROP_DOWN_LINK);

        tester.assertRenderedPage(DropDownPage.class);
    }

    @Test
    public void whenClickingOnRadioGroupLinkThenGotoRadioGroupPage() {
        tester.startPage(HomePage.class);

        tester.clickLink(HomePage.RADIO_GROUP_LINK);

        tester.assertRenderedPage(RadioGroupPage.class);
    }
}
