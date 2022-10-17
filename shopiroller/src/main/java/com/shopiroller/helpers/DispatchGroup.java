package com.shopiroller.helpers;

public class DispatchGroup {

    private int count = 0;
    private Runnable runnable;

    public DispatchGroup()
    {
        super();
        count = 0;
    }

    public synchronized void enter(){
        count++;
    }

    public synchronized void leave(){
        count--;
        notifyGroup();
    }

    public void notify(Runnable r) {
        runnable = r;
        notifyGroup();
    }

    private void notifyGroup(){
        if (count <=0 && runnable!=null) {
            runnable.run();
        }
    }
}