package com.shopiroller.fragments;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;

import com.shopiroller.models.events.AnimationFinishedEvent;

import org.greenrobot.eventbus.EventBus;

public abstract class OrderFlowBaseFragment extends Fragment {

    public abstract boolean isValid();

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);

        // HW layer support only exists on API 11+
        if (animation == null && nextAnim != 0) {
            animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        }

        if (animation != null) {

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                public void onAnimationEnd(Animation animation) {
                    EventBus.getDefault().post(new AnimationFinishedEvent());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

            });
        }

        return animation;
    }

}
