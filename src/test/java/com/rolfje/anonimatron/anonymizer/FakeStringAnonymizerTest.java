package com.rolfje.anonimatron.anonymizer;

import com.github.javafaker.Faker;

import java.util.Locale;

/**
 * Created 7/15/2020
 *
 * @author <a href="mailto:ArtemMoroz@coherentsolutions.com">Artem Moroz</a>
 * @version 1.0
 */
public class FakeStringAnonymizerTest {

    public static void main(String[] args) {
        Faker f = new Faker(new Locale("ru"));
        //3010972A011PB2
        System.out.println(f.regexify("\\d{7}[A-Z]\\d{3}[A-Z]{2}\\d"));
//        System.out.println(f.date().future(50, TimeUnit.DAYS));
        System.out.println(f.educator().university());
        System.out.println(f.educator().campus());
        System.out.println(f.educator().course());
        System.out.println(f.educator().secondarySchool());


    }
}
