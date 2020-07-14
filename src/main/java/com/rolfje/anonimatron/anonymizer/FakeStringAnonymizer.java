package com.rolfje.anonimatron.anonymizer;

import com.github.javafaker.Faker;
import com.rolfje.anonimatron.synonyms.StringSynonym;
import com.rolfje.anonimatron.synonyms.Synonym;
import lombok.Data;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FakeStringAnonymizer implements Anonymizer {
    private static Logger LOG = Logger.getLogger(FakeStringAnonymizer.class);
    final String regex = "((\\w+)\\(\\))*";
    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);


    public String getType() {
        return "FAKE_STRING";
    }

    @Override
    public Synonym anonymize(Object from, int size, boolean shortlived) {
        throw new UnsupportedOperationException("Unable to anonymize without required params");
    }

    public Synonym anonymize(Object from, int size, boolean shortlived, Map<String, String> params) {

        AnonymizerConfig config = AnonymizerConfig.build(params);
        String fromStr = (String) from;
        String result = execute(config);
        if (config.isSameSize()) {
            int fromValueSize = fromStr.length();
            while (fromValueSize != result.length()) {
                result = execute(config);
            }
        }
//        StringSynonym s = new StringSynonym();
//        s.setType(getType());
//        s.setFrom(fromStr);
//        s.setTo(result);

        StringSynonym stringSynonym = new StringSynonym(
                getType(),
                fromStr,
                result,
                shortlived
        );

        LOG.info("After anonymization [" + fromStr + "] we have [" + result + "]");
        System.out.println("After anonymization [" + fromStr + "] we have [" + result + "]");
        return stringSynonym;
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

    protected Faker initFakerWithLocale(String locale) {
        if (locale == null) {
            return new Faker();
        }
        switch (locale) {
            case "ru": {
                return new Faker(new Locale(locale));
            }
            default: {
                return new Faker();
            }
        }
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
        Faker f = initFakerWithLocale(config.getLocale());
        String function = config.getFunc();
        List<String> commands = parseFunction(function);
        if (commands.isEmpty()) {
            //todo think what to return
        }
        return executeCommandsList(f, parseFunction(function));
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
            config.setLocale(params.get("locale"));
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
