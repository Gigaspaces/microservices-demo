var baseUrl = "http://localhost:8180/" // For local/dev mode
//var baseUrl = ""
var watchedOrders = {};

let rests = []
let images = [...Array(10).keys()].map(id => "images/restaurants/"+id+".jpg");

function getOrderStatus(orderId, callback) {
    $.get(baseUrl +"orders/order/status?orderId="+orderId)
        .done(res => callback(res));
}
$(document).ready(function () {
    $.get( baseUrl + "kitchen/menus", function( data ) {
      rests = data.resturantMenuList.map(restaurant => {
        return {...restaurant, id: restaurant.restaurantId, name: restaurant.restaurantName};
      });
      rests = rests.sort(function(a, b) {
        return (a.name > b.name) ? 1 : ((b.name > a.name) ? -1 : 0)
      });
      rests = rests.map((rest, index) => { return {...rest, img: images[index]}; } );
      update(model);
    });

    toggleWatching(false);

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

    $("#message-box").hide();

});

// Model
const pages = {
    RESTAURANTS: 'restaurants',
    MENUS: 'menus'
};

let watchingState = true;

const initModel =() => { return {
    page: pages.RESTAURANTS,
    selectedRestaurant: 0,
    selectedMenu: [],
    selectedMenuItems: []
} };

let model = initModel();

//update

const update = (model) => {
    switch (model.page) {
        case pages.RESTAURANTS:
            $("#cards-container").html(getAllRestaurants(rests));
            $("#header-title").text("Our Restaurants");
            $("#breadcrumb-container").html("");
            $("#make-order").hide();
            return;
        case pages.MENUS:
            $("#cards-container").html(getAllMenuItemsByRestaurant(model));
            $("#header-title").text(getRestById(rests, model.selectedRestaurant).name + " menu");
            $("#breadcrumb-container").html("<span style='cursor:pointer' onClick='moveToRestaurants()'> Restaurants</span> > "+getRestById(rests, model.selectedRestaurant).name+" > Menu")
            $("#make-order").show();
            return;
        default:
            return;
    }
}

//utils

// RESTAURANT UTILS //

// getAllRestaurants : renders restaurants into html elements 
// @restList : restaurants List 
// #return   : a string which is the html of the restaurant container

const getAllRestaurants = (restList) => {
    const htmlElementsList = restList.map((restaurant) => {
        return `
    <div class="card" class="col-4" onClick="restaurantClicked('${restaurant.id}')" id="restaurant-${restaurant.id}">
        <img src="${restaurant.img}" class="card-image">
        <div class="card-body">
            <h5 class="card-title">${restaurant.name}</h5>
<!--            <p class="card-text">${restaurant.desc}</p> -->
        </div>
    </div>`;
    });

    return htmlElementsList.reduce((a, b) => a + b, "")
}

// restaurantClicked : a function which updates the dom
// when restaurant card is clicked
// @restaurantId : the restaurant id 
//

const restaurantClicked = (restaurantId) => {
    let restaurant = getRestById(rests, restaurantId);
    let array = [];
    for (var key in restaurant.menuItems) {
      array.push({
        id: key,
        name: restaurant.menuItems[key]
      });
    }

    let sorted = array.sort(function(a, b) {
      return (a.name > b.name) ? 1 : ((b.name > a.name) ? -1 : 0)
    });

    model = {
        ...model,
        page: pages.MENUS,
        selectedRestaurant: restaurantId,
        selectedMenu: sorted
    }
    update(model);
    return;
}

// getRestById: gets restaurant by id from restaurants array
// @param restArray : array of restaurants
// @param restId    : id of restaurant
// return an object which is the restaurant that was selected

const getRestById = (restArray, restId) => {
    return restArray.filter((rest) => rest.id == restId)[0];
}

const moveToRestaurants = () => {
    model = initModel();
    update(model);
}
// RESTAURANT UTILS //

const getAllMenuItemsByRestaurant = (inputModel) => {
    const htmlElementsList = inputModel.selectedMenu.map((item) => {
        if (isSelected(model, item)) {
            return `
            <div class="card col-4" style="min-width:200px;min-height:200px;text-align:center;padding:10px;padding-top:80px; background-color:#e3e3e3;" onClick="removeMenuToOrderList('${item.id}')" id="item-${item.id}">
                ${item.name}
            </div>`;
        } else {
            return `
            <div class="card col-4" style="min-width:200px;min-height:200px;text-align:center;padding:10px;padding-top:80px;" onClick="addMenuToOrderList('${item.id}')" id="item-${item.id}">
                ${item.name}
            </div>`;
        }

    });

    return htmlElementsList.reduce((a, b) => a + b, "")
}

const isSelected = (inputModel, testedItem) => {
    return inputModel.selectedMenuItems.filter((item) => item.id == testedItem.id).length > 0;
}

const addMenuToOrderList = (item) => {
    model.selectedMenuItems.push(getItemById(model.selectedMenu,item))
    model = {
        ...model,
        page: pages.MENUS,
    }
    update(model);
}

const removeMenuToOrderList = (itemId) => {
    model = {
        ...model,
        selectedMenuItems:model.selectedMenuItems.filter((filteredItem) => {
            console.log(filteredItem.id.toString() !== itemId.toString());
            return filteredItem.id.toString() !== itemId.toString()
            
        })
        
    }
    update(model);
}

const getItemById = (menuArray, itemId) => {
    return (menuArray.filter((item) => item.id == itemId)[0]);
}


//watching toggling:

const toggleWatching = (newWatchingState) => {
    if (newWatchingState) {
            $("#watch-body").show(500);
            watchingState=true;
            return;
    }
    switch(watchingState) {
        case true:
            $("#watch-body").hide(500);
            watchingState=false;
            return;
        case false:
            $("#watch-body").show(500);
            watchingState=true;
            return;
    } 
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
const makeOrder = () => {
    let restaurant = getRestById(rests, model.selectedRestaurant);
     let request = {
            "restaurantId" : model.selectedRestaurant,
            "menuItemsIds": model.selectedMenuItems.map(menuItem => menuItem.id)
        };
     $.ajax({
        url: baseUrl + "orders/order/place",
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(request),
        success: function( response ) {
            addWatchOrder(response.orderId, restaurant.name, []);
            toggleWatching(true);
            moveToRestaurants();

            $("#message-box").html("Order with ID=["+response.orderId+"] was placed");
            $("#message-box").show(700);
            setTimeout(function() {
                $("#message-box").hide(500);
            }, 4000);

        },
        error: function(e) { // if error occured
          console.log(e);
        }
      });
}

$(window).bind('beforeunload', function(){
    return 'Are you sure you want to leave?';
  });