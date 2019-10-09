package com.techyourchance.multithreading.common;

import com.techyourchance.fragmenthelper.FragmentHelper;
import com.techyourchance.multithreading.demonstrations.atomicity.AtomicityDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.customhandler.CustomHandlerDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.threadwait.ThreadWaitDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.uihandler.UiHandlerDemonstrationFragment;
import com.techyourchance.multithreading.demonstrations.uithread.UiThreadDemonstrationFragment;
import com.techyourchance.multithreading.exercises.exercise1.Exercise1Fragment;
import com.techyourchance.multithreading.exercises.exercise2.Exercise2Fragment;
import com.techyourchance.multithreading.exercises.exercise3.Exercise3Fragment;
import com.techyourchance.multithreading.exercises.exercise4.Exercise4Fragment;
import com.techyourchance.multithreading.home.HomeFragment;

public class ScreensNavigator {

    private final FragmentHelper mFragmentHelper;

    public ScreensNavigator(FragmentHelper fragmentHelper) {
        mFragmentHelper = fragmentHelper;
    }

    public void navigateBack() {
        mFragmentHelper.navigateBack();
    }

    public void navigateUp() {
        mFragmentHelper.navigateUp();
    }

    public void toHomeScreen() {
        mFragmentHelper.replaceFragmentAndClearHistory(HomeFragment.newInstance());
    }

    public void toExercise1Screen() {
        mFragmentHelper.replaceFragment(Exercise1Fragment.newInstance());
    }

    public void toExercise2Screen() {
        mFragmentHelper.replaceFragment(Exercise2Fragment.newInstance());
    }

    public void toUiThreadDemonstration() {
        mFragmentHelper.replaceFragment(UiThreadDemonstrationFragment.newInstance());
    }

    public void toUiHandlerDemonstration() {
        mFragmentHelper.replaceFragment(UiHandlerDemonstrationFragment.newInstance());
    }

    public void toCustomHandlerDemonstration() {
        mFragmentHelper.replaceFragment(CustomHandlerDemonstrationFragment.newInstance());
    }

    public void toExercise3Screen() {
        mFragmentHelper.replaceFragment(Exercise3Fragment.newInstance());
    }

    public void toAtomicityDemonstration() {
        mFragmentHelper.replaceFragment(AtomicityDemonstrationFragment.newInstance());
    }

    public void toExercise4Screen() {
        mFragmentHelper.replaceFragment(Exercise4Fragment.newInstance());
    }

    public void toThreadWaitDemonstration() {
        mFragmentHelper.replaceFragment(ThreadWaitDemonstrationFragment.newInstance());
    }
}
