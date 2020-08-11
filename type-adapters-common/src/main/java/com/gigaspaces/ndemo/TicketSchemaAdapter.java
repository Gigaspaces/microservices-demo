package com.gigaspaces.ndemo;

import com.gigaspaces.datasource.SpaceTypeSchemaAdapter;
import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.metadata.SpaceTypeDescriptor;
import com.gigaspaces.metadata.SpaceTypeDescriptorBuilder;
import com.gigaspaces.order.model.OrderStatus;

import java.util.List;

public class TicketSchemaAdapter implements SpaceTypeSchemaAdapter {
    @Override
    public SpaceDocument adaptEntry(SpaceDocument spaceDocument) {
        spaceDocument.setProperty("withCutlery", Integer.valueOf(spaceDocument.<String>getProperty("withCutlery")) == 1 ? Boolean.TRUE : Boolean.FALSE);
        spaceDocument.setProperty("delivery", Boolean.TRUE);
        return spaceDocument;
    }

    @Override
    public SpaceTypeDescriptor adaptTypeDescriptor(SpaceTypeDescriptor spaceTypeDescriptor) {
        return new SpaceTypeDescriptorBuilder(spaceTypeDescriptor.getTypeName())
                .idProperty("orderId", true)
                .addFixedProperty("orderId", String.class)
                .addFixedProperty("RestaurantId", String.class)
                .addFixedProperty("menuItems", List.class)
                .addFixedProperty("status", OrderStatus.class)
                .addFixedProperty("withCutlery", Boolean.class)
                .addFixedProperty("delivery", Boolean.class)
                .create();
    }

    @Override
    public String getTypeName() {
        return "com.gigaspaces.ndemo.model.Ticket";
    }
}
