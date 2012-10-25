package org.complitex.keconnection.heatmeter.web.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.complitex.dictionary.util.DateUtil;
import org.complitex.dictionary.web.component.DatePicker;
import org.complitex.dictionary.web.component.EnumDropDownChoice;
import org.complitex.keconnection.heatmeter.entity.Heatmeter;
import org.complitex.keconnection.heatmeter.entity.HeatmeterConsumption;
import org.complitex.keconnection.heatmeter.entity.HeatmeterConsumptionStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.complitex.dictionary.util.DateUtil.getFirstDayOfCurrentMonth;

/**
 * @author Anatoly A. Ivanov java@inheaven.ru
 *         Date: 25.10.12 14:55
 */
public class HeatmeterConsumptionPanel extends AbstractHeatmeterEditPanel {
    public HeatmeterConsumptionPanel(String id, final IModel<Heatmeter> model, final IModel<Date> operatingMonthModel) {
        super(id, model, operatingMonthModel);

        setOutputMarkupId(true);

        final ListView<HeatmeterConsumption> listView = new ListView<HeatmeterConsumption>("list_view",
                new LoadableDetachableModel<List<HeatmeterConsumption>>() {
                    @Override
                    protected List<HeatmeterConsumption> load() {
                        List<HeatmeterConsumption> list = new ArrayList<>();

                        for (HeatmeterConsumption c : model.getObject().getConsumptions()){
                            if (DateUtil.isSameMonth(c.getOperatingMonth(), operatingMonthModel.getObject())){
                                list.add(c);
                            }
                        }

                        return list;
                    }
                }) {
            @Override
            protected void populateItem(ListItem<HeatmeterConsumption> item) {
                HeatmeterConsumption consumption = item.getModelObject();

                item.add(new DatePicker<>("readoutDate", new PropertyModel<>(consumption, "readoutDate")));
                item.add(new TextField<>("consumption", new PropertyModel<>(consumption, "consumption")));
                item.add(new TextField<>("consumption1", new PropertyModel<>(consumption, "consumption1")));
                item.add(new TextField<>("consumption2", new PropertyModel<>(consumption, "consumption2")));
                item.add(new TextField<>("consumption3", new PropertyModel<>(consumption, "consumption3")));
                item.add(new EnumDropDownChoice<>("status", HeatmeterConsumptionStatus.class,
                        new PropertyModel<HeatmeterConsumptionStatus>(consumption, "status"), false).setRequired(true));

                item.visitChildren(new IVisitor<Component, Object>() {
                    @Override
                    public void component(Component object, IVisit<Object> visit) {
                        object.setEnabled(isCurrentOperationMonth());
                        visit.dontGoDeeper();
                    }
                });
            }
        };
        add(listView);

        add(new WebMarkupContainer("header"){
            @Override
            public boolean isVisible() {
                return !listView.getModelObject().isEmpty();
            }
        });

        add(new AjaxSubmitLink("add") {
            @Override
            public boolean isVisible() {
                return isCurrentOperationMonth();
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                model.getObject().getConsumptions().add(new HeatmeterConsumption(getFirstDayOfCurrentMonth()));

                target.add(HeatmeterConsumptionPanel.this);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });

        add(new AjaxSubmitLink("remove") {
            @Override
            public boolean isVisible() {
                return isCurrentOperationMonth() && !listView.getModelObject().isEmpty();
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                List<HeatmeterConsumption> list = listView.getModelObject();
                model.getObject().getConsumptions().remove(list.size() - 1);
                listView.detachModels();

                target.add(HeatmeterConsumptionPanel.this);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });
    }
}
