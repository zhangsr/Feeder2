package com.feeder.domain.inoreader;

import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 14/05/2017
 */

public class InoSubscriptionList {
    List<InoSubscription> subscriptions;

    public class InoSubscription {
        String id;
        String title;
        String sortid;
        String firstitemmsec;
        String url;
        String htmlUrl;
        String iconUrl;
    }
}
