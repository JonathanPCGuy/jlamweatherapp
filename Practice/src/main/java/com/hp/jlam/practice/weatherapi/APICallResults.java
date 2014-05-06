package com.hp.jlam.practice.weatherapi;

/**
 * Created by lamjon on 4/18/2014.
 */
public interface APICallResults {

    public void UpdateResults(String jsonResults);

    public void UpdateWithError(String errorString);
}
