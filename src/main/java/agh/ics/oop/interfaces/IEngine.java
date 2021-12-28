package agh.ics.oop.interfaces;

public interface IEngine {
    void run();
    void addObserver(IEngineMoveObserver observer);
    void removeObserver(IEngineMoveObserver observer);
}