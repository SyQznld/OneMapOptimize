package com.oneMap.module.common.base;

import android.support.annotation.Keep;

/**
 * <p>数据回调接口</p>
 *
 * @name InfoCallback
 */
@Keep
public interface InfoCallback<T> {

    void onSuccess(T info);

    void onError(int code, String message);

}
