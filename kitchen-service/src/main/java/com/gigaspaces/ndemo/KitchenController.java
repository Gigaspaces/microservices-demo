package com.gigaspaces.ndemo;

import com.gigaspaces.ndemo.model.MenuItem;
import com.gigaspaces.ndemo.model.Restaurant;
import com.j_spaces.core.client.SQLQuery;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.gigaspaces.ndemo.TracingUtils.wrap;

@RestController
public class KitchenController {

    @Autowired
    private GigaSpace gigaSpace;

    @Autowired
    private Kitchen kitchen;

    private static Logger logger = Logger.getLogger("DEBUG_YAEL_LOGGER");

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


    @PostMapping("/order/prepare")
    public void prepareOrder(@RequestBody PrepareOrderRequest request) throws Exception {
        logger.severe("%%%%%%%%% prepare requested orderId = "+request.getOrderId()+" %%%%%%%%%");
        wrap("prepare-order", () -> {
            kitchen.queue(request);
            return null;
        });
    }


}
