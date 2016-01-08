package com.eatery.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nazick on 1/6/16.
 */
public class AspectTags {

    public static List<String> aspects = Arrays.asList("Food", "F_Appetizer", "F_Dessert", "F_Drinks", "F_FoodItem", "F_FI_Taste",
            "F_FI_Price", "F_FI_Quality", "F_FI_Healthy", "F_FI_CookingLevel", "F_FI_Size", "F_FI_Religious", "F_Ingredients",
            "Service", "S_Menu", "S_OpenHours", "S_Parking", "S_Staff", "S_Stf_Behavior", "S_Stf_Experience", "S_Stf_Availability",
            "S_Stf_Appearance", "S_Gift", "S_PetsAllowed", "S_Accessibility", "S_Wifi", "S_Delivery", "S_Del_Time",
            "S_Del_OrderingMethod", "S_Cutlery", "S_Seating", "Ambience", "A_Decor", "A_Furniture", "A_Fur_Table", "A_Fur_Door",
            "A_Entertainment", "A_Ent_Music", "A_Ent_LiveShow", "A_Ent_Tv", "A_Environment", "A_Env_Size", "A_Env_Type",
            "A_Env_AC", "A_Places", "A_Plc_Bathroom", "A_Plc_SmokingArea", "A_Plc_Buffet", "A_Plc_Bar", "A_Plc_Patio",
            "A_Plc_DiningRoom", "A_Plc_RestRoom", "A_Plc_Kitchen", "A_OutsideView", "A_LocatedArea", "Offers", "Worthiness",
            "W_Price", "W_Waiting", "Others", "O_Reservation", "O_Payment", "O_Pay_Method", "O_Pay_Price", "O_Experience",
            "O_Exp_StarsByCus", "Restaurant", "Opinion");

    public static boolean isAspectExist(String aspectName){
        if(aspects.contains(aspectName)){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String args[]){
        System.out.println(AspectTags.isAspectExist("Food"));
    }
}