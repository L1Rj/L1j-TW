/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.l1j.gui.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javolution.util.FastList;

public class ConfigFile {
	private static List<ConfigFile> CONFIGS = new FastList<ConfigFile>();

	private final List<ConfigComment> COMMENTS = new FastList<ConfigComment>();

	private File _file;

	private String _name;

	public ConfigFile(File file) {
		_file = file;
		int lastIndex = file.getName().lastIndexOf('.');
		setName(file.getName().substring(0, lastIndex));
	}

	public static List<ConfigFile> getConfigs() {
		return CONFIGS;
	}

	public void setConfigs(List<ConfigFile> configs) {
		CONFIGS = configs;
	}

	public void addConfigProperty(String name, Object value, ConfigValueType type, String comments) {
		COMMENTS.add(new ConfigProperty(name, value, type, comments));
	}

	public void addConfigComment(String comment) {
		COMMENTS.add(new ConfigComment(comment));
	}

	public void addConfigProperty(String name, Object value, String comments) {
		this.addConfigProperty(name, value, ConfigValueType.firstTypeMatch(value), comments);
	}

	public List<ConfigComment> getConfigProperties() {
		return COMMENTS;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		_name = name;
	}

	public void save() throws IOException {
		BufferedWriter bufWriter = null;
		try {
			bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(_file), "UTF-8"));
			for (ConfigComment cc : COMMENTS) {
				cc.save(bufWriter);
			}
		} finally {
			if (bufWriter != null) {
				bufWriter.close();
			}
		}
	}
}
