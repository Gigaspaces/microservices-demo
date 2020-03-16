const rests = [
    {
        id: 1,
        name: 'a',
        desc: 'The best restaurant in the world',
        img: 'images/rest1.jpg'
    }
    , {
        id: 2,
        name: 'Restaurant b',
        desc: 'The second best restaurant in the world',
        img: 'images/rest1.jpg'
    }, {
        id: 3,
        name: 'Restaurant b',
        desc: 'The second best restaurant in the world',
        img: 'images/rest1.jpg'
    }, {
        id: 4,
        name: 'Restaurant b',
        desc: 'The second best restaurant in the world',
        img: 'images/rest1.jpg'
    }, {
        id: 5,
        name: 'Restaurant b',
        desc: 'The second best restaurant in the world',
        img: 'images/rest1.jpg'
    }]

$(document).ready(function () {
    update(model);
    toggleWatching(true);
});

// Model
const pages = {
    RESTAURANTS: 'restaurants',
    MENUS: 'menus'
};

let watchingState = true;

let initModel = {
    page: pages.RESTAURANTS,
    selectedRestaurant: 0,
    selectedMenu: [{ id: 0, name: 'pasta' },
    { id: 1, name: 'veg' }],
    selectedMenuItems: []
};

let model = initModel;

//update

const update = (model) => {
    console.log(model);
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
            <p class="card-text">${restaurant.desc}</p>
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
    model = {
        ...model,
        page: pages.MENUS,
        selectedRestaurant: restaurantId,
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
    let model = initModel;
    update(initModel);
}
// RESTAURANT UTILS //

const getAllMenuItemsByRestaurant = (inputModel) => {
    const htmlElementsList = inputModel.selectedMenu.map((item) => {
        if (isSelected(model, item)) {
            return `
            <div class="card col-4" style="min-width:200px;min-height:200px;text-align:center;padding:10px;padding-top:80px; background-color:#ffcc99;" onClick="removeMenuToOrderList('${item.id}')" id="item-${item.id}">
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

const toggleWatching = () => {
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

const makeOrder = () => {
    alert("make an order !");
}

$(window).bind('beforeunload', function(){
    return 'Are you sure you want to leave?';
  });