/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package androidx.constraintlayout.core.motion.key;

import androidx.constraintlayout.core.motion.MotionWidget;
import androidx.constraintlayout.core.motion.utils.FloatRect;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.TypedValues;

import java.util.HashMap;
import java.util.HashSet;

public class MotionKeyPosition extends MotionKey {
    static final String NAME = "KeyPosition";
    protected static final float SELECTION_SLOPE = 20;
    public int mCurveFit = UNSET;
    public String mTransitionEasing = null;
    public int mPathMotionArc = UNSET; // -1 means not set
    public int mDrawPath = 0;
    public float mPercentWidth = Float.NaN;
    public float mPercentHeight = Float.NaN;
    public float mPercentX = Float.NaN;
    public float mPercentY = Float.NaN;
    public float mAltPercentX = Float.NaN;
    public float mAltPercentY = Float.NaN;
    public static final int TYPE_SCREEN = 2;
    public static final int TYPE_PATH = 1;
    public static final int TYPE_CARTESIAN = 0;
    public int mPositionType = TYPE_CARTESIAN;

    private float mCalculatedPositionX = Float.NaN;
    private float mCalculatedPositionY = Float.NaN;
    static final int KEY_TYPE = 2;

    {
        mType = KEY_TYPE;
    }

    // TODO this needs the views dimensions to be accurate
    private void calcScreenPosition(int layoutWidth, int layoutHeight) {
        int viewWidth = 0;
        int viewHeight = 0;
        mCalculatedPositionX = (layoutWidth - viewWidth) * mPercentX + viewWidth / 2;
        mCalculatedPositionY = (layoutHeight - viewHeight) * mPercentX + viewHeight / 2;
    }

    private void calcPathPosition(float start_x, float start_y,
                                  float end_x, float end_y) {
        float pathVectorX = end_x - start_x;
        float pathVectorY = end_y - start_y;
        float perpendicularX = -pathVectorY;
        float perpendicularY = pathVectorX;
        mCalculatedPositionX = start_x + pathVectorX * mPercentX + perpendicularX * mPercentY;
        mCalculatedPositionY = start_y + pathVectorY * mPercentX + perpendicularY * mPercentY;
    }

    private void calcCartesianPosition(float start_x, float start_y,
                                       float end_x, float end_y) {
        float pathVectorX = end_x - start_x;
        float pathVectorY = end_y - start_y;
        float dxdx = (Float.isNaN(mPercentX)) ? 0 : mPercentX;
        float dydx = (Float.isNaN(mAltPercentY)) ? 0 : mAltPercentY;
        float dydy = (Float.isNaN(mPercentY)) ? 0 : mPercentY;
        float dxdy = (Float.isNaN(mAltPercentX)) ? 0 : mAltPercentX;
        mCalculatedPositionX = (int) (start_x + pathVectorX * dxdx + pathVectorY * dxdy);
        mCalculatedPositionY = (int) (start_y + pathVectorX * dydx + pathVectorY * dydy);
    }

    float getPositionX() {
        return mCalculatedPositionX;
    }

    float getPositionY() {
        return mCalculatedPositionY;
    }

    public void positionAttributes(MotionWidget view, FloatRect start, FloatRect end, float x, float y, String[] attribute, float[] value) {
        switch (mPositionType) {

            case TYPE_PATH:
                positionPathAttributes(start, end, x, y, attribute, value);
                return;
            case TYPE_SCREEN:
                positionScreenAttributes(view, start, end, x, y, attribute, value);
                return;
            case TYPE_CARTESIAN:
            default:
                positionCartAttributes(start, end, x, y, attribute, value);
                return;

        }
    }

    void positionPathAttributes(FloatRect start, FloatRect end, float x, float y, String[] attribute, float[] value) {
        float startCenterX = start.centerX();
        float startCenterY = start.centerY();
        float endCenterX = end.centerX();
        float endCenterY = end.centerY();
        float pathVectorX = endCenterX - startCenterX;
        float pathVectorY = endCenterY - startCenterY;
        float distance = (float) Math.hypot(pathVectorX, pathVectorY);
        if (distance < 0.0001) {
            System.out.println("distance ~ 0");
            value[0] = 0;
            value[1] = 0;
            return;
        }

        float dx = pathVectorX / distance;
        float dy = pathVectorY / distance;
        float perpendicular = (dx * (y - startCenterY) - (x - startCenterX) * dy) / distance;
        float dist = (dx * (x - startCenterX) + dy * (y - startCenterY)) / distance;
        if (attribute[0] != null) {
            if (Position.S_PERCENT_X.equals(attribute[0])) {
                value[0] = dist;
                value[1] = perpendicular;
            }
        } else {
            attribute[0] = Position.S_PERCENT_X;
            attribute[1] = Position.S_PERCENT_Y;
            value[0] = dist;
            value[1] = perpendicular;
        }
    }

