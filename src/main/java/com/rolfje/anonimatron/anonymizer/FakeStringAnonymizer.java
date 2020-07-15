package com.rolfje.anonimatron.anonymizer;

import com.rolfje.anonimatron.synonyms.StringSynonym;
import com.rolfje.anonimatron.synonyms.Synonym;
import org.apache.log4j.Logger;

import java.util.Map;

public class FakeStringAnonymizer extends BaseFakeAnonymizer {
    private static Logger LOG = Logger.getLogger(FakeStringAnonymizer.class);

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

        StringSynonym stringSynonym = new StringSynonym(
                getType(),
                fromStr,
                result,
                shortlived
        );

        logResult(stringSynonym);
        return stringSynonym;
    }


}
