package com.feeder.domain;

import com.feeder.model.FeedlyResult;
import com.feeder.model.Subscription;

/**
 * @description:
 * @author: Match
 * @date: 10/18/16
 */

public class FeedlyUtils {
    public static Subscription result2Subscription(FeedlyResult result, Long accountId) {
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
                "",
                0L,
                0L,
                accountId,
                "",
                "",
                ""
        );
        return subscription;
    }
}
