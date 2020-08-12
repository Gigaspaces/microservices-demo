package com.gigaspaces.ndemo;

import com.gigaspaces.datasource.SpaceTypeSchemaAdapter;
import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.metadata.SpaceTypeDescriptor;
import com.gigaspaces.metadata.SpaceTypeDescriptorBuilder;

import java.util.List;

public class TicketSchemaAdapter implements SpaceTypeSchemaAdapter {

    public TicketSchemaAdapter() {
    }

    @Override
    public SpaceDocument adaptEntry(SpaceDocument spaceDocument) {
        spaceDocument.setProperty("withCutlery", spaceDocument.<Integer>getProperty("withCutlery") == 1 ? Boolean.TRUE : Boolean.FALSE);
        spaceDocument.setProperty("delivery", Boolean.TRUE);
        return spaceDocument;
    }

    @Override
    public SpaceTypeDescriptor adaptTypeDescriptor(SpaceTypeDescriptor spaceTypeDescriptor) {
        return new SpaceTypeDescriptorBuilder(spaceTypeDescriptor.getTypeName())
                .idProperty("orderId", true)
                .routingProperty("routing")
                .addFixedProperty("orderId", String.class)
                .addFixedProperty("routing", Integer.class)
                .addFixedProperty("restaurantId", String.class)
                .addFixedProperty("menuItems", List.class)
                .addFixedProperty("status", String.class)
                .addFixedProperty("withCutlery", Boolean.class)
                .addFixedProperty("delivery", Boolean.class)
                .create();
    }

    @Override
    public String getTypeName() {
        return "com.gigaspaces.ndemo.model.Ticket";
    }
}
