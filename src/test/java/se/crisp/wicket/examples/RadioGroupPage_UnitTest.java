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

class RadioGroupPageAjaxOverride extends RadioGroupPage {

    static final String FORGOT_TO_CALL_SET_MARKUP_ID = "You forgot to call setMarkupId(true) on '%s'";
    int ajaxCalled = 0;

    @Override
    protected void ajaxUpdate(AjaxRequestTarget target, Component comp) {
    	super.ajaxUpdate(target, comp);
        ajaxCalled++;
        String failMessage = String.format(FORGOT_TO_CALL_SET_MARKUP_ID, comp.getId());
        assertNotNull(failMessage, comp.getMarkupId(false));
    }
}

public class RadioGroupPage_UnitTest {

    static final String CHOICE_2_MESSAGE = "choice 2";
    static final int CHOICE_2_INDEX = 1;
    private WicketTester tester;

    @Before
    public void setUp() {
        tester = new WicketTester(new WicketTestExamplesApplication());
    }

    @Test
    public void render() {
        tester.startPage(RadioGroupPage.class);
    }

    @Test
    public void whenSelectingAnOptionThenDisplayMessage() {
        RadioGroupPageAjaxOverride testPage = createTestPage();

        fillInTheForm(CHOICE_2_INDEX);

        triggerRadioGroupUpdate();

        String actualMessage = getTheResult();
        assertOnlyTheMessageIsDisplayed(testPage, CHOICE_2_MESSAGE, actualMessage);
    }

    @Test
    public void whenSubmittingThenGoToHomePage() {
        createTestPage();

        FormTester formTester = fillInTheForm(CHOICE_2_INDEX);

        formTester.submit();

        tester.assertRenderedPage(HomePage.class);
    }

    private FormTester fillInTheForm(int choiceIndex) {
        FormTester formTester = tester.newFormTester(RadioGroupPage.FORM);
        formTester.select(RadioGroupPage.RADIO_GROUP, choiceIndex);
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
        return label.getDefaultModelObjectAsString();
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