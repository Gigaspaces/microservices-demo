var baseUrl = "http://localhost:8180/" // For local/dev mode
//var baseUrl = ""
var watchedOrders = {};

function clearSelect(select, keep) {
    while (select.length > keep) {
        select.remove(select.length - 1);
    }
}


function updateMenu(restaurantId, elem) {
    var restaurant = resturantMenuList.filter(function (restaurant) { return restaurant.restaurantId == restaurantId; })[0];
    clearSelect(elem, 0);
    var array = [];
    for (var key in restaurant.menuItems) {
      array.push({
        id: key,
        name: restaurant.menuItems[key]
      });
    }

    var sorted = array.sort(function(a, b) {
      return (a.name > b.name) ? 1 : ((b.name > a.name) ? -1 : 0)
    });

    sorted.forEach(function (menuItem) {
    //TODO use addOption
        var option = document.createElement("option");
        option.text = menuItem.name;
        option.value = menuItem.id;
        elem.add(option);
    });
}

function addOption(select, text, value) {
     var option = document.createElement("option");
     option.text = text;
     if (value) {
        option.value = value;
     }
     select.add(option);
}

function addWatchOrder(orderId, restaurantName, menuItems) {
    watchedOrders[orderId]="";
    var tr = $("<tr/>");
    tr.addClass("order-"+orderId);
    
    var th = $("<th/>");
    th.text(orderId);
    th.attr("scope","row");
    tr.append(th);

    var tdStatus = $("<td/>");
    tr.append(tdStatus);

    var tdRestaurantName = $("<td/>");
    tdRestaurantName.text(restaurantName);
    tr.append(tdRestaurantName);
    
    $(".watched-orders").append(tr);
}

function getOrderStatus(orderId, callback) {
    $.get(baseUrl +"orders/order/status?orderId="+orderId)
        .done(res => callback(res));
}

/// APIs
function makeOrder(elem) {
    var parent = $(elem).parents(".form-horizontal");
    var selects = parent.find("select");
    var result = parent.find(".result");
    var selectedRestaurant = selects[0].value;
    var selectedRestaurantName = selects[0].options[selects[0].selectedIndex].text;
    if (!selectedRestaurant) {
        alert("Please select a restaurant");
        return;
    }

    var selectedMenuItems = selects[1].value;
    if (!selectedMenuItems) {
        alert("Please select menu items");
        return;
    }
    var selectedMenuItemsNames = Array.from(selects[1].options).filter(o => o.selected).map(o => o.text);
    var selectedMenuItems = Array.from(selects[1].options).filter(o => o.selected).map(o => o.value);
    var request = {
        "restaurantId" : selectedRestaurant,
        "menuItemsIds": selectedMenuItems
    };
   elem.disabled = true;

     $.ajax({
        url: baseUrl + "orders/order/place",
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(request),
        success: function( response ) {
            result.html("Order with ID=["+response.orderId+"] was placed");
            elem.disabled = false;
            addWatchOrder(response.orderId, selectedRestaurantName, selectedMenuItemsNames);
        },
        error: function(e) { // if error occured
          elem.disabled = false;
          console.log(e);
          result.html("<font color=red>Failed to place order, please check console log</font>");
        }
      });
}
/// End of APIs

$(document).ready(function() {

    $(".restaurants").change(function(e) {
        var menuItemsSelect = $(this).parents(".form-horizontal").find(".menuItems")[0];
        if (menuItemsSelect) {
            updateMenu(this.value, menuItemsSelect);
        }
    });

    $.get( baseUrl + "kitchen/menus", function( data ) {

      resturantMenuList = data.resturantMenuList;
      $(".restaurants").each(function (i, select) {
        clearSelect(select, 1);
        var sorted = resturantMenuList.sort(function(a, b) {
          return (a.restaurantName > b.restaurantName) ? 1 : ((b.restaurantName > a.restaurantName) ? -1 : 0)
        });
        sorted.forEach(function (restaurantMenu) {
            addOption(select, restaurantMenu.restaurantName, restaurantMenu.restaurantId);
        });
      });
    });


    setInterval(function () {
        Object.keys(watchedOrders).forEach(function(orderId) {
            getOrderStatus(orderId, status => {
                watchedOrders[orderId] = status.status;
                // var query = ".watched-orders .order-"+orderId;
                // $(query+" td")[0].innerText = status.status;
                var tr=document.getElementsByClassName("order-"+orderId)[0];
                tr.querySelector("td").innerText=status.status;
                if (status.status == "DELIVERY_DONE") {
                    delete watchedOrders[orderId];                    
                }    
            });
        });
    }, 500);


});