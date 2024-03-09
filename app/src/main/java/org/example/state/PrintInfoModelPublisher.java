/* (C)2024 */
package org.example.state;

import java.util.ArrayList;
import java.util.List;

public class PrintInfoModelPublisher implements IPublisher<PrintInfoModel> {

    private final List<ISubscriber<PrintInfoModel>> subscribers = new ArrayList<>();

    @Override
    public void notifySubscribers(PrintInfoModel model) {
        subscribers.stream().forEach(subscriber -> subscriber.update(model));
    }

    @Override
    public void subscribe(ISubscriber<PrintInfoModel> subscriber) {
        this.subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(ISubscriber<PrintInfoModel> subscriber) {
        this.subscribers.remove(subscriber);
    }
}
