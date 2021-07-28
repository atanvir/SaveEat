package com.saveeat.utils.application

import com.saveeat.R
import com.saveeat.model.request.brand.BrandModel
import com.saveeat.model.request.menu.MenuCategoryModel
import com.saveeat.model.request.restaurant.*
import com.saveeat.model.request.reward.RewardModel
import kotlinx.coroutines.CoroutineScope

object StaticDataHelper {

    fun getRewardModel() : MutableList<RewardModel?>?{
        val list: MutableList<RewardModel?> =ArrayList()
        list.add(RewardModel(image = R.drawable.group_4006,"Food Lover"))
        list.add(RewardModel(image = R.drawable.group_4007,"Carbon Capturer"))
        list.add(RewardModel(image = R.drawable.group_4008,"Tree Lover"))
        list.add(RewardModel(image = R.drawable.group_4009,"CO2minator"))


        list.add(RewardModel(image = R.drawable.group_4006,"Food Lover"))
        list.add(RewardModel(image = R.drawable.group_4007,"Carbon Capturer"))
        list.add(RewardModel(image = R.drawable.group_4008,"Tree Lover"))
        list.add(RewardModel(image = R.drawable.group_4009,"CO2minator"))


        list.add(RewardModel(image = R.drawable.group_4006,"Food Lover"))
        list.add(RewardModel(image = R.drawable.group_4007,"Carbon Capturer"))
        list.add(RewardModel(image = R.drawable.group_4008,"Tree Lover"))
        list.add(RewardModel(image = R.drawable.group_4009,"CO2minator"))
        return list
    }

    fun getLocationByRestraurant() : MutableList<RestaurantByLocationModel?>?{

        val list: MutableList<RestaurantByLocationModel?> =ArrayList()

        list?.add(RestaurantByLocationModel(restroImage = R.drawable.group_2569,isSuper=true,distance = "167 m",rating = 1,stock = "5 meals left",outOfStock = false,veg = false,sellingPrice = 40.0,marketPrice = 100.0,image = R.drawable.rectangle_4136,"Breakfast Club 21"))
        list?.add(RestaurantByLocationModel(restroImage = R.drawable.group_2570,isSuper=false,distance = "172 m",rating = 3,stock = "1 meal left",outOfStock = false,veg = false,sellingPrice = 40.0,marketPrice = 100.0,image = R.drawable.new_data,"Turkish Kebab house"))
        list?.add(RestaurantByLocationModel(restroImage = R.drawable.group_2569,isSuper=true,distance = "155 m",rating = 2,stock = "Nothing left today",outOfStock = true,veg = false,sellingPrice = 40.0,marketPrice = 100.0,image = R.drawable.laal_rang,"Coffee & Breakfast"))
        list?.add(RestaurantByLocationModel(restroImage = R.drawable.group_2569,isSuper=true,distance = "167 m",rating = 1,stock = "5 meals left",outOfStock = false,veg = false,sellingPrice = 40.0,marketPrice = 100.0,image = R.drawable.rectangle_4136,"Breakfast Club 21"))
        list?.add(RestaurantByLocationModel(restroImage = R.drawable.group_2570,isSuper=false,distance = "172 m",rating = 3,stock = "1 meal left",outOfStock = false,veg = false,sellingPrice = 40.0,marketPrice = 100.0,image = R.drawable.new_data,"Turkish Kebab house"))
        list?.add(RestaurantByLocationModel(restroImage = R.drawable.group_2569,isSuper=true,distance = "155 m",rating = 2,stock = "Nothing left today",outOfStock = true,veg = false,sellingPrice = 40.0,marketPrice = 100.0,image = R.drawable.laal_rang,"Coffee & Breakfast"))

        list?.add(RestaurantByLocationModel(restroImage = R.drawable.group_2569,isSuper=true,distance = "167 m",rating = 1,stock = "5 meals left",outOfStock = false,veg = false,sellingPrice = 40.0,marketPrice = 100.0,image = R.drawable.rectangle_4136,"Breakfast Club 21"))
        list?.add(RestaurantByLocationModel(restroImage = R.drawable.group_2570,isSuper=false,distance = "172 m",rating = 3,stock = "1 meal left",outOfStock = false,veg = false,sellingPrice = 40.0,marketPrice = 100.0,image = R.drawable.new_data,"Turkish Kebab house"))
        list?.add(RestaurantByLocationModel(restroImage = R.drawable.group_2569,isSuper=true,distance = "155 m",rating = 2,stock = "Nothing left today",outOfStock = true,veg = false,sellingPrice = 40.0,marketPrice = 100.0,image = R.drawable.laal_rang,"Coffee & Breakfast"))
        return list
    }

