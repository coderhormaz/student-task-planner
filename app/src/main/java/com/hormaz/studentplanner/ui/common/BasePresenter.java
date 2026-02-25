package com.hormaz.studentplanner.ui.common;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends BaseView> {

    private WeakReference<V> viewRef;

    public void attachView(V view) {
        this.viewRef = new WeakReference<>(view);
    }

    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    protected V getView() {
        return viewRef != null ? viewRef.get() : null;
    }

    protected boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }
}
