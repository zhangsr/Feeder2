package com.feeder.domain.model;

import com.feeder.common.ThreadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 8/8/16
 */
public abstract class BaseModel {
    private List<DataObserver> mObserverList = new ArrayList<>();

    public abstract List<?> getDataSource();

    public abstract void requestData();

    public void registerObserver(DataObserver observer) {
        mObserverList.add(observer);
    }

    public void unRegisterObserver(DataObserver observer) {
        mObserverList.remove(observer);
    }

    protected void notifyAll(final ResponseState state) {
        ThreadManager.post(new Runnable() {
            @Override
            public void run() {
                for (DataObserver observer : mObserverList) {
                    if (observer != null) {
                        // TODO: 11/15/16 cache and delay response avoid too frequent
                        observer.onDataResponse(state, getDataType());
                    }
                }
            }
        });
    }

    protected abstract DataType getDataType();
}
