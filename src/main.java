import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import java.awt.*;


@ScriptManifest(author = "Ryan", info = "Who doesn't love a cool beer?", name = "F2P Ring Crafter", version = 1, logo = "")


public class main extends Script {

    Position Pub_Door;
    Position Bank;
    long startTime = System.currentTimeMillis();
    long TripPerHour = 0;
    long timeRan;
    long TotalTripTime;
    long startTripTime;
    long totalBars;

    int oldTotal = 0;
    long newTotal;
    boolean sell = false;
    boolean Trade = false;


    @Override

    public void onStart() {

        log("Welcome");


    }

    @Override


    public int onLoop() throws InterruptedException {


        if (Trade) {





        }/*else if (sell) {

            if (getNpcs().closest("Grand Exchange Clerk") != null) {

                if (!getInventory().contains(i -> i.isNote() && i.getName().equals("Bronze bar")) && !getGrandExchange().isOpen()) {
                    getBank().open();
                    getBank().depositAll();
                    getBank().withdrawAll("Coins");
                    getBank().enableMode(org.osbot.rs07.api.Bank.BankMode.WITHDRAW_NOTE);
                    getBank().withdrawAll("Gold ring");


                    getBank().close();
                }

                if (getGrandExchange().isOpen()) {

                    int buy = amountToBuy((int) getInventory().getAmount("Coins"));

                    if (getInventory().contains("Gold ring") && !inBox()) {
                        log("selling bars");
                        getGrandExchange().sellItem(2350, 1, 999999);
                        sleep(5000);


                        for (GrandExchange.Box b : GrandExchange.Box.values()) {
                            if (getGrandExchange().getStatus(b).equals(GrandExchange.Status.FINISHED_SALE) && !getGrandExchange().getStatus(b).equals(GrandExchange.Status.EMPTY)) {
                                getGrandExchange().collect();
                            }
                        }
                    } else {


                        if (getInventory().getAmount("Gold bar") != buy && !inBox()) {

                            getGrandExchange().buyItem(436, "copper ore", 55, buy - (int) getInventory().getAmount("Copper ore"));
                            sleep(5000);

                            for (GrandExchange.Box b : GrandExchange.Box.values()) {
                                if (getGrandExchange().getStatus(b).equals(GrandExchange.Status.FINISHED_BUY) && !getGrandExchange().getStatus(b).equals(GrandExchange.Status.EMPTY)) {
                                    getGrandExchange().collect();
                                }
                            }
                        }
                    }
                } else {
                    getNpcs().closest("Grand Exchange Clerk").interact("Exchange");
                }

            } else {
                getWalking().webWalk(new Position(3165, 3487, 0)); //ge
            }

            if (getInventory().contains("Gold bar")) {
                sell = false;
            }


        } else*/ if (getInventory().contains("Ring mould") &&  getInventory().contains(i -> !i.isNote() && i.getName().equals("Gold bar")) && getObjects().closest("Furnace") != null) {
//log(getInventory().contains(i -> !i.isNote() && i.getName().equals("Copper ore")));

            if (getObjects().closest("Furnace").getPosition().distance(myPosition()) > 16) {
                getWalking().walk(new Position(3096, 3496, 0));
            } else if (!myPlayer().isAnimating()) {
                getObjects().closest("Furnace").interact("Smelt");
                sleep(1000);
                if (getWidgets().get(446, 7) != null) {
                    getWidgets().get(446, 7).interact();
                    ConditionalSleep condslp = new ConditionalSleep(35000) {
                        @Override
                        public boolean condition() throws InterruptedException {
                            return !getInventory().contains("Gold bar");
                        }
                    };

                    condslp.sleep();
                }
            }

        } else if (((!getInventory().contains(i -> i.getName().equals("Gold bar") && !i.isNote()) || !getInventory().contains("Ring mould"))) && myPosition().distance(new Position(3096, 3496, 0)) < 10) {

            if (!getBank().isOpen()) {
                log("openingbank");
                getBank().open();
                sleep(500);
            } else {
                log("depositing");
                totalBars += getInventory().getAmount("Gold ring");
                getBank().depositAll();
                sleep(500);

                if (getBank().contains("Gold bar") && getBank().contains("Ring mould")) {
                    log("withdrawing items");
                    getBank().withdraw("Ring mould", 1);
                    sleep(500);

                    getBank().withdraw("Gold bar", 27);
                } else {
                    log("no ore, time to buy more");
                    getBank().close();
                    sell = true;

                }
            }
        } else {
            getWalking().webWalk(new Position(3096, 3496, 0));
        }// edgeville

        // else{log("error");


        sleep(500);
        //    log("Total Profit: " + (totalBeers * 70) + "gp (" + newTotal + "gp/hr)" + "time: " + timeRan / 1000);

        return 150;
    }

    @Override

    public void onExit() {
    }

    @Override

    public void onPaint(Graphics2D g) {

        timeRan = System.currentTimeMillis() - startTime;
        TotalTripTime = System.currentTimeMillis() - startTripTime;

        g.drawString("Time Ran: " + formatTime(timeRan), 15, 300);
        g.drawString("Rings Made: " + totalBars, 15, 320);
        g.drawString("Total Profit: " + (totalBars * 168) + "gp   (" + (((totalBars / Integer.parseUnsignedInt(String.valueOf(timeRan / 1000 / 60)) * 70) * 60)) + " GP/h)", 15, 340);


    }

    public String formatTime(long ms) {
        long s = ms / 1000, m = s / 60, h = m / 60;
        s %= 60;
        m %= 60;
        h %= 24;
        return (String.format("%02d:%02d:%02d", h, m, s));
    }

    public boolean inVarrock() {
        return true;
    }

    public boolean inBox(){

        boolean contains = false;

        for (GrandExchange.Box b : GrandExchange.Box.values()) {
            if (!getGrandExchange().getStatus(b).equals(GrandExchange.Status.EMPTY)){
                contains = true;
            }
        }

        return contains;

    }


    public int amountToBuy(int cashStack) {
        int amount = 0;
        if (cashStack < 5000) {
            amount = 25;
        } else if (cashStack < 10000) {
            amount = 50;
        } else if (cashStack < 20000) {
            amount = 100;
        } else if (cashStack < 50000) {
            amount = 200;
        } else if (cashStack < 100000) {
            amount = 400;
        }else if(cashStack < 500000){
            amount = 1000;
        }else{
            amount = 2000;
        }

        log(amount);
        return amount;
    }
}