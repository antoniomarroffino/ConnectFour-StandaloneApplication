package ch.supsi.connectfour.frontend.controller.statusGame;

public abstract class UpdateStatusAbstract implements UpdateStatusInterface{
    @Override
    public void updateViewStatusPreStart() {}

    @Override
    public void updateViewStatusGame() {}

    @Override
    public void updateViewStatusEnd() {}
}
