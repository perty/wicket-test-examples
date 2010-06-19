package se.crisp.wicket.examples;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

public class DropDownPage extends WebPage {

    static final String ONCHANGE_EVENT = "onchange";

    static final String FORM = "form";

    static final String DROPDOWN = "dropDown";

    static final String MESSAGE = "message";

    private Label label;

    static String[] choices = {"choice 1", "choice 2"};

    private static final Logger log = Logger.getLogger(DropDownPage.class);

    public class DropDownFormObject implements Serializable {
        private static final long serialVersionUID = 1L;

        String dropDown;

        @Override
        public String toString() {
            return dropDown == null ? "" : dropDown;
        }
    }

    public class DropDownForm extends Form<DropDownFormObject> {

        private static final long serialVersionUID = 1L;

        public DropDownForm(String id) {
            super(id);
            DropDownFormObject model = new DropDownFormObject();
            setDefaultModel(new CompoundPropertyModel<DropDownFormObject>(model));
            DropDownChoice<String> dropDownChoice = new DropDownChoice<String>(DROPDOWN, Arrays.asList(choices));
            dropDownChoice.add(createAjaxBehavior());
            add(dropDownChoice);
        }

        private AjaxFormComponentUpdatingBehavior createAjaxBehavior() {
            AjaxFormComponentUpdatingBehavior updatingBehavior = new AjaxFormComponentUpdatingBehavior(ONCHANGE_EVENT) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    DropDownFormObject modelObject = (DropDownFormObject) getDefaultModelObject();
                    log.debug(String.format("onUpdate: %s", modelObject.toString()));
                    label.setDefaultModelObject(modelObject.toString());
                    ajaxUpdate(target, label);
                }
            };
            return updatingBehavior;
        }

        @Override
        protected void onSubmit() {
            log.debug("onSubmit: " + getDefaultModelObjectAsString());
            setResponsePage(RadioGroupPage.class);
        }
    }

    protected void ajaxUpdate(AjaxRequestTarget target, Component comp) {
        target.addComponent(comp);
    }

    public DropDownPage() {
        add(new DropDownForm(FORM));
        addMessage();
    }

    private void addMessage() {
        label = new Label(MESSAGE, "");
        label.setOutputMarkupId(true);
        add(label);
    }

}
