package com.feeder.domain;

import com.feeder.model.FeedlyResult;
import com.feeder.model.Subscription;

/**
 * @description:
 * @author: Match
 * @date: 10/18/16
 */

public class FeedlyUtils {
    public static Subscription result2Subscription(FeedlyResult result) {
        if (result == null) {
            return null;
        }
        Subscription subscription = new Subscription(
                null,
                result.feedId,
                result.title,
                result.iconUrl,
                result.feedId.substring(5),
                result.lastUpdated,
                result.website,
                result.description,
                "",
                ""
        );
        return subscription;
    }
}
