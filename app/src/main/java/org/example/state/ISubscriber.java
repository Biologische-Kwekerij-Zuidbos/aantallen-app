package org.example.state;

public interface ISubscriber<T> {

    public void update(T newState);

}
