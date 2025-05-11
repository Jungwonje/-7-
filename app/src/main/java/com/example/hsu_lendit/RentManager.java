package com.example.hsu_lendit;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class RentManager {
    private Map<String, Queue<String>> itemQueues = new HashMap<>();
    private Map<String, Integer> itemCounts = new HashMap<>();

    public RentManager() {
        // Initialize items and their counts
        addItem("노트북", 3);
        addItem("우산", 4);
        addItem("충전기", 2);
        addItem("가위", 5);
        addItem("마우스", 0);
        addItem("이어폰", 6);
        addItem("태블릿", 3);
    }

    private void addItem(String itemName, int count) {
        Queue<String> queue = new LinkedList<>();
        for (int i = 1; i <= count; i++) {
            queue.add(itemName + "-" + (char) ('A' + i - 1));
        }
        itemQueues.put(itemName, queue);
        itemCounts.put(itemName, count);
    }

    public String rentItem(String itemName) {
        Queue<String> queue = itemQueues.get(itemName);
        if (queue != null && !queue.isEmpty()) {
            itemCounts.put(itemName, itemCounts.get(itemName) - 1);
            return queue.poll();
        }
        return null; // Out of stock
    }

    public void returnItem(String itemName, String itemId) {
        Queue<String> queue = itemQueues.get(itemName);
        if (queue != null) {
            queue.offer(itemId);
            itemCounts.put(itemName, itemCounts.get(itemName) + 1);
        }
    }

    public int getItemCount(String itemName) {
        return itemCounts.getOrDefault(itemName, 0);
    }
}