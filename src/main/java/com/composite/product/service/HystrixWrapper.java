package com.composite.product.service;

import com.netflix.hystrix.HystrixObservableCommand;

import java.util.List;
import java.util.function.Supplier;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.HttpClientErrorException;
import rx.Observable;

public class HystrixWrapper<S,T> extends HystrixObservableCommand<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HystrixWrapper.class);
    private S action;
    private List<Class<? extends Exception>> ignoredExceptions;

    /**
     * Constructor
     * @param action
     * @param setter
     */
    public HystrixWrapper(S action, Setter setter) {
        this(action, setter, null);
    }

    /**
     * Constructor
     * @param action
     * @param setter
     */
    public HystrixWrapper(S action, Setter setter, List<Class<? extends Exception>> ignoredExceptions) {
        super(setter);
        this.action = action;
        this.ignoredExceptions = ignoredExceptions;
    }

    /**
     * Construct the Observable for execution.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected Observable<T> construct() {
        if (action instanceof Supplier) {
            return Observable.create(subscriber -> {
                T response  = null;
                try {
                    response = ((Supplier<T>) action).get();
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                } catch (HttpClientErrorException e) {
                    //Log error
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                    subscriber.onNext(response);
                }
            });
        }
        return Observable.just(null);
    }

    @Override
    protected Observable<T> resumeWithFallback() {
        Throwable exception = getFailedExecutionException();
        if (exception != null) {
            LOGGER.error("Hystrix Exception: {}", exception.getMessage());
        }
        return Observable.error(new UnsupportedOperationException("No fallback available."));
    }
}