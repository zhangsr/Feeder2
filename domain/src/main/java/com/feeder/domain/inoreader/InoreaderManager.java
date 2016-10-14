package com.feeder.domain.inoreader;

/**
 * @description:
 * @author: Match
 * @date: 8/11/16
 */
public class InoreaderManager {
    private static InoreaderManager sManager;

    private InoreaderManager(){}

    public InoreaderManager getInstance() {
        if (sManager == null) {
            sManager = new InoreaderManager();
        }
        return sManager;
    }
}
