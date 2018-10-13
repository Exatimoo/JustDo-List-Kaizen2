package fr.kaizen.justdo_list_kaizen;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rauch Cyprien on 12/10/2018.
 */

public class StackOverflowXmlParser {
    // We don't use namespaces
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "resources");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String value = parser.getName();
            // Starts by looking for the entry tag
            if (value.equals("daily_task")) {
                entries.add(readDailyTasks(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    public static class DailyTasks {
        public final String id;
        public final String name;
        public final List day_checked;

        private DailyTasks(String id, String name, List day_checked) {
            this.id = id;
            this.name = name;
            this.day_checked = day_checked;
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private DailyTasks readDailyTasks(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "daily_task");
        parser.require(XmlPullParser.START_TAG, ns, "task");
        String id = null;
        String name = null;
        List days_checked = new ArrayList();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String value = parser.getName();
            if (value.equals("id")) {
                id = readId(parser);
            } else if (value.equals("name")) {
                name = readName(parser);
            } else if (value.equals("date")) {
                days_checked = readDaysChecked(parser);
            } else {
                skip(parser);
            }
        }
        return new DailyTasks(id, name, days_checked);
    }

    // Processes title tags in the id.
    private String readId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "id");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "id");
        return id;
    }

    // Processes title tags in the name.
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    // Processes title tags in the days checked.
    private List readDaysChecked(XmlPullParser parser) throws IOException, XmlPullParserException {
        List days_checked = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "days");
        while (parser.next() != XmlPullParser.END_TAG) {
            days_checked.add(readText(parser));
        }
        parser.require(XmlPullParser.END_TAG, ns, "days");
        return days_checked;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}