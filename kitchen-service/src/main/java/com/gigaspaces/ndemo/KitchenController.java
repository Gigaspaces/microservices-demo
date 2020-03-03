package com.gigaspaces.ndemo;

import com.gigaspaces.ndemo.model.MenuItem;
import com.gigaspaces.ndemo.model.Restaurant;
import com.j_spaces.core.client.SQLQuery;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@RestController
public class KitchenController {

    @Autowired
    private GigaSpace gigaSpace;


    //getMenus
    @GetMapping("/menus")
    public GetMenusResponse getMenus(@RequestParam(defaultValue = "") String region) throws Exception {
        return wrap("get-menus", () -> {
            GetMenusResponse response = new GetMenusResponse();
            Restaurant[] restaurants;
            if (region != null && region.length() > 0) {
                restaurants = gigaSpace.readMultiple(new SQLQuery<>(Restaurant.class, "region = ?").setParameter(1, region));
            } else {
                restaurants = gigaSpace.readMultiple(new SQLQuery<>(Restaurant.class, ""));
            }


            response.setResturantMenuList(Arrays.stream(restaurants).map(restaurant -> {
                RestaurantMenu restaurantMenu = new RestaurantMenu();
                restaurantMenu.setRestaurantId(restaurant.getId());
                restaurantMenu.setRestaurantName(restaurant.getName());

                HashMap<String, String> menuItemsMap = new HashMap<>();
                MenuItem[] menuItems = gigaSpace.readMultiple(new SQLQuery<>(MenuItem.class, "restaurantId = ?", restaurant.getId()));
                for (MenuItem menuItem : menuItems) {
                    menuItemsMap.put(menuItem.getId(), menuItem.getName());
                }
                restaurantMenu.setMenuItems(menuItemsMap);
                return restaurantMenu;
            }).collect(Collectors.toList()));

            return response;
        });
    }


    //prepareOrder
    @PostMapping("/order/prepare")
    public void prepareOrder(PrepareOrderRequest request){
        //TODO
    }





//    @GetMapping("/restaurant")
//    public Restaurant[] getRestaurants() throws Exception {
//        return wrap("kitchen-get-restaurant", () ->
//                gigaSpace.readMultiple(new Restaurant())
//        );
//    }
//
//    @PostMapping("/restaurant")
//    public Restaurant addRestaurant(@RequestBody CreateRestaurantRequest createRestaurantRequest) throws Exception {
//        return wrap("kitchen-add-restaurant", () -> {
//                    Restaurant restaurant = new Restaurant(createRestaurantRequest.getName(), createRestaurantRequest.getRegion());
//                    gigaSpace.write(restaurant);
//                    return restaurant;
//                }
//        );
//    }

    private <T> T wrap(String name, Callable<T> c) throws Exception {
        if (GlobalTracer.isRegistered()) {
            Tracer tracer = GlobalTracer.get();
            Span serverSpan = tracer.activeSpan();

            Span span = tracer.buildSpan(name)
                    .asChildOf(serverSpan.context())
                    .start();

            try {
                return c.call();
            } catch (Exception e) {
                span.log(e.toString());
                throw e;
            } finally {
                // Optionally finish the Span if the operation it represents
                // is logically completed at this point.
                span.finish();
            }
        } else {
            return c.call();
        }
    }


}
