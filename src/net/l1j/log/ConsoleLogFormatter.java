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
package net.l1j.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import net.l1j.util.StreamUtil;
import net.l1j.util.StringUtil;

public class ConsoleLogFormatter extends Formatter {
	private static final String NEXT_LINE = "\r\n";

	@Override
	public String format(LogRecord record) {
		final StringBuilder output = new StringBuilder(500);
		StringUtil.append(output, record.getMessage(), NEXT_LINE);

		if (record.getThrown() != null) {
			StringWriter sw = null;
			PrintWriter pw = null;
			try {
				sw = new StringWriter();
				pw = new PrintWriter(sw);
				record.getThrown().printStackTrace(pw);
				StringUtil.append(output, sw.toString(), NEXT_LINE);
			} catch (Exception e) {
			} finally {
				StreamUtil.close(pw, sw);
			}
		}
		return output.toString();
	}
}
