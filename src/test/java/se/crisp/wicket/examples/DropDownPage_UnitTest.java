package se.crisp.wicket.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import se.crisp.wicket.examples.CheckBoxPage;
import se.crisp.wicket.examples.DropDownPage;
import se.crisp.wicket.examples.RadioGroupPage;
import se.crisp.wicket.examples.WicketTestExamplesApplication;

class DropDownPageAjaxOverride extends DropDownPage {

    int ajaxCalled = 0;

    @Override
    protected void ajaxUpdate(AjaxRequestTarget target, Component comp) {
        ajaxCalled++;
        String failMessage = String.format("You forgot to call setMarkupId(true) on '%s'", comp.getId());
        assertNotNull(failMessage, comp.getMarkupId(false));
    }
}

public class DropDownPage_UnitTest {
    
    private WicketTester tester;

    @Before
    public void setUp() {
        tester = new WicketTester(new WicketTestExamplesApplication());
    }

    @Test
    public void render() {
        tester.startPage(DropDownPage.class);
    }

    @Test
    public void whenSelectingFromDropDownShouldDisplayMessage() {
        DropDownPageAjaxOverride testPage = createTestPage();

        fillInTheForm();

        String actualMessage = getTheResult();
        String expectedMessage = DropDownPage.choices[0];

        assertOnlyTheMessageIsDisplayed(testPage, expectedMessage, actualMessage);
    }

    @Test
    public void whenSubmittingTheFormThenGoToRadioGroupPage() {
        createTestPage();

        FormTester formTester = tester.newFormTester(DropDownPage.FORM);
        formTester.submit();

        tester.assertRenderedPage(RadioGroupPage.class);
    }

    private DropDownPageAjaxOverride createTestPage() {
        DropDownPageAjaxOverride page = new DropDownPageAjaxOverride();
        tester.startPage(page);
        return page;
    }

    private FormTester fillInTheForm() {
        FormTester formTester = tester.newFormTester(DropDownPage.FORM);
        formTester.select(DropDownPage.DROPDOWN, 0);
        tester.executeAjaxEvent(DropDownPage.FORM + ":" + DropDownPage.DROPDOWN, DropDownPage.ONCHANGE_EVENT);
        return formTester;
    }

    private String getTheResult() {
        DropDownPage page = (DropDownPage) tester.getLastRenderedPage();
        tester.assertComponent(DropDownPage.MESSAGE, Label.class);
        Label label = (Label) page.get(CheckBoxPage.MESSAGE);
        String actualMessage = label.getDefaultModelObjectAsString();
        return actualMessage;
    }

    private void assertOnlyTheMessageIsDisplayed(DropDownPageAjaxOverride testPage, String expected, String actual) {
        assertEquals(expected, actual);
        assertEquals(1, testPage.ajaxCalled);
        tester.assertNoErrorMessage();
    }
}