    void positionScreenAttributes(MotionWidget view, FloatRect start, FloatRect end, float x, float y, String[] attribute, float[] value) {
        float startCenterX = start.centerX();
        float startCenterY = start.centerY();
        float endCenterX = end.centerX();
        float endCenterY = end.centerY();
        float pathVectorX = endCenterX - startCenterX;
        float pathVectorY = endCenterY - startCenterY;
        MotionWidget viewGroup = ((MotionWidget) view.getParent());
        int width = viewGroup.getWidth();
        int height = viewGroup.getHeight();

        if (attribute[0] != null) { // they are saying what to use
            if (Position.S_PERCENT_X.equals(attribute[0])) {
                value[0] = x / width;
                value[1] = y / height;
            } else {
                value[1] = x / width;
                value[0] = y / height;
            }
        } else { // we will use what we want to
            attribute[0] = Position.S_PERCENT_X;
            value[0] = x / width;
            attribute[1] = Position.S_PERCENT_Y;
            value[1] = y / height;
        }
    }

    void positionCartAttributes(FloatRect start, FloatRect end, float x, float y, String[] attribute, float[] value) {
        float startCenterX = start.centerX();
        float startCenterY = start.centerY();
        float endCenterX = end.centerX();
        float endCenterY = end.centerY();
        float pathVectorX = endCenterX - startCenterX;
        float pathVectorY = endCenterY - startCenterY;
        if (attribute[0] != null) { // they are saying what to use
            if (Position.S_PERCENT_X.equals(attribute[0])) {
                value[0] = (x - startCenterX) / pathVectorX;
                value[1] = (y - startCenterY) / pathVectorY;
            } else {
                value[1] = (x - startCenterX) / pathVectorX;
                value[0] = (y - startCenterY) / pathVectorY;
            }
        } else { // we will use what we want to
            attribute[0] = Position.S_PERCENT_X;
            value[0] = (x - startCenterX) / pathVectorX;
            attribute[1] = Position.S_PERCENT_Y;
            value[1] = (y - startCenterY) / pathVectorY;
        }
    }

    public boolean intersects(int layoutWidth, int layoutHeight, FloatRect start, FloatRect end, float x, float y) {
        calcPosition(layoutWidth, layoutHeight, start.centerX(), start.centerY(), end.centerX(), end.centerY());
        if ((Math.abs(x - mCalculatedPositionX) < SELECTION_SLOPE)
                && (Math.abs(y - mCalculatedPositionY) < SELECTION_SLOPE)) {
            return true;
        }
        return false;
    }

    public MotionKey copy(MotionKey src) {
        super.copy(src);
        MotionKeyPosition k = (MotionKeyPosition) src;
        mTransitionEasing = k.mTransitionEasing;
        mPathMotionArc = k.mPathMotionArc;
        mDrawPath = k.mDrawPath;
        mPercentWidth = k.mPercentWidth;
        mPercentHeight = Float.NaN;
        mPercentX = k.mPercentX;
        mPercentY = k.mPercentY;
        mAltPercentX = k.mAltPercentX;
        mAltPercentY = k.mAltPercentY;
        mCalculatedPositionX = k.mCalculatedPositionX;
        mCalculatedPositionY = k.mCalculatedPositionY;
        return this;
    }

    public MotionKey clone() {
        return new MotionKeyPosition().copy(this);
    }

    void calcPosition(int layoutWidth, int layoutHeight, float start_x, float start_y, float end_x, float end_y) {
        switch (mPositionType) {
            case TYPE_SCREEN:
                calcScreenPosition(layoutWidth, layoutHeight);
                return;

            case TYPE_PATH:
                calcPathPosition(start_x, start_y, end_x, end_y);
                return;
            case TYPE_CARTESIAN:
            default:
                calcCartesianPosition(start_x, start_y, end_x, end_y);
                return;
        }
    }

    @Override
    public void getAttributeNames(HashSet<String> attributes) {

    }

    public void addValues(HashMap<String, SplineSet> splines) {
    }

    @Override
    public boolean setValue(int type, int value) {
        switch (type) {
            case Position.TYPE_POSITION_TYPE:
                mPositionType = value;
                break;
            case TypedValues.TYPE_FRAME_POSITION:
                mFramePosition = value;
                break;
            case Position.TYPE_CURVE_FIT:
                mCurveFit = value;
                break;

            default:
                return super.setValue(type, value);
        }
        return true;

    }

    @Override
    public boolean setValue(int type, float value) {
        switch (type) {
            case Position.TYPE_PERCENT_WIDTH:
                mPercentWidth = value;
                break;
            case Position.TYPE_PERCENT_HEIGHT:
                mPercentHeight = value;
                break;
            case Position.TYPE_SIZE_PERCENT:
                mPercentHeight = mPercentWidth = value;
                break;
            case Position.TYPE_PERCENT_X:
                mPercentX = value;
                break;
            case Position.TYPE_PERCENT_Y:
                mPercentY = value;
                break;
            default:
                return super.setValue(type, value);
        }
        return true;
    }

    @Override
    public boolean setValue(int type, String value) {
        switch (type) {
            case Position.TYPE_TRANSITION_EASING:
                mTransitionEasing = value.toString();
                break;
            default:
                return super.setValue(type, value);
        }
        return true;
    }

    @Override
    public int getId(String name) {
        return Position.getId(name);
    }

}
