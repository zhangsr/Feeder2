package com.feeder.domain.model;

/**
 * @description:
 * @author: Match
 * @date: 7/22/16
 */
public interface DataObserver {
    void onDataResponse(ResponseState state, DataType type);
}
