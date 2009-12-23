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
package net.l1j.tool;

import java.io.IOException;
import java.io.Writer;

public class ConfigComment {

	private String _comments;

	/**
	 * @param comments
	 */
	public ConfigComment(String comments) {
		_comments = comments;
	}

	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return _comments;
	}

	/**
	 * @param comments
	 *            The comments to set.
	 */
	public void setComments(String comments) {
		_comments = comments;
	}

	public void save(Writer writer) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append('#');
		sb.append(this.getComments().replace("\r\n", "\r\n#"));
		sb.append("\r\n\r\n");
		writer.write(sb.toString());
	}
}
