package model.observerStrategy;

public interface Subject {
    public void addListener(Observer observer);
    public void removeListener(Observer observer);
    public void notifyListener();
}
