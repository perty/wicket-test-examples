package se.crisp.wicket.examples;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

class CheckBoxPageAjaxOverride extends CheckBoxPage implements Serializable {
    static final String FORGOT_TO_CALL_SET_MARKUP_ID =
            "You forgot to call setMarkupId(true) on '%s'";

    int ajaxCalled = 0;

    @Override
    protected void ajaxUpdate(AjaxRequestTarget target, Component comp) {
        super.ajaxUpdate(target, comp);
        ajaxCalled++;
        String failMessage = String.format(FORGOT_TO_CALL_SET_MARKUP_ID, comp.getId());
        assertNotNull(failMessage, comp.getMarkupId(false));
    }
}

public class CheckBoxPage_UnitTest {
    // Get this right or get "No AjaEventBehavior found ..."
    static final String CHECKBOX_EVENT = "onclick";

    private WicketTester tester;

    @Before
    public void setUp() {
        tester = new WicketTester(new WicketTestExamplesApplication());
    }

    @Test
    public void render() {
        tester.startPage(CheckBoxPage.class);
    }

    @Test
    public void whenSelectingBoxThenDisplayMessage() {

        CheckBoxPageAjaxOverride testPage = createTestPage();

        fillInTheForm();

        String actualMessage = getTheResult();
        String expectedMessage = CheckBoxPage.GREETING;

        assertOnlyTheMessageIsDisplayed(testPage, expectedMessage, actualMessage);
    }

    @Test
    public void whenSubmittingFormThenGoToDropDownPage() {
        createTestPage();

        FormTester formTester = tester.newFormTester(CheckBoxPage.FORM);
        formTester.submit();

        tester.assertRenderedPage(DropDownPage.class);
    }

    private CheckBoxPageAjaxOverride createTestPage() {
        CheckBoxPageAjaxOverride testPage = new CheckBoxPageAjaxOverride();
        tester.startPage(testPage);
        return testPage;
    }

    private FormTester fillInTheForm() {
        FormTester formTester = tester.newFormTester(CheckBoxPage.FORM);
        formTester.setValue(CheckBoxPage.CHECK_BOX, true);
        String pathToCheckBox = CheckBoxPage.FORM + ":" + CheckBoxPage.CHECK_BOX;
        tester.executeAjaxEvent(pathToCheckBox, CHECKBOX_EVENT);
        return formTester;
    }

    private String getTheResult() {
        tester.assertComponent(CheckBoxPage.MESSAGE, Label.class);
        CheckBoxPage page = (CheckBoxPage) tester.getLastRenderedPage();
        Label label = (Label) page.get(CheckBoxPage.MESSAGE);
        String actualMessage = label.getDefaultModelObjectAsString();
        return actualMessage;
    }

    private void assertOnlyTheMessageIsDisplayed(CheckBoxPageAjaxOverride testPage,
                                                 String expected, String actual) {
        assertEquals(expected, actual);
        assertEquals(1, testPage.ajaxCalled);
        tester.assertNoErrorMessage();
    }


}