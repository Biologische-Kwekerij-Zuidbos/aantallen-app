/* (C)2024 */
package org.example.state;

public interface IPublisher<T> {

    public void notifySubscribers(T object);

    public void subscribe(ISubscriber<T> subscriber);

    public void unsubscribe(ISubscriber<T> subscriber);
}
