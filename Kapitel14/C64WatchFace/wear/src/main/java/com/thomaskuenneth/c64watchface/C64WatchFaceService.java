/*
 * This file is part of C64 Tribute Watch Face
 * Copyright (C) 2014 - 2017  Thomas Kuenneth
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.thomaskuenneth.c64watchface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class implements a Commodore 64 like watch face.
 *
 * @author Thomas Kuenneth
 */
public class C64WatchFaceService extends CanvasWatchFaceService {

    public static final String PREFS_NAME = C64WatchFaceService.class.getSimpleName();
    public static final String PREFS_DATE = "date";
    public static final String PREFS_SECONDS = "seconds";
    public static final String PREFS_UPPERCASE = "uppercase";

    private SharedPreferences prefs;

    @Override
    public Engine onCreateEngine() {
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine {

        private static final int INTERACTIVE_UPDATE_RATE_MS = 333;

        // Commodore 64 colour 14 (http://unusedino.de/ec64/technical/misc/vic656x/colors/)
        private static final int LIGHT_BLUE = 0xff6C5EB5;

        // Commodore 64 colour 6
        private static final int BLUE = 0xff352879;

        // black
        private static final int BLACK = 0xff000000;

        // white
        private static final int WHITE = 0xffffffff;

        // calculazed text height
        private float last;

        // C64 cursor visible
        private boolean c64CursorVisible;

        final Calendar cal = Calendar.getInstance();

        /* receiver to update the time zone */
        final BroadcastReceiver mTimeZoneReceiver =
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        cal.setTimeZone(TimeZone.getTimeZone(intent.getStringExtra("time-zone")));
                        setCalToNow();
                    }
                };
        boolean mRegisteredTimeZoneReceiver;

        Paint borderPaint;
        Paint backgroundPaint;
        Paint textPaint;
        boolean isRound = false;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            setCalToNow();
            mRegisteredTimeZoneReceiver = false;
            last = -1;
            textPaint = new Paint();
            Typeface typface = Typeface.createFromAsset(getAssets(),
                    "C64_Pro_Mono-STYLE.ttf");
            textPaint.setTypeface(typface);
            borderPaint = new Paint();
            backgroundPaint = new Paint();
            setupPaint(false);
            setWatchFaceStyle(
                    new WatchFaceStyle.Builder(C64WatchFaceService.this)
                            .setStatusBarGravity(Gravity.START | Gravity.TOP)
                            .build());
            c64CursorVisible = false;
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);
            isRound = insets.isRound();
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            setupPaint(inAmbientMode);
            onVisibilityChanged(isVisible());
            invalidate();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            boolean dateVisible = prefs.getBoolean(PREFS_DATE, false);
            boolean seconds = prefs.getBoolean(PREFS_SECONDS, false);
            setCalToNow();
            String strDate;
            if (dateVisible) {
                String strWeekday = new SimpleDateFormat("EE", Locale.getDefault()).format(cal.getTime());
                if (strWeekday.endsWith(".")) {
                    strWeekday = strWeekday.substring(0,
                            strWeekday.length() - 1);
                }
                strDate = strWeekday + " " + cal.get(Calendar.DAY_OF_MONTH);
            } else {
                strDate = "";
            }
            String patternTime;
            if (DateFormat.is24HourFormat(getBaseContext())) {
                patternTime = "HH:mm";
            } else {
                patternTime = "KK:mm";
            }
            if (seconds) {
                patternTime += ":ss";
            }
            if (!DateFormat.is24HourFormat(getBaseContext())) {
                patternTime += " a";
            }
            StringBuilder sb = new StringBuilder(new SimpleDateFormat(patternTime,
                    Locale.getDefault()).format(cal.getTime()));
            while (strDate.length() > sb.length()) {
                sb.append(" ");
            }
            String strTime = sb.toString();
            int w = bounds.width();
            int h = bounds.height();
            int borderHeight = (int) (((float) h / 100f) * 5f);
            int borderWidth = (int) (((float) w / 100f) * 5f);
            canvas.drawPaint(borderPaint);
            Rect r = new Rect(borderWidth,
                    borderHeight, w - 1 - borderWidth,
                    h - borderHeight - 1);

            if (isRound) {
                canvas.drawCircle(bounds.width() / 2,
                        bounds.height() / 2,
                        (bounds.width() - borderWidth) / 2,
                        backgroundPaint);
            } else {
                canvas.drawRect(r, backgroundPaint);
            }

            if (prefs.getBoolean(PREFS_UPPERCASE, false)) {
                strTime = strTime.toUpperCase();
                strDate = strDate.toUpperCase();
            }

            if (last == -1) {
                int maxWidth = r.width();
                float size = 12f;
                last = size;
                while (true) {
                    textPaint.setTextSize(size);
                    float current = textPaint.measureText(strTime);
                    if (current < maxWidth) {
                        last = size;
                        size += 4;
                    } else {
                        break;
                    }
                }
                textPaint.setTextSize(last);
            }
            int x = (w - (int) textPaint.measureText(strTime)) / 2;
            int th = dateVisible ? 2 * (int) last : (int) last;
            int y = ((h - th) / 2) - (int) textPaint.ascent();
            canvas.drawText(strTime, x, y, textPaint);
            if (dateVisible) {
                y += last;
                canvas.drawText(strDate, x, y, textPaint);
            }
            if (!isInAmbientMode()) {
                y += last;
                String a = c64CursorVisible ? "\u2588" : " ";
                canvas.drawText(a, x, y, textPaint);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                registerReceiver();
                // Update time zone in case it changed while we weren't visible.
                cal.setTimeZone(TimeZone.getDefault());
                setCalToNow();
                last = -1;
            } else {
                unregisterReceiver();
            }
            // Whether the timer should be running depends on whether we're visible and
            // whether we're in ambient mode), so we may need to start or stop the timer
            updateTimer();
        }

        private void updateTimer() {
            if (shouldTimerBeRunning()) {
                pulse();
            }
        }

        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter =
                    new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            C64WatchFaceService.this.registerReceiver(mTimeZoneReceiver,
                    filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            C64WatchFaceService.this.unregisterReceiver(
                    mTimeZoneReceiver);
        }

        private void setupPaint(boolean inAmbientMode) {
            textPaint.setColor(inAmbientMode ? WHITE : LIGHT_BLUE);
            borderPaint.setColor(inAmbientMode ? BLACK : LIGHT_BLUE);
            backgroundPaint.setColor(inAmbientMode ? BLACK : BLUE);
            textPaint.setAntiAlias(!inAmbientMode);
        }

        private void setCalToNow() {
            cal.setTime(new Date());
        }

        private void pulse() {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    c64CursorVisible = !c64CursorVisible;
                    invalidate();
                    if (shouldTimerBeRunning()) {
                        pulse();
                    }
                }
            }, INTERACTIVE_UPDATE_RATE_MS);
        }
    }
}
