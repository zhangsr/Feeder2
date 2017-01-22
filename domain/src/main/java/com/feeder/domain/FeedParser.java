package com.feeder.domain;

import android.util.Xml;

import com.feeder.common.DateUtil;
import com.feeder.model.Article;
import com.google.common.base.Strings;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.feeder.common.XMLUtil.skip;

/**
 * @description:
 * @author: Match
 * @date: 10/28/16
 */

// TODO: 10/29/16 optimize: fetch other useful tag
public class FeedParser {
    private static final String RSS = "rss";
    private static final String ITEM = "item";
    private static final String CHANNEL = "channel";
    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String PUB_DATE = "pubDate";
    private static final String DESC = "description";
    private static final String CONTENT = "content:encoded";

    public static List<Article> parse(String xmlStr) {
        if (Strings.isNullOrEmpty(xmlStr)) {
            return null;
        }

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(xmlStr));
            parser.nextTag();
            return readRss(parser);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Article> readRss(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, RSS);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals(CHANNEL)) {
                return readChannel(parser);
            } else {
                skip(parser);
            }
        }
        return null;
    }

    private static List<Article> readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, null, CHANNEL);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals(ITEM)) {
                entries.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private static Article readItem(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, ITEM);
        String title = null;
        String link = null;
        String pubDate = null;
        String description = null;
        String content = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(TITLE)) {
                title = readTitle(parser);
            } else if (name.equals(LINK)) {
                link = readLink(parser);
            } else if (name.equals(PUB_DATE)) {
                pubDate = readPubDate(parser);
            } else if (name.equals(DESC)) {
                description = readDesc(parser);
            } else if (name.equals(CONTENT)) {
                content = readContent(parser);
            } else {
                skip(parser);
            }
        }
        return new Article(null, title, link, description, false, false, content, null,
                DateUtil.parseRfc822(pubDate).getTime(), false, "", "", "");
    }

    private static String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, TITLE);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, TITLE);
        return title;
    }

    private static String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, LINK);
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, LINK);
        return link;
    }

    private static String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, PUB_DATE);
        String pubData = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, PUB_DATE);
        return pubData;
    }

    private static String readDesc(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, DESC);
        String desc = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, DESC);
        return desc;
    }

    private static String readContent(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, CONTENT);
        String content = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, CONTENT);
        return content;
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
