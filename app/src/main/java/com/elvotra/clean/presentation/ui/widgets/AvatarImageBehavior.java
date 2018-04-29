package com.elvotra.clean.presentation.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.elvotra.clean.R;

public class AvatarImageBehavior extends CoordinatorLayout.Behavior<ImageView> {

    private Context context;

    private float customFinalYPosition;
    private float customFinalHeight;

    private int startXPosition;
    private float startToolbarPosition;
    private int startYPosition;
    private int finalYPosition;
    private int startHeight;
    private int finalXPosition;
    private float changeBehaviorPoint;

    public AvatarImageBehavior(Context context, AttributeSet attrs) {
        this.context = context;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageBehavior);
            customFinalYPosition = a.getDimension(R.styleable.AvatarImageBehavior_finalYPosition, 0);
            customFinalHeight = a.getDimension(R.styleable.AvatarImageBehavior_finalHeight, 0);

            a.recycle();
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        maybeInitProperties(child, dependency);

        final int maxScrollDistance = (int) (startToolbarPosition);
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;

        if (expandedPercentageFactor < changeBehaviorPoint) {
            float heightFactor = (changeBehaviorPoint - expandedPercentageFactor) / changeBehaviorPoint;

            float distanceXToSubtract = ((startXPosition - finalXPosition)
                    * heightFactor) + (child.getHeight() / 2);
            float distanceYToSubtract = ((startYPosition - finalYPosition)
                    * (1f - expandedPercentageFactor)) + (child.getHeight() / 2);

            child.setX(startXPosition - distanceXToSubtract);
            child.setY(startYPosition - distanceYToSubtract);

            float heightToSubtract = ((startHeight - customFinalHeight) * heightFactor);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = (int) (startHeight - heightToSubtract);
            lp.height = (int) (startHeight - heightToSubtract);
            child.setLayoutParams(lp);
        } else {
            float distanceYToSubtract = ((startYPosition - finalYPosition)
                    * (1f - expandedPercentageFactor)) + (startHeight / 2);

            child.setX(startXPosition - child.getWidth() / 2);
            child.setY(startYPosition - distanceYToSubtract);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = (int) (startHeight);
            lp.height = (int) (startHeight);
            child.setLayoutParams(lp);
        }
        return true;
    }

    private void maybeInitProperties(ImageView child, View dependency) {
        if (startYPosition == 0)
            startYPosition = (int) (dependency.getY());

        if (finalYPosition == 0)
            finalYPosition = (dependency.getHeight() / 2);

        if (startHeight == 0)
            startHeight = child.getHeight();

        if (startXPosition == 0)
            startXPosition = (int) (child.getX() + (child.getWidth() / 2));

        if (finalXPosition == 0)
            finalXPosition = context.getResources().getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + ((int) customFinalHeight / 2) + (int) customFinalYPosition;

        if (startToolbarPosition == 0)
            startToolbarPosition = dependency.getY();

        if (changeBehaviorPoint == 0) {
            changeBehaviorPoint = (child.getHeight() - customFinalHeight) / (2f * (startYPosition - finalYPosition));
        }
    }
}