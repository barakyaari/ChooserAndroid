package net.cloudapp.chooser.chooser.domain.executor;

import net.cloudapp.chooser.chooser.domain.interactors.base.AbstractInteractor;

public interface Executor {

    /**
     * This method should call the interactor's run method and thus start the interactor. This should be called
     * on a background thread as interactors might do lengthy operations.
     *
     * @param interactor The interactor to run.
     */
    void execute(final AbstractInteractor interactor);
}