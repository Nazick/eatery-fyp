package com.eatery.system;

import hibernate.HibernateMain;
import model.RatingsEntity;

import javax.xml.bind.SchemaOutputResolver;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by nazick on 1/7/16.
 */
public class EateryDemo {

    HibernateMain hibernateMain;

    public EateryDemo(HibernateMain hibernateMain) {
        this.hibernateMain = hibernateMain;
    }

    public static void main(String[] args){
        EateryDemo eateryDemo=  new EateryDemo(new HibernateMain());
        eateryDemo.startEateryDemo();
    }



    public void startEateryDemo(){
        System.out.println("#### WELCOME EATERY - Multi aspect Restaurant Rating System ####");

        while(true) {
            System.out.println("\nSearch by (Select Number) :");
            System.out.println("1. Restaurant\n2. Aspect");
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();

            switch (s) {
                case "1":
                    this.searchByRestaurant();
                    break;
                case "2":
                    this.searchByAspect();
                    break;
                default:
                    System.out.println("Incorrect Selection");
            }
        }
    }

    public void searchByRestaurant(){
        System.out.print("\nRestaurant Name : ");
        Scanner sc = new Scanner(System.in);
        String restaurantName = sc.nextLine();
        List list = hibernateMain.getRestaurantByName(restaurantName);
        if(list.size() != 0){
            while(true){
                System.out.print("AspectName : ");
                String aspectName = sc.nextLine();
                List list1 = hibernateMain.searchAspectByRestaurant(restaurantName,aspectName);
                if(list1.size() != 0){
                    RatingsEntity ratingsEntity = (RatingsEntity)list1.get(0);
                    System.out.println(aspectName+" - "+ratingsEntity.getScore());
                }else{
                    System.out.println("No results for aspect "+aspectName+" for restaurant "+restaurantName);
                }
                System.out.println("\nPress 0 to return to main and 1 to continue");
                String selection = sc.nextLine();
                if(selection.equals("0")){
                    break;
                }else if(selection.equals("1")){
                    continue;
                }
            }
        }else{
            System.out.println("Incorrect Restaurant name.");
        }
    }

    public void searchByAspect(){
        System.out.print("\nAspect Name : ");
        Scanner scanner = new Scanner(System.in);
        String aspectName = scanner.nextLine();
        List list = hibernateMain.searchByAspect(aspectName,5);
        if(list.size() != 0){
            Iterator iterator = list.iterator();
            while(iterator.hasNext()){
                Object[] tuple = (Object[]) iterator.next();
                System.out.println(tuple[0]+"\t-\t"+tuple[1]);
            }
        }else{
            System.out.println("Incorrect Aspect Name");
        }

    }

}
