package se.crisp.wicket.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import se.crisp.wicket.examples.HomePage;
import se.crisp.wicket.examples.RadioGroupPage;
import se.crisp.wicket.examples.WicketTestExamplesApplication;

class RadioGroupPageAjaxOverride extends RadioGroupPage {

    int ajaxCalled = 0;

    @Override
    protected void ajaxUpdate(AjaxRequestTarget target, Component comp) {
        ajaxCalled++;
        String failMessage = String.format("You forgot to call setMarkupId(true) on '%s'", comp.getId());
        assertNotNull(failMessage, comp.getMarkupId(false));
    }
}

public class RadioGroupPage_UnitTest {
    
    private WicketTester tester;

    @Before
    public void setUp() {
        tester = new WicketTester(new WicketTestExamplesApplication());
        tester.setupRequestAndResponse(true);
    }

    @Test
    public void render() {
        tester.startPage(RadioGroupPage.class);
    }

    @Test
    public void whenSelectingAnOptionThenDisplayMessage() {
        RadioGroupPageAjaxOverride testPage = createTestPage();

        fillInTheForm();

        triggerRadioGroupUpdate();

        String actualMessage = getTheResult();
        assertOnlyTheMessageIsDisplayed(testPage, "choice 2", actualMessage);
    }

    @Test
    public void whenSubmittingThenGoToHomePage() {
        createTestPage();

        FormTester formTester = fillInTheForm();

        formTester.submit();

        tester.assertRenderedPage(HomePage.class);
    }

    private FormTester fillInTheForm() {
        FormTester formTester = tester.newFormTester(RadioGroupPage.FORM);
        formTester.select(RadioGroupPage.RADIO_GROUP, 1);
        return formTester;
    }

    @SuppressWarnings("unchecked")
    private void triggerRadioGroupUpdate() {
        String path = RadioGroupPage.FORM + ":" + RadioGroupPage.RADIO_GROUP;
        RadioGroup<String> rg = (RadioGroup<String>) tester.getComponentFromLastRenderedPage(path);
        rg.onSelectionChanged();
    }

    private String getTheResult() {
        RadioGroupPage page = (RadioGroupPage) tester.getLastRenderedPage();
        tester.assertComponent(RadioGroupPage.MESSAGE, Label.class);
        Label label = (Label) page.get(RadioGroupPage.MESSAGE);
        String actualMessage = label.getDefaultModelObjectAsString();
        return actualMessage;
    }

    private void assertOnlyTheMessageIsDisplayed(RadioGroupPageAjaxOverride testPage, String expected, String actual) {
        assertEquals(expected, actual);
        assertEquals(1, testPage.ajaxCalled);
        tester.assertNoErrorMessage();
    }

    private RadioGroupPageAjaxOverride createTestPage() {
        RadioGroupPageAjaxOverride page = new RadioGroupPageAjaxOverride();
        tester.startPage(page);
        return page;
    }
}