    fun homeData(): MutableList<Any?>? {



        val list: MutableList<Any?> =ArrayList()

        list?.add(CategoryModel(isFavScreen=false,superRestro = false,outOfStock = false, sectionName="Save it before it's too late", sectionImage = R.drawable.poke, image= R.drawable.aab, name = "", rating=0, distance = 516.0, fav=false, products = null,brand = null, getSaveRestro()))

        list?.add(CategoryModel(isFavScreen=false,superRestro = true,outOfStock = false, sectionName="Popular Around You", sectionImage = R.drawable.group_3987, image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=0, distance = 516.0, fav=false, products = getOrganeList(),null,null))
        list?.add(CategoryModel(isFavScreen=false,superRestro = true,outOfStock = false, sectionName="", sectionImage = null, image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=0, distance = 516.0, fav=false, products = getBombayCanteenList(),null,null))
        list?.add(CategoryModel(isFavScreen=false,superRestro = true,outOfStock = false, sectionName="", sectionImage = null, image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=0, distance = 516.0, fav=false, products = getOrganeList2(),null,null))
        list?.add(CategoryModel(isFavScreen=false,superRestro = false,outOfStock = false, sectionName="", sectionImage = null, image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=0, distance = 516.0, fav=false, products = getOrganeList(),null,null))
        list?.add(CategoryModel(isFavScreen=false,superRestro = true,outOfStock = false, sectionName="", sectionImage = null, image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=0, distance = 516.0, fav=false, products = getOrganeList2(),null,null))
       // list?.add(RestaurantCategoryModel(isFavScreen=false,superRestro = false,outOfStock = true, sectionName="", sectionImage = "", image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=2, distance = 506.0, fav=false, products = getBlurData()))


        list?.add(CategoryModel(isFavScreen=false,superRestro = false,outOfStock = false, sectionName="New restaurants on SaveEat", sectionImage = R.drawable.group_3989, image= R.drawable.aab, name = "", rating=0, distance = 516.0, fav=false, products = getOrganeList(),brand = getBrandData(),null))
        list?.add(CategoryModel(isFavScreen=false,superRestro = true,outOfStock = false, sectionName="All restaurants near you", sectionImage = R.drawable.group_3990, image= R.drawable.aab, name = "Deep Era - Normal...", rating=0, distance = 516.0, fav=false, products = getOrganeList(),brand = null,null))
        list?.add(CategoryModel(isFavScreen=false,superRestro = true,outOfStock = false, sectionName="", sectionImage = null, image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=0, distance = 516.0, fav=false, products = getBombayCanteenList(),null,null))
        list?.add(CategoryModel(isFavScreen=false,superRestro = true,outOfStock = false, sectionName="", sectionImage = null, image= R.drawable.aab, name = "New Era - Normal...", rating=0, distance = 516.0, fav=false, products = getOrganeList2(),null,null))
        list?.add(CategoryModel(isFavScreen=false,superRestro = false,outOfStock = false, sectionName="", sectionImage = null, image= R.drawable.aab, name = "Salan Bran", rating=0, distance = 516.0, fav=false, products = getOrganeList(),null,null))
        list?.add(CategoryModel(isFavScreen=false,superRestro = true,outOfStock = false, sectionName="", sectionImage = null, image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=0, distance = 516.0, fav=false, products = getOrganeList2(),null,null))
        list?.add(CategoryModel(isFavScreen=false,superRestro = false,outOfStock = true, sectionName="", sectionImage = null, image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=2, distance = 506.0, fav=false, products = getBlurData(),null,null))

        return list

    }
    fun getBrandData() : MutableList<BrandModel?>?{

        val list: MutableList<BrandModel?> =ArrayList()

        list?.add(BrandModel(name = "McDonalds",image = R.drawable.mc))
        list?.add(BrandModel(name = "Chuyax Diner",image = R.drawable.bg))
        list?.add(BrandModel(name = "Starbucks",image = R.drawable.queen))

        return list

    }

