package com.company;

import java.util.Scanner;

public class Main {
    public static void main(String... args) {
        Scanner in = new Scanner(System.in);
        System.out.println("パスワードを入力してください");
        String str = in.nextLine();

        while (!str.equals("abc")) {
            System.out.println("パスワードが違う。入力しろ");
            str = in.nextLine();
        }
        System.out.println("OK!");
    }
}
