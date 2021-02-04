package ominext.timetracking;

//test tính toán thời gian làm dựa theo checkingTime

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int startTime = 8;
        int endTime = 18;
        int startBreaktime = 11;
        int endBreaktime = 13;
        int s = 0;
        int[] time = new int[2];  /* gan thoi gian trong ngay vao mang */
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nhap thong tin gio: ");
        time[0] = scanner.nextInt();
        time[1] = scanner.nextInt();

        for (int i = 0; i < 2; i++) {

            if (time[i] <= startTime) {
                time[i] = startTime;
            } else if ((startBreaktime <= time[i]) && (time[i] <= endBreaktime)) {
                if (i % 2 == 1) {                                                           // checkout nam trong khoang nghi trua
                    if (startBreaktime <= time[i - 1] && time[i - 1] <= endBreaktime) {        // va checkin nam trong khoang nghi trua
                        time[i] = time[i - 1];                                                // thi khong tinh cap checkin/out
                    } else {
                        time[i] = startBreaktime;
                    }
                } else {                                                                  // checkin nam trong khoang nghi trua
                    time[i] = endBreaktime;
                }
            } else if (endBreaktime < time[i]) {
                if (i % 2 == 1 && startBreaktime > time[i - 1]) {
                    s = s - (endBreaktime - startBreaktime);
                }
            }
            if (time[i] >= endTime) {
                time[i] = endTime;
            }
            if (i % 2 == 0) {
                s = s - time[i]; // Check in se tru
            } else {
                s = s + time[i]; // Check out se cong
            }

        }

        System.out.println(s);
    }
}


