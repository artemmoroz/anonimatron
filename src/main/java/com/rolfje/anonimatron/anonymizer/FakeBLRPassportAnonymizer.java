package com.rolfje.anonimatron.anonymizer;

import com.rolfje.anonimatron.synonyms.StringSynonym;
import com.rolfje.anonimatron.synonyms.Synonym;
import org.apache.log4j.Logger;

import java.util.Map;

public class FakeBLRPassportAnonymizer extends BaseFakeAnonymizer {
    private static Logger LOG = Logger.getLogger(FakeBLRPassportAnonymizer.class);

    public String getType() {
        return "FAKE_PASSPORT_BLR";
    }

    @Override
    public Synonym anonymize(Object from, int size, boolean shortlived) {
        throw new UnsupportedOperationException("Unable to anonymize without required params");
    }

    public Synonym anonymize(Object from, int size, boolean shortlived, Map<String, String> params) {

        String type = params.get("type");
        if (type != null) {
            String result = null;
            switch (type) {
                case "PN": {
                    result = getFaker().regexify("\\d{7}[A-Z]\\d{3}[A-Z]{2}\\d");
                    break;
                }
                case "N": {
                    result = String.valueOf(getFaker().number().numberBetween(1000000, 9999999));
                    break;
                }
            }
            StringSynonym stringSynonym = new StringSynonym(
                    getType(),
                    (String) from,
                    result,
                    shortlived
            );

            logResult(stringSynonym);
            return stringSynonym;
        } else {
            return null;
        }


    }


}
