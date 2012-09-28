/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.complitex.keconnection.address.strategy.building.web.edit;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.complitex.address.strategy.building.web.edit.BuildingEditComponent;
import org.complitex.dictionary.entity.DomainObject;
import org.complitex.dictionary.strategy.web.AbstractComplexAttributesPanel;
import org.complitex.dictionary.strategy.web.DomainObjectAccessUtil;
import org.complitex.dictionary.web.component.DisableAwareDropDownChoice;
import org.complitex.dictionary.web.component.DomainObjectDisableAwareRenderer;
import org.complitex.dictionary.web.component.list.AjaxRemovableListView;
import org.complitex.keconnection.address.strategy.building.KeConnectionBuildingStrategy;
import org.complitex.keconnection.address.strategy.building.entity.BuildingCode;
import org.complitex.keconnection.address.strategy.building.entity.BuildingCodeList;
import org.complitex.keconnection.address.strategy.building.entity.KeConnectionBuilding;
import org.complitex.keconnection.organization.strategy.IKeConnectionOrganizationStrategy;

import javax.ejb.EJB;
import java.util.List;

/**
 *
 * @author Artem
 */
public class KeConnectionBuildingEditComponent extends AbstractComplexAttributesPanel {

    @EJB(name = IKeConnectionOrganizationStrategy.KECONNECTION_ORGANIZATION_STRATEGY_NAME)
    private IKeConnectionOrganizationStrategy organizationStrategy;

    public KeConnectionBuildingEditComponent(String id, boolean disabled) {
        super(id, disabled);
    }

    @Override
    protected KeConnectionBuilding getDomainObject() {
        return (KeConnectionBuilding) super.getDomainObject();
    }

    @Override
    protected void init() {
        final boolean isDisabled = isDisabled();
        final boolean enabled = !isDisabled && DomainObjectAccessUtil.canEdit(
                KeConnectionBuildingStrategy.KECONNECTION_BUILDING_STRATEGY_NAME, "building", getDomainObject());

        add(new BuildingEditComponent("parentBuildingEditComponent", isDisabled) {

            @Override
            protected String getBuildingStrategyName() {
                return KeConnectionBuildingStrategy.KECONNECTION_BUILDING_STRATEGY_NAME;
            }
        });

        final KeConnectionBuilding building = getDomainObject();

        final BuildingCodeList associationList =
                building.getBuildingCodeList();
        if (building.getId() == null) { // new building
            associationList.addNew();
        }

        final List<DomainObject> allServicingOrganizations = organizationStrategy.getAllServicingOrganizations(getLocale());
        final DomainObjectDisableAwareRenderer organizationRenderer = new DomainObjectDisableAwareRenderer() {

            @Override
            public Object getDisplayValue(DomainObject object) {
                return organizationStrategy.displayDomainObject(object, getLocale());
            }
        };

        final WebMarkupContainer buildingOrganizationAssociationsContainer =
                new WebMarkupContainer("buildingOrganizationAssociationsContainer");
        buildingOrganizationAssociationsContainer.setVisible(!isDisabled || !associationList.isEmpty());
        add(buildingOrganizationAssociationsContainer);

        final WebMarkupContainer associationsUpdateContainer = new WebMarkupContainer("associationsUpdateContainer");
        associationsUpdateContainer.setOutputMarkupId(true);
        buildingOrganizationAssociationsContainer.add(associationsUpdateContainer);

        ListView<BuildingCode> associations =
                new AjaxRemovableListView<BuildingCode>("associations",
                associationList) {

                    @Override
                    protected void populateItem(ListItem<BuildingCode> item) {
                        final WebMarkupContainer fakeContainer = new WebMarkupContainer("fakeContainer");
                        item.add(fakeContainer);

                        final BuildingCode association = item.getModelObject();

                        //organization
                        IModel<DomainObject> organizationModel = new Model<DomainObject>() {

                            @Override
                            public DomainObject getObject() {
                                Long organizationId = association.getOrganizationId();
                                if (organizationId != null) {
                                    for (DomainObject o : allServicingOrganizations) {
                                        if (organizationId.equals(o.getId())) {
                                            return o;
                                        }
                                    }
                                }
                                return null;
                            }

                            @Override
                            public void setObject(DomainObject organization) {
                                association.setOrganizationId(organization != null
                                        ? organization.getId() : null);
                            }
                        };
                        //initialize model:
                        Long organizationId = association.getOrganizationId();
                        if (organizationId != null) {
                            for (DomainObject o : allServicingOrganizations) {
                                if (organizationId.equals(o.getId())) {
                                    organizationModel.setObject(o);
                                }
                            }
                        }

                        DisableAwareDropDownChoice<DomainObject> organization =
                                new DisableAwareDropDownChoice<DomainObject>("organization", organizationModel,
                                allServicingOrganizations, organizationRenderer);
                        organization.setEnabled(enabled);
                        organization.add(new AjaxFormComponentUpdatingBehavior("onchange") {

                            @Override
                            protected void onUpdate(AjaxRequestTarget target) {
                                target.add(associationsUpdateContainer);
                            }
                        });
                        item.add(organization);

                        //building code
                        IModel<String> buildingCodeModel = new PropertyModel<String>(association, "buildingCode");
                        TextField<String> buildingCode = new TextField<String>("buildingCode", buildingCodeModel);
                        buildingCode.setEnabled(enabled);
                        buildingCode.add(new AjaxFormComponentUpdatingBehavior("onblur") {

                            @Override
                            protected void onUpdate(AjaxRequestTarget target) {
                                target.add(associationsUpdateContainer);
                            }
                        });
                        item.add(buildingCode);

                        //remove link
                        addRemoveLink("removeAssociation", item, null, associationsUpdateContainer).setVisible(enabled);
                    }

                    @Override
                    protected boolean approveRemoval(ListItem<BuildingCode> item) {
                        return associationList.size() > 1;
                    }
                };
        associationsUpdateContainer.add(associations);
        AjaxLink<Void> addAssociation = new AjaxLink<Void>("addAssociation") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                associationList.addNew();
                target.add(associationsUpdateContainer);
            }
        };
        addAssociation.setVisible(enabled);
        buildingOrganizationAssociationsContainer.add(addAssociation);
    }

    public boolean isBuildingOrganizationAssociationListEmpty() {
        return getDomainObject().getBuildingCodeList().isEmpty();
    }

    public boolean isBuildingOrganizationAssociationListHasNulls() {
        return getDomainObject().getBuildingCodeList().hasNulls();
    }
}
