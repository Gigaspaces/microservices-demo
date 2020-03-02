package com.gigaspaces.ndemo;

import com.gigaspaces.ndemo.model.MenuItem;
import com.gigaspaces.ndemo.model.Restaurant;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class KitchenPreloader {

    @Autowired
    private GigaSpace gigaSpace;

    @PostConstruct
    public void preload() {
        List<String> restaurantsIds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Restaurant restaurant = new Restaurant("Restaurant " + i, "region " + (i % 2));
            gigaSpace.write(restaurant);
            System.out.println(restaurant);
            restaurantsIds.add(restaurant.getId());
        }


        for (int i = 0; i < restaurantsIds.size(); i++) {
            for (int j = 0; j < 10; j++) {
                gigaSpace.write(new MenuItem("menuitem" + j, restaurantsIds.get(i)));
            }
        }
    }

}
