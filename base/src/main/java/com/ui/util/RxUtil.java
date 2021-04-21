package com.ui.util;

import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

public class RxUtil {
    public static Observable<Object> click(View view) {
        return new Observable<Object>() {
            @Override
            protected void subscribeActual(Observer<? super Object> observer) {
                if (!checkMainThread(observer)) {
                    return;
                }
                view.setOnClickListener(v -> {
                    v.setEnabled(false);
                    v.postDelayed(() -> {
                        v.setEnabled(true);
                    }, 350);
                    observer.onNext(new Object());
                });
                observer.onSubscribe(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        if (view != null) view.setOnClickListener(null);
                    }
                });
            }
        };
    }

    public static Observable<Object> clickQuick(View view) {
        return new Observable<Object>() {
            @Override
            protected void subscribeActual(Observer<? super Object> observer) {
                if (!checkMainThread(observer)) {
                    return;
                }
                view.setOnClickListener(v -> {
                    v.setEnabled(false);
                    v.postDelayed(() -> {
                        v.setEnabled(true);
                    }, 150);
                    observer.onNext(new Object());
                });
                observer.onSubscribe(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        if (view != null) view.setOnClickListener(null);
                    }
                });
            }
        };
    }

    public static <T> T checkNotNull(T value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }

    public static Observable<Object> clickNoEnable(@NonNull View view) {
        return new Observable<Object>() {
            @Override
            protected void subscribeActual(Observer<? super Object> observer) {
                checkNotNull(view, "view == null");
                if (!checkMainThread(observer)) {
                    return;
                }
                view.setOnClickListener(v -> {
                    observer.onNext(new Object());
                });
                observer.onSubscribe(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        view.setOnClickListener(null);
                    }
                });
            }
        }.throttleFirst(500, TimeUnit.MILLISECONDS);
    }

    public static Observable<Object> longClick(@NonNull View view) {
        return new Observable<Object>() {
            @Override
            protected void subscribeActual(Observer<? super Object> observer) {
                checkNotNull(view, "view == null");
                if (!checkMainThread(observer)) {
                    return;
                }
                view.setOnLongClickListener(v -> {
                    observer.onNext(new Object());
                    return true;
                });
                observer.onSubscribe(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        view.setOnLongClickListener(null);
                    }
                });
            }
        };
    }

    public static Observable<Object> clickQuickNoEnable(@NonNull View view) {
        return new Observable<Object>() {
            @Override
            protected void subscribeActual(Observer<? super Object> observer) {
                checkNotNull(view, "view == null");
                if (!checkMainThread(observer)) {
                    return;
                }
                view.setOnClickListener(v -> {
                    observer.onNext(new Object());
                });
                observer.onSubscribe(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        view.setOnClickListener(null);
                    }
                });
            }
        }.throttleFirst(150, TimeUnit.MILLISECONDS);
    }

    public static Observable<Object> clickSlowNoEnable(@NonNull View view) {
        return new Observable<Object>() {
            @Override
            protected void subscribeActual(Observer<? super Object> observer) {
                checkNotNull(view, "view == null");
                if (!checkMainThread(observer)) {
                    return;
                }
                view.setOnClickListener(v -> {
                    observer.onNext(new Object());
                });
                observer.onSubscribe(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        view.setOnClickListener(null);
                    }
                });
            }
        }.throttleFirst(3, TimeUnit.SECONDS);
    }

    public static Observable<String> textChanges(TextView view) {
        return new Observable<String>() {
            @Override
            protected void subscribeActual(Observer<? super String> observer) {
                checkNotNull(view, "view == null");
                if (!checkMainThread(observer)) {
                    return;
                }
                TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        observer.onNext(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
                observer.onSubscribe(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        if (view != null) view.removeTextChangedListener(watcher);
                    }
                });
                view.addTextChangedListener(watcher);
                observer.onNext(view.getText().toString());
            }
        };
    }


    public static Consumer<? super Boolean> enabled(final View view) {
        return b -> {
            if (view != null)
                view.setEnabled(b);
        };
    }

    public static Consumer<? super Boolean> visibility(final View view) {
        return b -> {
            if (view != null)
                view.setVisibility(b ? View.VISIBLE : View.GONE);
        };
    }

    public static Consumer<? super String> text(final TextView view) {
        return s -> {
            if (view != null) {
                view.setText(s);
            }
        };
    }

    public static Consumer<? super Long> time(final TextView view, String f) {
        return s -> {
            if (view != null)
                view.setText(TimeUtil.format(s, TextUtils.isEmpty(f) ? TimeUtil.FORMAT_YYYYMMDD : f));
        };
    }

    public static Consumer<? super String> textE(final EditText view) {
        return s -> {
            if (view != null) {
                view.setText(s);
                if (s.length() > 0)
                    try {
                        view.setSelection(s.toString().length());
                    } catch (Exception e) {
                    }
            }
        };
    }

    public static Consumer<? super String> html(final TextView view) {
        return s -> {
            if (view != null)
                view.setText(Html.fromHtml(s));
        };
    }

    public static Observable<Integer> radioGroupCheckedChanges(RadioGroup radioGroup) {
        return new Observable<Integer>() {
            @Override
            protected void subscribeActual(Observer<? super Integer> observer) {
                checkNotNull(radioGroup, "view == null");
                if (!checkMainThread(observer)) {
                    return;
                }

                RadioGroup.OnCheckedChangeListener listener = (group, checkedId) -> observer.onNext(checkedId);
                observer.onSubscribe(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        radioGroup.setOnCheckedChangeListener(null);
                    }
                });
                radioGroup.setOnCheckedChangeListener(listener);
            }
        };
    }

    public static Observable<Integer> checkBoxCheckedChanges(CheckBox checkBox) {
        return new Observable<Integer>() {
            @Override
            protected void subscribeActual(Observer<? super Integer> observer) {
                checkNotNull(checkBox, "view == null");
                if (!checkMainThread(observer)) {
                    return;
                }

                CheckBox.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        observer.onNext(isChecked ? 1 : 0);
                    }
                };
                observer.onSubscribe(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        checkBox.setOnCheckedChangeListener(null);
                    }
                });
                checkBox.setOnCheckedChangeListener(listener);

            }
        };
    }

    public static Consumer<? super Integer> radioGroupChecked(final RadioGroup view) {
        return new Consumer<Integer>() {
            @Override
            public void accept(Integer value) throws Exception {
                if (value == -1) {
                    view.clearCheck();
                } else {
                    view.check(value);
                }
            }

        };
    }

    public static boolean checkMainThread(Observer<?> observer) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            observer.onError(new IllegalStateException(
                    "Expected to be called on the main thread but was " + Thread.currentThread().getName()));
            return false;
        }
        return true;
    }
}
