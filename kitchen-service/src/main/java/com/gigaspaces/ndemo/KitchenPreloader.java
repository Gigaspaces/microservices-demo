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
        Restaurant restaurant;
        restaurant = new Restaurant("Sushi House", "Down town");
        gigaSpace.write(restaurant);
        gigaSpace.write(new MenuItem("Salmon sashimi", restaurant.getId()));
        gigaSpace.write(new MenuItem("Tuna sashimi", restaurant.getId()));
        gigaSpace.write(new MenuItem("Philadelphia roll", restaurant.getId()));
        gigaSpace.write(new MenuItem("Spicy tuna roll", restaurant.getId()));

        restaurant = new Restaurant("Five Guys", "Down town");
        gigaSpace.write(restaurant);
        gigaSpace.write(new MenuItem("Hamburger", restaurant.getId()));
        gigaSpace.write(new MenuItem("Cheeseburger", restaurant.getId()));
        gigaSpace.write(new MenuItem("Franch fries", restaurant.getId()));
        gigaSpace.write(new MenuItem("Onion rings", restaurant.getId()));

        restaurant = new Restaurant("Burgus", "Down town");
        gigaSpace.write(restaurant);
        gigaSpace.write(new MenuItem("Hamburger", restaurant.getId()));
        gigaSpace.write(new MenuItem("Cheeseburger", restaurant.getId()));
        gigaSpace.write(new MenuItem("Franch fries", restaurant.getId()));
        gigaSpace.write(new MenuItem("Onion rings", restaurant.getId()));

        restaurant = new Restaurant("Tony's Pizza", "West side");
        gigaSpace.write(restaurant);
        gigaSpace.write(new MenuItem("Small pizza", restaurant.getId()));
        gigaSpace.write(new MenuItem("Large pizza", restaurant.getId()));
        gigaSpace.write(new MenuItem("Garlic bread", restaurant.getId()));
        gigaSpace.write(new MenuItem("Soda", restaurant.getId()));

        restaurant = new Restaurant("Moon Sushi", "West side");
        gigaSpace.write(restaurant);
        gigaSpace.write(new MenuItem("Salmon sashimi", restaurant.getId()));
        gigaSpace.write(new MenuItem("Tuna sashimi", restaurant.getId()));
        gigaSpace.write(new MenuItem("Philadelphia roll", restaurant.getId()));
        gigaSpace.write(new MenuItem("Spicy tuna roll", restaurant.getId()));

        restaurant = new Restaurant("Frank's", "West side");
        gigaSpace.write(restaurant);
        gigaSpace.write(new MenuItem("Hotdog", restaurant.getId()));
        gigaSpace.write(new MenuItem("Chilly hotdog", restaurant.getId()));
        gigaSpace.write(new MenuItem("Fries", restaurant.getId()));
        gigaSpace.write(new MenuItem("Beer", restaurant.getId()));

        restaurant = new Restaurant("Falafel Corner", "Up town");
        gigaSpace.write(restaurant);
        gigaSpace.write(new MenuItem("Lamb moroccan style", restaurant.getId()));
        gigaSpace.write(new MenuItem("Spicy beef", restaurant.getId()));
        gigaSpace.write(new MenuItem("Chicken Moroccan style", restaurant.getId()));
        gigaSpace.write(new MenuItem("Shawarma", restaurant.getId()));

        restaurant = new Restaurant("Pizza Domino's", "Up town");
        gigaSpace.write(restaurant);
        gigaSpace.write(new MenuItem("Small pizza", restaurant.getId()));
        gigaSpace.write(new MenuItem("Large pizza", restaurant.getId()));
        gigaSpace.write(new MenuItem("Garlic bread", restaurant.getId()));
        gigaSpace.write(new MenuItem("Soda", restaurant.getId()));

        restaurant = new Restaurant("Curry Kitchen", "Up town");
        gigaSpace.write(restaurant);
        gigaSpace.write(new MenuItem("Butter chicken", restaurant.getId()));
        gigaSpace.write(new MenuItem("Tandoori chicken", restaurant.getId()));
        gigaSpace.write(new MenuItem("Chicken tikka masala", restaurant.getId()));
        gigaSpace.write(new MenuItem("Malai kofta", restaurant.getId()));

        restaurant = new Restaurant("Spoons Cafe", "Up town");
        gigaSpace.write(restaurant);
        gigaSpace.write(new MenuItem("Omelette sandwich", restaurant.getId()));
        gigaSpace.write(new MenuItem("Tuna sandwich", restaurant.getId()));
        gigaSpace.write(new MenuItem("Bruschetta", restaurant.getId()));
        gigaSpace.write(new MenuItem("Camembert pastry", restaurant.getId()));
    }
}