    fun getSaveRestro() : MutableList<SaveRestaurantsModel?>?{

        val list: MutableList<SaveRestaurantsModel?> =ArrayList()

        list?.add(SaveRestaurantsModel(restroLogo=R.drawable.rectangle_4136, sellingPrice = 75.0, marketPrice = 210.0, image = R.drawable.laalal, storeName = "The Bombay Canteen", name = "Ham & Cheese Sandwich", rating = 3, distnace = "175m"))
        list?.add(SaveRestaurantsModel(restroLogo=R.drawable.rectangle_4136, sellingPrice = 85.0, marketPrice = 300.0, image = R.drawable.group_25621, storeName = "The Salad Box", name = "Avocado, Egg and Tuna salad", rating = 4, distnace = "179m"))
        list?.add(SaveRestaurantsModel(restroLogo=R.drawable.rectangle_4136, sellingPrice = 75.0, marketPrice = 210.0, image = R.drawable.laalal, storeName = "The Bombay Canteen", name = "Ham & Cheese Sandwich", rating = 3, distnace = "175m"))
        list?.add(SaveRestaurantsModel(restroLogo=R.drawable.rectangle_4136, sellingPrice = 85.0, marketPrice = 300.0, image = R.drawable.group_25621, storeName = "The Salad Box", name = "Avocado, Egg and Tuna salad", rating = 4, distnace = "179m"))

        return list
    }




     fun getFavData(): MutableList<Any?>? {

        val model : MutableList<Any?>? = ArrayList()
//        model?.add(MenuCategoryModel(superRestro = false,outOfStock=false, sectionName="Save it before it's too late", sectionImage = "", image= R.drawable.group_2562, name = "The Bombay Canteen", rating=4, distance = 178.0, fav=false, products = getBombayCanteenList()))
        model?.add(CategoryModel(isFavScreen=true,superRestro = true,outOfStock = false, sectionName="Popular Around You", sectionImage = null, image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=0, distance = 516.0, fav=false, products = getOrganeList(),null,null))
        model?.add(CategoryModel(isFavScreen=true,superRestro = true,outOfStock = false, sectionName="", sectionImage = null, image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=0, distance = 516.0, fav=false, products = getOrganeList2(),null,null))
         model?.add(CategoryModel(isFavScreen=true,superRestro = true,outOfStock = false, sectionName="", sectionImage = null, image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=0, distance = 516.0, fav=false, products = getOrganeList(),null,null))
         model?.add(CategoryModel(isFavScreen=true,superRestro = true,outOfStock = false, sectionName="", sectionImage = null, image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=0, distance = 516.0, fav=false, products = getOrganeList2(),null,null))
         model?.add(CategoryModel(isFavScreen=true,superRestro = false,outOfStock = true, sectionName="", sectionImage = null, image= R.drawable.aab, name = "Orange Barn - Chicken Fa...", rating=2, distance = 506.0, fav=false, products = getBlurData(),null,null))
      //  model?.add(MenuCategoryModel(isFavScreen=true,superRestro = false,outOfStock = false, sectionName="New restaurants on SaveEat", sectionImage = "", image= R.drawable.rectangle_142, name = "Grills Beef & Beer", rating=3, distance = 411.0, fav=false, products = getBombayCanteenList()))

        return model
    }

    private fun getBlurData(): MutableList<RestaurantProductModel?>? {
        val list: MutableList<RestaurantProductModel?>? = ArrayList()

        list?.add(RestaurantProductModel(stock="Nothing left today",outOfStock=true, veg= true, sellingPrice = 200.0, marketPrice = 89.0, image= R.drawable.group_2767, name = "2 Chicken Skewers with\n" + "balsamic rice"))
        list?.add(RestaurantProductModel(stock="Nothing left today", true,veg= true, sellingPrice = 150.0, marketPrice = 98.0, image= R.drawable.group_2775, name = "Brie, tomato and spinach\n" + "baguette"))
        list?.add(RestaurantProductModel(stock="Nothing left today",true, veg= true, sellingPrice = 300.0, marketPrice = 119.0, image= R.drawable.group_2771, name = "Classic chicken &\n" + "cheese burger"))


        list?.add(RestaurantProductModel(stock="Nothing left today", true,veg= true, sellingPrice = 200.0, marketPrice = 89.0, image= R.drawable.group_2767, name = "2 Chicken Skewers with\n" + "balsamic rice"))
        list?.add(RestaurantProductModel(stock="Nothing left today", true,veg= true, sellingPrice = 150.0, marketPrice = 98.0, image= R.drawable.group_2775, name = "Brie, tomato and spinach\n" + "baguette"))
        list?.add(RestaurantProductModel(stock="Nothing left today",true, veg= true, sellingPrice = 300.0, marketPrice = 119.0, image= R.drawable.group_2771, name = "Classic chicken &\n" + "cheese burger"))


        list?.add(RestaurantProductModel(stock="Nothing left today", true,veg= true, sellingPrice = 200.0, marketPrice = 89.0, image= R.drawable.group_2767, name = "2 Chicken Skewers with\n" + "balsamic rice"))
        list?.add(RestaurantProductModel(stock="Nothing left today",true, veg= true, sellingPrice = 150.0, marketPrice = 98.0, image= R.drawable.group_2775, name = "Brie, tomato and spinach\n" + "baguette"))
        list?.add(RestaurantProductModel(stock="Nothing left today",true, veg= true, sellingPrice = 300.0, marketPrice = 119.0, image= R.drawable.group_2771, name = "Classic chicken &\n" + "cheese burger"))



        return list

    }

