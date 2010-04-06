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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.utils.StringUtil;

public class CharacterLogFormatter extends Formatter {
	private static final String NEXT_LINE = "\r\n";

	private SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	@Override
	public String format(LogRecord record) {
		final Object[] params = record.getParameters();
		final StringBuilder output = StringUtil.startAppend(30 + record.getMessage().length() + (params == null ? 0 : params.length * 10), "[", dateFmt.format(new Date(record.getMillis())), "] ", record.getMessage());

		for (Object p : record.getParameters()) {
			if (p == null) {
				continue;
			}

			StringUtil.append(output, " ");

			if (p instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) p;
				StringUtil.append(output, pc.getAccountName(), " ", pc.getName(), " [" + String.valueOf(pc.getId()) + "]");
			} else {
				StringUtil.append(output, p.toString());
			}
		}
		output.append(NEXT_LINE);

		return output.toString();
	}
}
