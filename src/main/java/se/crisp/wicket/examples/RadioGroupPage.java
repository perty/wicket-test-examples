package se.crisp.wicket.examples;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModelComparator;
import org.apache.wicket.model.PropertyModel;

public class RadioGroupPage extends WebPage {

    static final String FORM = "form";

    static final String RADIO_GROUP = "radioGroup";

    static final String RADIO_REPEATER = "radioRepeater";

    static final String RADIO = "radio";

    static final String RADIO_NAME = "radioName";

    static final String MESSAGE = "message";

    static final String RADIO_EVENT = "onClick";

    private Label label;

    static String[] choices = {"choice 1", "choice 2"};

    private static final Logger log = Logger.getLogger(RadioGroupPage.class);

    class RadioGroupFormObject implements Serializable {

        private static final long serialVersionUID = 1L;

        String radio;

        public void setRadio(String radio) {
            this.radio = radio;
        }

    }

    public class RadioGroupForm extends Form<RadioGroupFormObject> {

        private static final long serialVersionUID = 1L;

        public RadioGroupForm(String id) {
            super(id);
            RadioGroupFormObject selection = new RadioGroupFormObject();
            PropertyModel<String> model = new PropertyModel<String>(selection, "radio");
            selection.setRadio("no selection");
            setDefaultModel(model);
            final RadioGroup<String> radioGroup = createRadioGroup(model);
            ListView<String> repeater = new ListView<String>(RADIO_REPEATER, Arrays.asList(choices)) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(ListItem<String> item) {
                    log.debug("populate: " + item.getDefaultModelObjectAsString());
                    item.add(new Radio<String>(RADIO, item.getModel(), radioGroup));
                    item.add(new Label(RADIO_NAME, item.getModel()));
                }
            };
            radioGroup.add(repeater);
            radioGroup.add(createAjaxBehavior());
            add(radioGroup);
        }

        @Override
        protected void onSubmit() {
            log.debug("onSubmit: " + getDefaultModelObjectAsString());
            setResponsePage(HomePage.class);
        }

        private RadioGroup<String> createRadioGroup(PropertyModel<String> model) {
            final RadioGroup<String> radioGroup = new RadioGroup<String>(RADIO_GROUP, model) {
                private static final long serialVersionUID = 1L;

                @Override
                public IModelComparator getModelComparator() {
                    return new IModelComparator() {

                        private static final long serialVersionUID = 1L;

                        @Override
                        public boolean compare(Component component, Object newObject) {
                            String newString = (String) newObject;
                            String currentString = getDefaultModelObjectAsString();
                            log.debug(String.format("compare %s and %s", currentString, newString));
                            return currentString != null && currentString.equals(newString);
                        }
                    };
                }

                @Override
                protected boolean wantOnSelectionChangedNotifications() {
                    return false;
                }

                @Override
                protected void onSelectionChanged(Object newSelection) {
                    log.debug("onSelectionChanged: " + newSelection);
                    Object modelObject = getDefaultModelObject();
                    updateLabel(modelObject);
                }

            };
            return radioGroup;
        }

        private AjaxFormChoiceComponentUpdatingBehavior createAjaxBehavior() {
            return new AjaxFormChoiceComponentUpdatingBehavior() {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    Object modelObject = getComponent().getDefaultModelObject();
                    log.debug(String.format("onUpdate: %s", modelObject));
                    updateLabel(modelObject);
                }
            };
        }
    }

    private void updateLabel(Object modelObject) {
        label.setDefaultModelObject(modelObject);
        ajaxUpdate(AjaxRequestTarget.get(), label);
    }

    protected void ajaxUpdate(AjaxRequestTarget target, Component comp) {
        log.debug("ajax update");
        if (target != null) {
            target.addComponent(comp);
        }
    }

    public RadioGroupPage() {
        add(new RadioGroupForm(FORM));
        addMessage();
    }

    private void addMessage() {
        label = new Label(MESSAGE, "");
        label.setOutputMarkupId(true);
        add(label);
    }
}
