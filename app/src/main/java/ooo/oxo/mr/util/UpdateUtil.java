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

package ooo.oxo.mr.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import im.fir.sdk.FIR;
import im.fir.sdk.callback.VersionCheckCallback;
import im.fir.sdk.version.AppVersion;
import ooo.oxo.mr.BuildConfig;
import ooo.oxo.mr.R;

public class UpdateUtil {

    public static void checkForUpdate(OnUpdateAvailableListener listener) {
        //noinspection PointlessBooleanExpression
        if (!BuildConfig.FIR_ENABLED) {
            return;
        }

        FIR.checkForUpdateInFIR(BuildConfig.FIR_API_TOKEN, new VersionCheckCallback() {
            @Override
            public void onSuccess(AppVersion version, boolean b) {
                if (version.getVersionCode() > BuildConfig.VERSION_CODE) {
                    listener.onUpdateAvailable(version);
                }
            }
        });
    }

    public static void promptUpdate(Context context, AppVersion version) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.update_available, version.getVersionName()))
                .setMessage(TextUtils.isEmpty(version.getChangeLog())
                        ? null
                        : version.getChangeLog())
                .setNegativeButton(R.string.update_cancel, null)
                .setPositiveButton(R.string.update_confirm, (dialog, which) -> {
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(version.getUpdateUrl())));
                    } catch (ActivityNotFoundException e) {
                        // 狗带
                    }
                })
                .show();
    }

    public interface OnUpdateAvailableListener {

        void onUpdateAvailable(AppVersion version);

    }

}
