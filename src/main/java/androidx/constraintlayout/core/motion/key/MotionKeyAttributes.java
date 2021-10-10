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

import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.core.motion.utils.Utils;

import java.util.HashMap;
import java.util.HashSet;

public class MotionKeyAttributes extends MotionKey {
    static final String NAME = "KeyAttribute";
    private static final String TAG = "KeyAttributes";
    private static final boolean DEBUG = false;
    private String mTransitionEasing;
    private int mCurveFit = -1;
    private int mVisibility = 0;
    private float mAlpha = Float.NaN;
    private float mElevation = Float.NaN;
    private float mRotation = Float.NaN;
    private float mRotationX = Float.NaN;
    private float mRotationY = Float.NaN;
    private float mPivotX = Float.NaN;
    private float mPivotY = Float.NaN;
    private float mTransitionPathRotate = Float.NaN;
    private float mScaleX = Float.NaN;
    private float mScaleY = Float.NaN;
    private float mTranslationX = Float.NaN;
    private float mTranslationY = Float.NaN;
    private float mTranslationZ = Float.NaN;
    private float mProgress = Float.NaN;

    public static final int KEY_TYPE = 1;

    {
        mType = KEY_TYPE;
        mCustom = new HashMap<>();
    }

    @Override
    public void getAttributeNames(HashSet<String> attributes) {

        if (!Float.isNaN(mAlpha)) {
            attributes.add(Attributes.S_ALPHA);
        }
        if (!Float.isNaN(mElevation)) {
            attributes.add(Attributes.S_ELEVATION);
        }
        if (!Float.isNaN(mRotation)) {
            attributes.add(Attributes.S_ROTATION_Z);
        }
        if (!Float.isNaN(mRotationX)) {
            attributes.add(Attributes.S_ROTATION_X);
        }
        if (!Float.isNaN(mRotationY)) {
            attributes.add(Attributes.S_ROTATION_Y);
        }
        if (!Float.isNaN(mPivotX)) {
            attributes.add(Attributes.S_PIVOT_X);
        }
        if (!Float.isNaN(mPivotY)) {
            attributes.add(Attributes.S_PIVOT_Y);
        }
        if (!Float.isNaN(mTranslationX)) {
            attributes.add(Attributes.S_TRANSLATION_X);
        }
        if (!Float.isNaN(mTranslationY)) {
            attributes.add(Attributes.S_TRANSLATION_Y);
        }
        if (!Float.isNaN(mTranslationZ)) {
            attributes.add(Attributes.S_TRANSLATION_Z);
        }
        if (!Float.isNaN(mTransitionPathRotate)) {
            attributes.add(Attributes.S_PATH_ROTATE);
        }
        if (!Float.isNaN(mScaleX)) {
            attributes.add(Attributes.S_SCALE_X);
        }
        if (!Float.isNaN(mScaleY)) {
            attributes.add(Attributes.S_SCALE_Y);
        }
        if (!Float.isNaN(mProgress)) {
            attributes.add(Attributes.S_PROGRESS);
        }
        if (mCustom.size() > 0) {
            for (String s : mCustom.keySet()) {
                attributes.add(TypedValues.S_CUSTOM + "," + s);
            }
        }
    }

