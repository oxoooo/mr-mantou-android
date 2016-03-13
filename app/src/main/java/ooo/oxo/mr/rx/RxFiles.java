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

package ooo.oxo.mr.rx;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import rx.Observable;

public class RxFiles {

    public static Observable<File> copy(File from, File to) {
        return Observable.defer(() -> {
            FileInputStream input = null;
            FileOutputStream output = null;

            try {
                input = new FileInputStream(from);
                output = new FileOutputStream(to);

                FileChannel inputChannel = input.getChannel();
                FileChannel outputChannel = output.getChannel();

                inputChannel.transferTo(0, inputChannel.size(), outputChannel);

                return Observable.just(to);
            } catch (IOException e) {
                return Observable.error(e);
            } finally {
                closeQuietly(input);
                closeQuietly(output);
            }
        });
    }

    public static Observable<File> mkdirsIfNotExists(File file) {
        return Observable.defer(() -> {
            if (file.mkdirs() || file.isDirectory()) {
                return Observable.just(file);
            } else {
                return Observable.error(new IOException("Failed to mkdirs " + file.getPath()));
            }
        });
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

}
