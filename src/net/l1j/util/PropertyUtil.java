/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.l1j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Noctarius
 */
public final class PropertyUtil extends Properties {
	private static final long serialVersionUID = 1L;

	private final static Logger _log = Logger.getLogger(PropertyUtil.class.getName());

	public PropertyUtil() {
	}

	public PropertyUtil(String name) throws IOException {
		load(new FileInputStream(name));
	}

	public PropertyUtil(File file) throws IOException {
		load(new FileInputStream(file));
	}

	public PropertyUtil(InputStream inStream) throws IOException {
		load(inStream);
	}

	public PropertyUtil(Reader reader) throws IOException {
		load(reader);
	}

	public void load(String name) throws IOException {
		load(new FileInputStream(name));
	}

	public void load(File file) throws IOException {
		load(new FileInputStream(file));
	}

	@Override
	public void load(InputStream inStream) throws IOException {
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(inStream, "UTF-8");
			super.load(reader);
		} finally {
			inStream.close();
			if (reader != null) {
				reader.close();
			}
		}
	}

	@Override
	public void load(Reader reader) throws IOException {
		try {
			super.load(reader);
		} finally {
			reader.close();
		}
	}

	@Override
	public String getProperty(String key) {
		String property = super.getProperty(key);

		if (property == null) {
			_log.info("PropertyUtil: Missing property for key - " + key);

			return null;
		}

		return property.trim();
	}

	@Override
	public String getProperty(String key, String defaultValue) {
		String property = super.getProperty(key, defaultValue);

		if (property == null) {
			_log.warning("PropertyUtil: Missing defaultValue for key - " + key);

			return null;
		}

		return property.trim();
	}
}
