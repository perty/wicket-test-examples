package se.crisp.wicket.examples;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

import java.io.Serializable;

public class CheckBoxPage extends BasePage {

    static final String FORM = "form";

    static final String CHECK_BOX = "checkBox";

    static final String MESSAGE = "message";

    static final String GREETING = "Hello, clicker!";

    private Label label;

    private static final Logger log = Logger.getLogger(CheckBoxPage.class);

    public class CheckBoxFormModel implements Serializable {

        private static final long serialVersionUID = 1L;

        Boolean checkBox;

    }

    public class CheckBoxForm extends Form<CheckBoxFormModel> {

        private static final long serialVersionUID = 1L;

        public CheckBoxForm(String id) {
            super(id);
            CheckBoxFormModel model = new CheckBoxFormModel();
            setDefaultModel(new CompoundPropertyModel<CheckBoxFormModel>(model));
            add(new AjaxCheckBox(CHECK_BOX) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    log.debug(String.format("%s was clicked", getId()));
                    label.setDefaultModelObject(GREETING);
                    ajaxUpdate(target, label);
                }
            });
        }

        @Override
        protected void onSubmit() {
            log.debug("onSubmit");
            setResponsePage(DropDownPage.class);
        }
    }

    protected void ajaxUpdate(AjaxRequestTarget target, Component comp) {
        target.add(comp);
    }

    public CheckBoxPage() {
        add(new CheckBoxForm(FORM));
        addMessage();
    }

    private void addMessage() {
        label = new Label(MESSAGE, "");
        label.setOutputMarkupId(true);
        add(label);
    }

}