    @Override
    public void addValues(HashMap<String, SplineSet> splines) {
        for (String s : splines.keySet()) {
            SplineSet splineSet = splines.get(s);
            if (splineSet == null) {
                continue;
            }
            // TODO support custom
            if (s.startsWith(Attributes.S_CUSTOM)) {
                String cKey = s.substring(Attributes.S_CUSTOM.length() + 1);
                CustomVariable cValue = mCustom.get(cKey);
                if (cValue != null) {
                    ((SplineSet.CustomSpline)splineSet).setPoint(mFramePosition, cValue);
                }
                continue;
            }
            switch (s) {
                case Attributes.S_ALPHA:
                    if (!Float.isNaN(mAlpha)) {
                        splineSet.setPoint(mFramePosition, mAlpha);
                    }
                    break;
                case Attributes.S_ELEVATION:
                    if (!Float.isNaN(mElevation)) {
                        splineSet.setPoint(mFramePosition, mElevation);
                    }
                    break;
                case Attributes.S_ROTATION_Z:
                    if (!Float.isNaN(mRotation)) {
                        splineSet.setPoint(mFramePosition, mRotation);
                    }
                    break;
                case Attributes.S_ROTATION_X:
                    if (!Float.isNaN(mRotationX)) {
                        splineSet.setPoint(mFramePosition, mRotationX);
                    }
                    break;
                case Attributes.S_ROTATION_Y:
                    if (!Float.isNaN(mRotationY)) {
                        splineSet.setPoint(mFramePosition, mRotationY);
                    }
                    break;
                case Attributes.S_PIVOT_X:
                    if (!Float.isNaN(mRotationX)) {
                        splineSet.setPoint(mFramePosition, mPivotX);
                    }
                    break;
                case Attributes.S_PIVOT_Y:
                    if (!Float.isNaN(mRotationY)) {
                        splineSet.setPoint(mFramePosition, mPivotY);
                    }
                    break;
                case Attributes.S_PATH_ROTATE:
                    if (!Float.isNaN(mTransitionPathRotate)) {
                        splineSet.setPoint(mFramePosition, mTransitionPathRotate);
                    }
                    break;
                case Attributes.S_SCALE_X:
                    if (!Float.isNaN(mScaleX)) {
                        splineSet.setPoint(mFramePosition, mScaleX);
                    }
                    break;
                case Attributes.S_SCALE_Y:
                    if (!Float.isNaN(mScaleY)) {
                        splineSet.setPoint(mFramePosition, mScaleY);
                    }
                    break;
                case Attributes.S_TRANSLATION_X:
                    if (!Float.isNaN(mTranslationX)) {
                        splineSet.setPoint(mFramePosition, mTranslationX);
                    }
                    break;
                case Attributes.S_TRANSLATION_Y:
                    if (!Float.isNaN(mTranslationY)) {
                        splineSet.setPoint(mFramePosition, mTranslationY);
                    }
                    break;
                case Attributes.S_TRANSLATION_Z:
                    if (!Float.isNaN(mTranslationZ)) {
                        splineSet.setPoint(mFramePosition, mTranslationZ);
                    }
                    break;
                case Attributes.S_PROGRESS:
                    if (!Float.isNaN(mProgress)) {
                        splineSet.setPoint(mFramePosition, mProgress);
                    }
                    break;
                default:
                    System.err.println("not supported by KeyAttributes "+s);
            }
        }
    }

    @Override
    public MotionKey clone() {
        return null;
    }

    public boolean setValue(int type, int value) {

        switch (type) {
            case Attributes.TYPE_VISIBILITY:
                mVisibility = value;
                break;
            case Attributes.TYPE_CURVE_FIT:
                mCurveFit = value;
                break;
            case TypedValues.TYPE_FRAME_POSITION:
                mFramePosition = value;
                break;
            default:
                if (!setValue(type, value)) {
                    return super.setValue(type, value);
                }
        }
        return true;
    }

    public boolean setValue(int type, float value) {
        switch (type) {
            case Attributes.TYPE_ALPHA:
                mAlpha = value;
                break;
            case Attributes.TYPE_TRANSLATION_X:
                mTranslationX = value;
                break;
            case Attributes.TYPE_TRANSLATION_Y:
                mTranslationY = value;
                break;
            case Attributes.TYPE_TRANSLATION_Z:
                mTranslationZ = value;
                break;
            case Attributes.TYPE_ELEVATION:
                mElevation = value;
                break;
            case Attributes.TYPE_ROTATION_X:
                mRotationX = value;
                break;
            case Attributes.TYPE_ROTATION_Y:
                mRotationY = value;
                break;
            case Attributes.TYPE_ROTATION_Z:
                mRotation = value;
                break;
            case Attributes.TYPE_SCALE_X:
                mScaleX = value;
                break;
            case Attributes.TYPE_SCALE_Y:
                mScaleY = value;
                break;
            case Attributes.TYPE_PIVOT_X:
                mPivotX = value;
                break;
            case Attributes.TYPE_PIVOT_Y:
                mPivotY = value;
                break;
            case Attributes.TYPE_PROGRESS:
                mProgress = value;
                break;
            case Attributes.TYPE_PATH_ROTATE:
                mTransitionPathRotate = value;
                break;
            case TypedValues.TYPE_FRAME_POSITION:
                mTransitionPathRotate = value;
                break;
            default:
                return super.setValue(type, value);
        }
        return true;
    }

