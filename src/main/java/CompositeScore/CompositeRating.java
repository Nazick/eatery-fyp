package CompositeScore;

import hibernate.HibernateMain;
import model.*;

import java.util.*;

/**
 * Created by bruntha on 1/31/16.
 */
public class CompositeRating {
    HibernateMain hibernateMain = new HibernateMain();

    public static void main(String[] args) {
        CompositeRating compositeRating = new CompositeRating();
        compositeRating.storeCompositeRatings();
    }

    public HashMap<String, Double> getCompositeRatings() {
        List restaurants = hibernateMain.getRestaurants();

        HashMap<String, Double> compositeRatings = new HashMap<>();
        for (int i = 0; i < restaurants.size(); i++) {
            BusinessEntity businessEntity = (BusinessEntity) restaurants.get(i);
            compositeRatings.put(businessEntity.getBusinessId(), getCompositeRating(businessEntity.getBusinessId()));
            System.out.println(businessEntity.getBusinessId() + "\t" + getCompositeRating(businessEntity.getBusinessId()));
        }
        return compositeRatings;
    }

    public void storeCompositeRatings() {
        List restaurants = hibernateMain.getRestaurants();
        List aspects = hibernateMain.getAspects();

        for (int i = 0; i < restaurants.size(); i++) {
            BusinessEntity businessEntity = (BusinessEntity) restaurants.get(i);
            HashMap<String, Double> cScore = getCompositeRatingOfAspects(businessEntity.getBusinessId());
            Set set = cScore.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                hibernateMain.insertCompositeScore(new CompositeScoreEntity(businessEntity.getBusinessId(), getAspectID(aspects,entry.getKey().toString()),Double.parseDouble(entry.getValue().toString())));
                System.out.println(businessEntity.getBusinessId()+" "+entry.getKey()+" "+entry.getValue());
            }
        }
    }

    public int getAspectID(List list, String aspect) {
        int aspectId=0;
        for (int i = 0; i < list.size(); i++) {
            AspectEntity aspectEntity= (AspectEntity) list.get(i);

            if (aspectEntity.getAspectTag().matches(aspect)) {
                aspectId=aspectEntity.getAspectId();
            }
        }
        return aspectId;
    }

    public double getCompositeRating(String restaurantName) {
//        HibernateMain hibernateMain = new HibernateMain();
        List ratings = hibernateMain.getRatings(restaurantName);

//        print(ratings);

        double foodItemScore = getSubRatings("F_FoodItem", ratings);
        double staffScore = getSubRatings("S_Staff", ratings);
        double deliveryScore = getSubRatings("S_Delivery", ratings);
        double entertainmentScore = getSubRatings("A_Entertainment", ratings);
        double furnitureScore = getSubRatings("A_Furniture", ratings);
        double placesScore = getSubRatings("A_Places", ratings);
        double locatedAreaScore = getSubRatings("A_LocatedArea", ratings);
        double paymentScore = getSubRatings("O_Payment", ratings);
        double reservationScore = getSubRatings("O_Reservation", ratings);
        double experienceScore = getSubRatings("O_Experience", ratings);
        double environmentScore = getSubRatings("A_Environment", ratings);

        updateSubRatings(ratings, "F_FoodItem", foodItemScore);
        updateSubRatings(ratings, "S_Staff", staffScore);
        updateSubRatings(ratings, "S_Delivery", deliveryScore);
        updateSubRatings(ratings, "A_Entertainment", entertainmentScore);
        updateSubRatings(ratings, "A_Furniture", furnitureScore);
        updateSubRatings(ratings, "A_Places", placesScore);
        updateSubRatings(ratings, "A_LocatedArea", locatedAreaScore);
        updateSubRatings(ratings, "O_Payment", paymentScore);
        updateSubRatings(ratings, "O_Reservation", reservationScore);
        updateSubRatings(ratings, "O_Experience", experienceScore);
        updateSubRatings(ratings, "A_Environment", environmentScore);

//        print(ratings);

        double serviceScore = getSubRatings("Service", ratings);
        double worthinessScore = getSubRatings("Worthiness", ratings);
        double ambienceScore = getSubRatings("Ambience", ratings);
        double foodScore = getSubRatings("Food", ratings);
        double offersScore = getSubRatings("Offers", ratings);
        double othersScore = getSubRatings("Others", ratings);

        updateSubRatings(ratings, "Service", serviceScore);
        updateSubRatings(ratings, "Worthiness", worthinessScore);
        updateSubRatings(ratings, "Ambience", ambienceScore);
        updateSubRatings(ratings, "Food", foodScore);
        updateSubRatings(ratings, "Offers", offersScore);
        updateSubRatings(ratings, "Others", othersScore);

//        print(ratings);

        double restaurantScore = getSubRatings("Restaurant", ratings);

        return restaurantScore;
    }

    public HashMap<String, Double> getCompositeRatingOfAspects(String restaurantName) {
        List ratings = hibernateMain.getRatings(restaurantName);
        HashMap<String, Double> comScors = new HashMap<>();
//        print(ratings);

        comScors.put("F_Appetizer",getRatingForAspect("F_Appetizer",ratings));
        comScors.put("F_Dessert",getRatingForAspect("F_Dessert",ratings));
        comScors.put("F_Drinks",getRatingForAspect("F_Drinks",ratings));
        comScors.put("F_FI_Taste",getRatingForAspect("F_FI_Taste",ratings));
        comScors.put("F_FI_Price",getRatingForAspect("F_FI_Price",ratings));
        comScors.put("F_FI_Quality",getRatingForAspect("F_FI_Quality",ratings));
        comScors.put("F_FI_Healthy",getRatingForAspect("F_FI_Healthy",ratings));
        comScors.put("F_FI_CookingLevel",getRatingForAspect("F_FI_CookingLevel",ratings));
        comScors.put("F_FI_Size",getRatingForAspect("F_FI_Size",ratings));
        comScors.put("F_FI_Religious",getRatingForAspect("F_FI_Religious",ratings));
        comScors.put("F_Ingredients",getRatingForAspect("F_Ingredients",ratings));
        comScors.put("S_Menu",getRatingForAspect("S_Menu",ratings));
        comScors.put("S_OpenHours",getRatingForAspect("S_OpenHours",ratings));
        comScors.put("S_Parking",getRatingForAspect("S_Parking",ratings));
        comScors.put("S_Stf_Behavior",getRatingForAspect("S_Stf_Behavior",ratings));
        comScors.put("S_Stf_Experience",getRatingForAspect("S_Stf_Experience",ratings));
        comScors.put("S_Stf_Availability",getRatingForAspect("S_Stf_Availability",ratings));
        comScors.put("S_Stf_Appearance",getRatingForAspect("S_Stf_Appearance",ratings));
        comScors.put("S_Gift",getRatingForAspect("S_Gift",ratings));
        comScors.put("S_PetsAllowed",getRatingForAspect("S_PetsAllowed",ratings));
        comScors.put("S_Accessibility",getRatingForAspect("S_Accessibility",ratings));
        comScors.put("S_Wifi",getRatingForAspect("S_Wifi",ratings));
        comScors.put("S_Del_Time",getRatingForAspect("S_Del_Time",ratings));
        comScors.put("S_Del_OrderingMethod",getRatingForAspect("S_Del_OrderingMethod",ratings));
        comScors.put("S_Cutlery",getRatingForAspect("S_Cutlery",ratings));
        comScors.put("S_Seating",getRatingForAspect("S_Seating",ratings));
        comScors.put("A_Decor",getRatingForAspect("A_Decor",ratings));
        comScors.put("A_Fur_Table",getRatingForAspect("A_Fur_Table",ratings));
        comScors.put("A_Fur_Door",getRatingForAspect("A_Fur_Door",ratings));
        comScors.put("A_Ent_Music",getRatingForAspect("A_Ent_Music",ratings));
        comScors.put("A_Ent_LiveShow",getRatingForAspect("A_Ent_LiveShow",ratings));
        comScors.put("A_Ent_Tv",getRatingForAspect("A_Ent_Tv",ratings));
        comScors.put("A_Env_Size",getRatingForAspect("A_Env_Size",ratings));
        comScors.put("A_Env_Type",getRatingForAspect("A_Env_Type",ratings));
        comScors.put("A_Env_AC",getRatingForAspect("A_Env_AC",ratings));
        comScors.put("A_Plc_Bathroom",getRatingForAspect("A_Plc_Bathroom",ratings));
        comScors.put("A_Plc_SmokingArea",getRatingForAspect("A_Plc_SmokingArea",ratings));
        comScors.put("A_Plc_Buffet",getRatingForAspect("A_Plc_Buffet",ratings));
        comScors.put("A_Plc_Bar",getRatingForAspect("A_Plc_Bar",ratings));
        comScors.put("A_Plc_Patio",getRatingForAspect("A_Plc_Patio",ratings));
        comScors.put("A_Plc_DiningRoom",getRatingForAspect("A_Plc_DiningRoom",ratings));
        comScors.put("A_Plc_RestRoom",getRatingForAspect("A_Plc_RestRoom",ratings));
        comScors.put("A_Plc_Kitchen",getRatingForAspect("A_Plc_Kitchen",ratings));
        comScors.put("A_OutsideView",getRatingForAspect("A_OutsideView",ratings));
        comScors.put("A_LocatedArea",getRatingForAspect("A_LocatedArea",ratings));
        comScors.put("W_Price",getRatingForAspect("W_Price",ratings));
        comScors.put("W_Waiting",getRatingForAspect("W_Waiting",ratings));
        comScors.put("O_Reservation",getRatingForAspect("O_Reservation",ratings));
        comScors.put("O_Pay_Method",getRatingForAspect("O_Pay_Method",ratings));
        comScors.put("O_Pay_Price",getRatingForAspect("O_Pay_Price",ratings));
        comScors.put("O_Exp_StarsByCus",getRatingForAspect("O_Exp_StarsByCus",ratings));

        double foodItemScore = getSubRatings("F_FoodItem", ratings);
        double staffScore = getSubRatings("S_Staff", ratings);
        double deliveryScore = getSubRatings("S_Delivery", ratings);
        double entertainmentScore = getSubRatings("A_Entertainment", ratings);
        double furnitureScore = getSubRatings("A_Furniture", ratings);
        double placesScore = getSubRatings("A_Places", ratings);
        double locatedAreaScore = getSubRatings("A_LocatedArea", ratings);
        double paymentScore = getSubRatings("O_Payment", ratings);
        double reservationScore = getSubRatings("O_Reservation", ratings);
        double experienceScore = getSubRatings("O_Experience", ratings);
        double environmentScore = getSubRatings("A_Environment", ratings);

        comScors.put("F_FoodItem", foodItemScore);
        comScors.put("S_Staff", staffScore);
        comScors.put("S_Delivery", deliveryScore);
        comScors.put("A_Entertainment", entertainmentScore);
        comScors.put("A_Furniture", furnitureScore);
        comScors.put("A_Places", placesScore);
        comScors.put("A_LocatedArea", locatedAreaScore);
        comScors.put("O_Payment", paymentScore);
        comScors.put("O_Reservation", reservationScore);
        comScors.put("O_Experience", experienceScore);
        comScors.put("A_Environment", environmentScore);

        updateSubRatings(ratings, "F_FoodItem", foodItemScore);
        updateSubRatings(ratings, "S_Staff", staffScore);
        updateSubRatings(ratings, "S_Delivery", deliveryScore);
        updateSubRatings(ratings, "A_Entertainment", entertainmentScore);
        updateSubRatings(ratings, "A_Furniture", furnitureScore);
        updateSubRatings(ratings, "A_Places", placesScore);
        updateSubRatings(ratings, "A_LocatedArea", locatedAreaScore);
        updateSubRatings(ratings, "O_Payment", paymentScore);
        updateSubRatings(ratings, "O_Reservation", reservationScore);
        updateSubRatings(ratings, "O_Experience", experienceScore);
        updateSubRatings(ratings, "A_Environment", environmentScore);

//        print(ratings);

        double serviceScore = getSubRatings("Service", ratings);
        double worthinessScore = getSubRatings("Worthiness", ratings);
        double ambienceScore = getSubRatings("Ambience", ratings);
        double foodScore = getSubRatings("Food", ratings);
        double offersScore = getRatingForAspect("Offers", ratings);
        double othersScore = getSubRatings("Others", ratings);

        comScors.put("Service", serviceScore);
        comScors.put("Worthiness", worthinessScore);
        comScors.put("Ambience", ambienceScore);
        comScors.put("Food", foodScore);
        comScors.put("Offers", offersScore);
        comScors.put("Others", othersScore);


        updateSubRatings(ratings, "Service", serviceScore);
        updateSubRatings(ratings, "Worthiness", worthinessScore);
        updateSubRatings(ratings, "Ambience", ambienceScore);
        updateSubRatings(ratings, "Food", foodScore);
        updateSubRatings(ratings, "Offers", offersScore);
        updateSubRatings(ratings, "Others", othersScore);

        double restaurantScore = getSubRatings("Restaurant", ratings);
        comScors.put("Restaurant", restaurantScore);

        return comScors;
    }

    private void updateSubRatings(List ratings, String aspect, double score) {
        boolean isAvailable = false;
        for (int i = 0; i < ratings.size(); i++) {
            RatingsEntity ratingsEntity = (RatingsEntity) ratings.get(i);
            if (ratingsEntity.getAspectTag().matches(aspect)) {
                ratingsEntity.setScore(score);
                ratings.remove(i);
                ratings.add(ratingsEntity);
                isAvailable = true;
            }
        }
        if (!isAvailable) {
            ratings.add(new RatingsEntity(aspect, "", score, 1));
        }
    }

    private void print(List ratings) {
        for (int i = 0; i < ratings.size(); i++) {
            RatingsEntity ratingsEntity = (RatingsEntity) ratings.get(i);
            System.out.println(ratingsEntity.getAspectTag() + " " + ratingsEntity.getScore());
        }
    }

    private double getSubRatings(String parentAspect, List ratings) {
//        HibernateMain hibernateMain = new HibernateMain();
        List weights = hibernateMain.getWeights(parentAspect);

        double subRating = 0.0;
        double factor = 0.0;
        for (int i = 0; i < weights.size(); i++) {
            WeightsEntity weightsEntity = (WeightsEntity) weights.get(i);
            double rating = getRatingForAspect(weightsEntity.getAspect(), ratings);
            if (rating != 0) {
                subRating += weightsEntity.getWeight() * rating;
                factor += weightsEntity.getWeight();
            }
        }
        if (subRating != 0)
            return subRating / factor;
        else
            return 0.0;
    }

    private double getRatingForAspect(String aspect, List ratings) {
        double rating = 0.0;
        for (int i = 0; i < ratings.size(); i++) {
            RatingsEntity ratingsEntity = (RatingsEntity) ratings.get(i);
            if (ratingsEntity.getAspectTag().matches(aspect)) {
                rating = ratingsEntity.getScore();
                break;
            }
        }
        return rating;
    }
}
