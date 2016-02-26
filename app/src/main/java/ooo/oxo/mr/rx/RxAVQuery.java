package ooo.oxo.mr.rx;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import java.util.List;

import rx.Observable;

public class RxAVQuery {

    public static <T extends AVObject> Observable<List<T>> find(AVQuery<T> query) {
        return Observable.create(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(query.find());
                    subscriber.onCompleted();
                }
            } catch (AVException e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                }
            }
        });
    }

}
