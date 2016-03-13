/*
 * Mr.Mantou - On the importance of taste
 * Copyright (C) 2015-2016  XiNGRZ <xxx@oxo.ooo>
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

package ooo.oxo.mr;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.umeng.analytics.MobclickAgent;

import im.fir.sdk.FIR;
import ooo.oxo.mr.model.Image;

public class MrApplication extends Application {

    public static MrApplication from(Context context) {
        Context application = context.getApplicationContext();
        if (application instanceof MrApplication) {
            return (MrApplication) application;
        } else {
            throw new IllegalArgumentException("context must be from MrApplication");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.setCatchUncaughtExceptions(false);

        if (BuildConfig.FIR_ENABLED) {
            FIR.init(this);
        }

        MrSharedState.createInstance();

        AVObject.registerSubclass(Image.class);

        AVOSCloud.initialize(this, BuildConfig.AVOS_APP_ID, BuildConfig.AVOS_APP_KEY);
    }

}