    fun getOrganeList(): MutableList<RestaurantProductModel?>? {
        val list: MutableList<RestaurantProductModel?>? = ArrayList()

        list?.add(RestaurantProductModel(stock="7 left", false,veg= true, sellingPrice = 200.0, marketPrice = 75.0, image= R.drawable.atlt, name = "Plate of rice and fried\n" + "chicken"))
        list?.add(RestaurantProductModel(stock="3 left",false ,veg= true, sellingPrice = 250.0, marketPrice = 75.0, image= R.drawable.tan, name = "Assorted fried bites &\n" + "pita bread"))
        list?.add(RestaurantProductModel(stock="5 left", false,veg= true, sellingPrice = 300.0, marketPrice = 110.0, image= R.drawable.retl, name = "Lamb ribs with\n" + "vegetables and sauce"))

        list?.add(RestaurantProductModel(stock="7 left",false, veg= true, sellingPrice = 200.0, marketPrice = 75.0, image= R.drawable.atlt, name = "Plate of rice and fried\n" + "chicken"))
        list?.add(RestaurantProductModel(stock="3 left",false, veg= true, sellingPrice = 250.0, marketPrice = 75.0, image= R.drawable.tan, name = "Assorted fried bites &\n" + "pita bread"))
        list?.add(RestaurantProductModel(stock="5 left", false,veg= true, sellingPrice = 300.0, marketPrice = 110.0, image= R.drawable.retl, name = "Lamb ribs with\n" + "vegetables and sauce"))

        list?.add(RestaurantProductModel(stock="7 left",false, veg= true, sellingPrice = 200.0, marketPrice = 75.0, image= R.drawable.atlt, name = "Plate of rice and fried\n" + "chicken"))
        list?.add(RestaurantProductModel(stock="3 left",false, veg= true, sellingPrice = 250.0, marketPrice = 75.0, image= R.drawable.tan, name = "Assorted fried bites &\n" + "pita bread"))
        list?.add(RestaurantProductModel(stock="5 left",false, veg= true, sellingPrice = 300.0, marketPrice = 110.0, image= R.drawable.retl, name = "Lamb ribs with\n" + "vegetables and sauce"))

        return list

    }

    private fun getOrganeList2(): MutableList<RestaurantProductModel?>? {
        val list: MutableList<RestaurantProductModel?>? = ArrayList()

        list?.add(RestaurantProductModel(stock="7 left",false, veg= true, sellingPrice = 200.0, marketPrice = 79.0, image= R.drawable.lamda, name = "Plate of rice and fried\n" + "chicken"))
        list?.add(RestaurantProductModel(stock="3 left",false, veg= true, sellingPrice = 250.0, marketPrice = 71.0, image= R.drawable.gana, name = "Assorted fried bites &\n" + "pita bread"))
        list?.add(RestaurantProductModel(stock="5 left",false, veg= true, sellingPrice = 310.0, marketPrice = 119.0, image= R.drawable.retl, name = "Lamb ribs with\n" + "vegetables and sauce"))

        list?.add(RestaurantProductModel(stock="7 left",false, veg= true, sellingPrice = 200.0, marketPrice = 79.0, image= R.drawable.lamda, name = "Plate of rice and fried\n" + "chicken"))
        list?.add(RestaurantProductModel(stock="3 left",false, veg= true, sellingPrice = 250.0, marketPrice = 71.0, image= R.drawable.gana, name = "Assorted fried bites &\n" + "pita bread"))
        list?.add(RestaurantProductModel(stock="5 left",false, veg= true, sellingPrice = 310.0, marketPrice = 119.0, image= R.drawable.retl, name = "Lamb ribs with\n" + "vegetables and sauce"))

        list?.add(RestaurantProductModel(stock="7 left",false, veg= true, sellingPrice = 200.0, marketPrice = 79.0, image= R.drawable.lamda, name = "Plate of rice and fried\n" + "chicken"))
        list?.add(RestaurantProductModel(stock="3 left",false, veg= true, sellingPrice = 250.0, marketPrice = 71.0, image= R.drawable.gana, name = "Assorted fried bites &\n" + "pita bread"))
        list?.add(RestaurantProductModel(stock="5 left",false, veg= true, sellingPrice = 310.0, marketPrice = 119.0, image= R.drawable.retl, name = "Lamb ribs with\n" + "vegetables and sauce"))

        return list

    }

