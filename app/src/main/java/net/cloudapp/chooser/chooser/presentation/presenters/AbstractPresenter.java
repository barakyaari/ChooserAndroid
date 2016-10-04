package net.cloudapp.chooser.chooser.presentation.presenters;

import net.cloudapp.chooser.chooser.domain.executor.MainThread;

import java.util.concurrent.Executor;

public abstract class AbstractPresenter {
    protected Executor mExecutor;
    protected MainThread mMainThread;

    public AbstractPresenter(Executor executor, MainThread mainThread) {
        mExecutor = executor;
        mMainThread = mainThread;
    }
}