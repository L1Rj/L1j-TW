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
package net.l1j.log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import net.l1j.util.StringUtil;
import net.l1j.util.SystemUtil;

public class ConsoleLogFormatter extends Formatter {
	private static final String NEXT_LINE = "\r\n";

	@Override
	public String format(LogRecord record) {
		final StringBuilder output = new StringBuilder(512);
		StringUtil.append(output, record.getMessage(), NEXT_LINE);

		if (record.getThrown() != null) {
			try {
				StringUtil.append(output, SystemUtil.getStackTrace(record.getThrown()), NEXT_LINE);
			} catch (Exception e) {
			}
		}
		return output.toString();
	}
}
