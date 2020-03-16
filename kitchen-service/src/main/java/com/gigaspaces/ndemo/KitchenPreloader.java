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

        Restaurant restaurant1 = new Restaurant("Sushi House", "Down town");
        gigaSpace.write(restaurant1);
        gigaSpace.write(new MenuItem("Salmon sashimi", restaurant1.getId()));
        gigaSpace.write(new MenuItem("Tuna sashimi", restaurant1.getId()));
        gigaSpace.write(new MenuItem("Philadelphia roll", restaurant1.getId()));
        gigaSpace.write(new MenuItem("Spicy tuna roll", restaurant1.getId()));

        Restaurant restaurant2 = new Restaurant("Five Guys", "Down town");
        gigaSpace.write(restaurant2);
        gigaSpace.write(new MenuItem("Hamburger", restaurant2.getId()));
        gigaSpace.write(new MenuItem("Cheeseburger", restaurant2.getId()));
        gigaSpace.write(new MenuItem("Franch fries", restaurant2.getId()));
        gigaSpace.write(new MenuItem("Onion rings", restaurant2.getId()));

        Restaurant restaurant3 = new Restaurant("Burgus", "Down town");
        gigaSpace.write(restaurant3);
        gigaSpace.write(new MenuItem("Hamburger", restaurant3.getId()));
        gigaSpace.write(new MenuItem("Cheeseburger", restaurant3.getId()));
        gigaSpace.write(new MenuItem("Franch fries", restaurant3.getId()));
        gigaSpace.write(new MenuItem("Onion rings", restaurant3.getId()));

        Restaurant restaurant4 = new Restaurant("Tony's Pizza", "West side");
        gigaSpace.write(restaurant4);
        gigaSpace.write(new MenuItem("Small pizza", restaurant4.getId()));
        gigaSpace.write(new MenuItem("Large pizza", restaurant4.getId()));
        gigaSpace.write(new MenuItem("Garlic bread", restaurant4.getId()));
        gigaSpace.write(new MenuItem("Soda", restaurant4.getId()));

        Restaurant restaurant5 = new Restaurant("Moon Sushi", "West side");
        gigaSpace.write(restaurant5);
        gigaSpace.write(new MenuItem("Salmon sashimi", restaurant5.getId()));
        gigaSpace.write(new MenuItem("Tuna sashimi", restaurant5.getId()));
        gigaSpace.write(new MenuItem("Philadelphia roll", restaurant5.getId()));
        gigaSpace.write(new MenuItem("Spicy tuna roll", restaurant5.getId()));

        Restaurant restaurant6 = new Restaurant("Frank's", "West side");
        gigaSpace.write(restaurant6);
        gigaSpace.write(new MenuItem("Hotdog", restaurant6.getId()));
        gigaSpace.write(new MenuItem("Chilly hotdog", restaurant6.getId()));
        gigaSpace.write(new MenuItem("Fries", restaurant6.getId()));
        gigaSpace.write(new MenuItem("Beer", restaurant6.getId()));

        Restaurant restaurant7 = new Restaurant("Falafel Corner", "Up town");
        gigaSpace.write(restaurant7);
        gigaSpace.write(new MenuItem("Lamb moroccan style", restaurant7.getId()));
        gigaSpace.write(new MenuItem("Spicy beef", restaurant7.getId()));
        gigaSpace.write(new MenuItem("Chicken Moroccan style", restaurant7.getId()));
        gigaSpace.write(new MenuItem("Shawarma", restaurant7.getId()));

        Restaurant restaurant8 = new Restaurant("Pizza Domino's", "Up town");
        gigaSpace.write(restaurant8);
        gigaSpace.write(new MenuItem("Small pizza", restaurant8.getId()));
        gigaSpace.write(new MenuItem("Large pizza", restaurant8.getId()));
        gigaSpace.write(new MenuItem("Garlic bread", restaurant8.getId()));
        gigaSpace.write(new MenuItem("Soda", restaurant8.getId()));

        Restaurant restaurant9 = new Restaurant("Curry Kitchen", "Up town");
        gigaSpace.write(restaurant9);
        gigaSpace.write(new MenuItem("Butter chicken", restaurant9.getId()));
        gigaSpace.write(new MenuItem("Tandoori chicken", restaurant9.getId()));
        gigaSpace.write(new MenuItem("Chicken tikka masala", restaurant9.getId()));
        gigaSpace.write(new MenuItem("Malai kofta", restaurant9.getId()));

        Restaurant restaurant10 = new Restaurant("Spoons Cafe", "Up town");
        gigaSpace.write(restaurant10);
        gigaSpace.write(new MenuItem("Omelette sandwich", restaurant10.getId()));
        gigaSpace.write(new MenuItem("Tuna sandwich", restaurant10.getId()));
        gigaSpace.write(new MenuItem("Bruschetta", restaurant10.getId()));
        gigaSpace.write(new MenuItem("Camembert pastry", restaurant10.getId()));
    }
}