    private fun getBombayCanteenList(): MutableList<RestaurantProductModel?>? {
        val list: MutableList<RestaurantProductModel?>? = ArrayList()
        list?.add(RestaurantProductModel(stock="4 left", false,veg= true, sellingPrice = 300.0, marketPrice = 75.0, image= R.drawable.rectangle_4136, name = "Ham & Cheese sandwich\n" + "on brown bread"))
        list?.add(RestaurantProductModel(stock="1 left", false, veg= true, sellingPrice = 200.0, marketPrice = 75.0, image= R.drawable.recta, name = "Poached eggs & avocado\n" + "on toast"))
        list?.add(RestaurantProductModel(stock="2 left", false, veg= true, sellingPrice = 400.0, marketPrice = 110.0, image= R.drawable.recta, name = "Fried chicken meal"))

        list?.add(RestaurantProductModel(stock="4 left", false, veg= true, sellingPrice = 300.0, marketPrice = 75.0, image= R.drawable.rectangle_4136, name = "Ham & Cheese sandwich\n" + "on brown bread"))
        list?.add(RestaurantProductModel(stock="1 left", false, veg= true, sellingPrice = 200.0, marketPrice = 75.0, image= R.drawable.recta, name = "Poached eggs & avocado\n" + "on toast"))
        list?.add(RestaurantProductModel(stock="2 left", false, veg= true, sellingPrice = 400.0, marketPrice = 110.0, image= R.drawable.recta, name = "Fried chicken meal"))

        list?.add(RestaurantProductModel(stock="4 left", false, veg= true, sellingPrice = 300.0, marketPrice = 75.0, image= R.drawable.rectangle_4136, name = "Ham & Cheese sandwich\n" + "on brown bread"))
        list?.add(RestaurantProductModel(stock="1 left", false, veg= true, sellingPrice = 200.0, marketPrice = 75.0, image= R.drawable.recta, name = "Poached eggs & avocado\n" + "on toast"))
        list?.add(RestaurantProductModel(stock="2 left", false, veg= true, sellingPrice = 400.0, marketPrice = 110.0, image= R.drawable.recta, name = "Fried chicken meal"))

        return list
    }

    fun getMapRestaurant(): MutableList<RestauarntMap?>? {
        val list: MutableList<RestauarntMap?>? = ArrayList()

        list?.add(RestauarntMap(image = R.drawable.image_28,name="Burger King - Oxford Circus ",stock="2 meals left"))
        list?.add(RestauarntMap(image = R.drawable.image_288,name="Dominos",stock="5 meals left"))


        list?.add(RestauarntMap(image = R.drawable.image_28,name="Burger King - Oxford Circus ",stock="2 meals left"))
        list?.add(RestauarntMap(image = R.drawable.image_288,name="Dominos",stock="5 meals left"))


        return list


    }

    fun getMenuCategory(): List<MenuCategoryModel?>? {
        val list: MutableList<MenuCategoryModel?>? = ArrayList()
        list?.add(MenuCategoryModel(name="All",selected = true))
        list?.add(MenuCategoryModel(name="Starters",selected = false))
        list?.add(MenuCategoryModel(name="Main Course",selected = false))
        list?.add(MenuCategoryModel(name="Desserts",selected = false))
        list?.add(MenuCategoryModel(name="Bevera",selected = false))

        return list

    }

    fun menuData(): MutableList<RestaurantProductModel?>? {
        val list: MutableList<RestaurantProductModel?>? = ArrayList()
        list?.addAll(getOrganeList()!!)
        list?.addAll(getOrganeList2()!!)
        list?.addAll(getBlurData()!!)
        return list

    }

}