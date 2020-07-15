package com.rolfje.anonimatron.anonymizer;

import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created 7/15/2020
 *
 * @author <a href="mailto:ArtemMoroz@coherentsolutions.com">Artem Moroz</a>
 * @version 1.0
 */
public class FakeStringAnonymizerTest {

    public static void main(String[] args) {
        Faker f = new Faker(new Locale("ru"));
        System.out.println(f.regexify("\\d\\d\\w\\w\\d\\d\\w\\w"));
        System.out.println(f.date().future(50, TimeUnit.DAYS));
        System.out.println(f.internet().image());


    }
}
