package com.rolfje.anonimatron.anonymizer;

import com.github.javafaker.Faker;
import com.rolfje.anonimatron.synonyms.Synonym;
import lombok.Data;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created 7/15/2020
 *
 * @author <a href="mailto:ArtemMoroz@coherentsolutions.com">Artem Moroz</a>
 * @version 1.0
 */
public abstract class BaseFakeAnonymizer implements Anonymizer {

    public static final String LOCALE = "locale";
    static Map<String, Faker> fakerMap = new HashMap<>();
    private static Logger LOG = Logger.getLogger(BaseFakeAnonymizer.class);
    final String regex = "((\\w+)\\(\\))*";
    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);

    protected Faker getFaker() {
        return getFaker(null);
    }

    protected Faker getFaker(String locale) {
        Faker f = fakerMap.get(locale);
        if (f == null) {
            f = initFaker(locale);
            synchronized (BaseFakeAnonymizer.class) {//todo refactor
                fakerMap.put(locale, f);
            }
        }
        return f;
    }

    private Faker initFaker(String locale) {
        if (locale == null) {
            return new Faker();
        } else {
            return new Faker(new Locale(locale));
        }
    }

    protected List<String> parseFunction(String function) {

        Matcher matcher = pattern.matcher(function);
        List<String> functionList = new ArrayList();
        while (matcher.find()) {
            if (!matcher.group(0).isEmpty()) {
                functionList.add(matcher.group(2));
            }
        }
        return functionList;
    }

    protected String executeCommandsList(Faker f, List<String> commands) {
        Object result = f;
        try {
            for (String c : commands) {
                result = result.getClass().getDeclaredMethod(c).invoke(result);
            }
        } catch (Exception e) {
            //todo think about to throw ex, or just replace with nulls or ...
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
            return "ERROR!";
        }
        try {
            return new String(String.valueOf(result).getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "ERROR2!";
        }
    }

    protected String execute(AnonymizerConfig config) {
        Faker f = getFaker(config.getLocale());
        String function = config.getFunc();
        List<String> commands = parseFunction(function);
        if (commands.isEmpty()) {
            //todo think what to return
        }
        return executeCommandsList(f, parseFunction(function));
    }

    protected void logResult(Synonym synonym) {
        String message = this.getClass().getSimpleName() + " :: " + synonym;
        LOG.debug(message);
        System.out.println(message);
    }

    protected Integer getIntSafe(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception ex) {
            return null;
        }
    }

    @Data
    public static class AnonymizerConfig {
        String func;
        String locale;
        boolean sameSize;

        public static AnonymizerConfig build(Map<String, String> params) {

            if (params.isEmpty()) {
                throw new UnsupportedOperationException("Unable to anonymize without required params");
            }
            AnonymizerConfig config = new AnonymizerConfig();
            config.setLocale(params.get(LOCALE));
            config.setFunc(params.get("func"));
            try {
                if (params.containsKey("sameSize"))
                    config.setSameSize(Boolean.parseBoolean(params.get("sameSize")));
            } catch (Exception e) {
                //
            }
            return config;
        }
    }

}
