package com.example.adminservice.controller;

import org.apache.ibatis.logging.stdout.StdOutImpl;

import java.util.Scanner;

public class Main {
    public static int gcd(int a, int b) {
        return b == 1 ? a : gcd(b, a % b);
    }

    public static void main(String[] args) {
        System.out.println(gcd(4, 2));
    }
}
