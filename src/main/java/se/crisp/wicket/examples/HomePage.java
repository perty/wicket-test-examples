package se.crisp.wicket.examples;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;

public class HomePage extends BasePage {

    private static final long serialVersionUID = 1L;

    static final String CHECK_BOX_LINK = "checkBoxPage";

    static final String DROP_DOWN_LINK = "dropDownPage";

    static final String RADIO_GROUP_LINK = "radioGroupPage";

    public HomePage() {
        add(createLink(CHECK_BOX_LINK, CheckBoxPage.class));
        add(createLink(DROP_DOWN_LINK, DropDownPage.class));
        add(createLink(RADIO_GROUP_LINK, RadioGroupPage.class));
    }

    private Link<Page> createLink(String id, final Class<? extends Page> pageClass) {
        return new Link<Page>(id) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(pageClass);
            }

        };
    }
}
