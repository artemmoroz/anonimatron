package com.rolfje.anonimatron.anonymizer;

import com.rolfje.anonimatron.synonyms.DateSynonym;
import com.rolfje.anonimatron.synonyms.Synonym;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FakeDateAnonymizer extends BaseFakeAnonymizer {
    private static Logger LOG = Logger.getLogger(FakeDateAnonymizer.class);

    public String getType() {
        return "FAKE_DATE";
    }

    protected DateSynonym createDateSynonym(Object from, Date to, boolean shortlived) {
        DateSynonym s = new DateSynonym();
        s.setType(getType());
        s.setShortlived(shortlived);
        s.setFrom(from);
        s.setTo(to);
        return s;
    }

    @Override
    public Synonym anonymize(Object from, int size, boolean shortlived) {
        Date to = getFaker().date().birthday();
        DateSynonym s = createDateSynonym(from, to, shortlived);
        logResult(s);
        return s;
    }

    public Synonym anonymize(Object from, int size, boolean shortlived, Map<String, String> params) {
        Integer ageMin = getIntSafe(params.get("ageMin"));
        Integer ageMax = getIntSafe(params.get("ageMax"));
        String locale = params.get(LOCALE);
        if (ageMax != null) {
            if (ageMin == null) {
                ageMin = 0;
            }
            Date to = null;
            if (ageMax > 0) {
                to = getFaker(locale).date().birthday(ageMin, ageMax);
            } else {
                to = getFaker(locale).date().future(ageMax * 365, TimeUnit.DAYS);
            }
            DateSynonym s = createDateSynonym(from, to, shortlived);
            logResult(s);
            return s;
        }
        return anonymize(from, size, shortlived);
    }
}
