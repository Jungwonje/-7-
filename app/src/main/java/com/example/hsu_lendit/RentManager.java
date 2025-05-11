package com.example.hsu_lendit;

import java.text.SimpleDateFormat;
import java.util.*;

public class RentManager {
    private Map<String, PriorityQueue<RentalItem>> itemQueues = new HashMap<>(); // 물품별 우선순위 큐
    private Map<String, Integer> itemCounts = new HashMap<>(); // 물품별 재고 수량
    private Map<String, List<String>> rentalLogs = new HashMap<>(); // 대여 기록

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    // 생성자: 초기 물품과 수량 추가
    public RentManager() {
        addItem("노트북", 3);
        addItem("우산", 4);
        addItem("충전기", 2);
        addItem("가위", 5);
        addItem("마우스", 0);
        addItem("이어폰", 6);
        addItem("태블릿", 3);
    }

    // 물품 추가 메서드
    private void addItem(String itemName, int count) {
        PriorityQueue<RentalItem> queue = new PriorityQueue<>((a, b) -> Long.compare(a.getRentalTime(), b.getRentalTime()));
        for (int i = 1; i <= count; i++) {
            queue.add(new RentalItem(itemName + "-" + (char) ('A' + i - 1))); // A, B, C, ...
        }
        itemQueues.put(itemName, queue);
        itemCounts.put(itemName, count);
        rentalLogs.put(itemName, new ArrayList<>()); // 대여 로그 초기화
    }

    // 물품 대여 메서드
    public String rentItem(String itemName) {
        PriorityQueue<RentalItem> queue = itemQueues.get(itemName);
        if (queue != null && !queue.isEmpty()) {
            RentalItem item = queue.poll(); // 큐에서 물품 추출
            itemCounts.put(itemName, itemCounts.get(itemName) - 1); // 재고 감소
            item.setRentalTime(System.currentTimeMillis()); // 대여 시간 설정

            // 대여 기록 추가
            String timestamp = dateFormat.format(new Date());
            rentalLogs.get(itemName).add("대여: " + item.getId() + " (" + timestamp + ")");

            return item.getId();
        }
        System.out.println("대여 실패: " + itemName + "의 재고가 부족합니다.");
        return null; // 재고 부족
    }

    // 물품 반납 메서드
    public boolean returnItem(String itemName, String itemId) {
        PriorityQueue<RentalItem> queue = itemQueues.get(itemName);
        if (queue != null) {
            RentalItem item = new RentalItem(itemId);
            if (!queue.contains(item)) { // 큐에 없는 ID만 반납 가능
                queue.offer(item); // 큐에 다시 추가
                itemCounts.put(itemName, itemCounts.get(itemName) + 1); // 재고 증가

                // 반납 기록 추가
                String timestamp = dateFormat.format(new Date());
                rentalLogs.get(itemName).add("반납: " + itemId + " (" + timestamp + ")");

                return true;
            }
        }
        return false; // 이미 큐에 있는 ID
    }

    // 특정 물품의 현재 재고 수량 반환
    public int getItemCount(String itemName) {
        return itemCounts.getOrDefault(itemName, 0);
    }

    // 특정 물품의 대여 기록 반환
    public List<String> getRentalLogs(String itemName) {
        return rentalLogs.getOrDefault(itemName, Collections.emptyList());
    }

    // 내부 클래스: 물품 정보
    private static class RentalItem {
        private final String id;
        private long rentalTime; // 대여 시간 (밀리초)

        public RentalItem(String id) {
            this.id = id;
            this.rentalTime = 0; // 초기값
        }

        public String getId() {
            return id;
        }

        public long getRentalTime() {
            return rentalTime;
        }

        public void setRentalTime(long rentalTime) {
            this.rentalTime = rentalTime;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            RentalItem that = (RentalItem) obj;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