    public void setInterpolation(HashMap<String, Integer> interpolation) {
        if (!Float.isNaN(mAlpha)) {
            interpolation.put(Attributes.S_ALPHA, mCurveFit);
        }
        if (!Float.isNaN(mElevation)) {
            interpolation.put(Attributes.S_ELEVATION, mCurveFit);
        }
        if (!Float.isNaN(mRotation)) {
            interpolation.put(Attributes.S_ROTATION_Z, mCurveFit);
        }
        if (!Float.isNaN(mRotationX)) {
            interpolation.put(Attributes.S_ROTATION_X, mCurveFit);
        }
        if (!Float.isNaN(mRotationY)) {
            interpolation.put(Attributes.S_ROTATION_Y, mCurveFit);
        }
        if (!Float.isNaN(mPivotX)) {
            interpolation.put(Attributes.S_PIVOT_X, mCurveFit);
        }
        if (!Float.isNaN(mPivotY)) {
            interpolation.put(Attributes.S_PIVOT_Y, mCurveFit);
        }
        if (!Float.isNaN(mTranslationX)) {
            interpolation.put(Attributes.S_TRANSLATION_X, mCurveFit);
        }
        if (!Float.isNaN(mTranslationY)) {
            interpolation.put(Attributes.S_TRANSLATION_Y, mCurveFit);
        }
        if (!Float.isNaN(mTranslationZ)) {
            interpolation.put(Attributes.S_TRANSLATION_Z, mCurveFit);
        }
        if (!Float.isNaN(mTransitionPathRotate)) {
            interpolation.put(Attributes.S_PATH_ROTATE, mCurveFit);
        }
        if (!Float.isNaN(mScaleX)) {
            interpolation.put(Attributes.S_SCALE_X, mCurveFit);
        }
        if (!Float.isNaN(mScaleY)) {
            interpolation.put(Attributes.S_SCALE_Y, mCurveFit);
        }
        if (!Float.isNaN(mProgress)) {
            interpolation.put(Attributes.S_PROGRESS, mCurveFit);
        }
        if (mCustom.size() > 0) {
            for (String s : mCustom.keySet()) {
                interpolation.put(Attributes.S_CUSTOM + "," + s, mCurveFit);
            }
        }
    }

    public boolean setValue(int type, String value) {
        switch (type) {
            case Attributes.TYPE_EASING:
                mTransitionEasing = value;
                break;

            case TypedValues.TYPE_TARGET:
                mTargetString = value;
                break;
            default:
                return super.setValue(type, value);
        }
        return true;
    }

    @Override
    public int getId(String name) {
        return Attributes.getId(name);
    }

    public int getCurveFit() {
        return mCurveFit;
    }

    public void printAttributes() {
        HashSet<String> nameSet = new HashSet<>();
        getAttributeNames(nameSet);

        System.out.println(" ------------- " + mFramePosition +" -------------");
        String[]names = nameSet.toArray(new String[0]);
        for (int i = 0; i < names.length; i++) {
            int id = Attributes.getId(names[i]);
            System.out.println(names[i]+ ":"+ getFloatValue(id));
        }
    }

    private float getFloatValue(int id) {
        switch (id) {
            case Attributes.TYPE_ALPHA:
                return mAlpha;
            case Attributes.TYPE_TRANSLATION_X:
                return mTranslationX;
            case Attributes.TYPE_TRANSLATION_Y:
                return mTranslationY;
            case Attributes.TYPE_TRANSLATION_Z:
                return mTranslationZ;
            case Attributes.TYPE_ELEVATION:
                return mElevation;
            case Attributes.TYPE_ROTATION_X:
                return mRotationX;
            case Attributes.TYPE_ROTATION_Y:
                return mRotationY;
            case Attributes.TYPE_ROTATION_Z:
                return mRotation;
            case Attributes.TYPE_SCALE_X:
                return mScaleX;
            case Attributes.TYPE_SCALE_Y:
                return mScaleY;
            case Attributes.TYPE_PIVOT_X:
                return mPivotX;
            case Attributes.TYPE_PIVOT_Y:
                return mPivotY;
            case Attributes.TYPE_PROGRESS:
                return mProgress;
            case Attributes.TYPE_PATH_ROTATE:
                return mTransitionPathRotate;
            case TypedValues.TYPE_FRAME_POSITION:
                return mFramePosition;
            default:
                return Float.NaN;
        }
    }
}
