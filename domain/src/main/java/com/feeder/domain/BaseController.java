package com.feeder.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 8/8/16
 */
public abstract class BaseController {
    private List<DataObserver> mObserverList = new ArrayList<>();

    public abstract List<?> getDataSource();

    public abstract void requestUpdate();

    public void registerObserver(DataObserver observer) {
        mObserverList.add(observer);
    }

    public void unRegisterObserver(DataObserver observer) {
        mObserverList.remove(observer);
    }

    protected void notifyAll(ResponseState state) {
        for (DataObserver observer : mObserverList) {
            if (observer != null) {
                observer.onResponse(state);
            }
        }
    }
}
