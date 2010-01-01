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
package net.l1j.gui.message;

import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public class SystemPrintStream extends PrintStream {

	private JTextComponent component;

	private StringBuffer sb = new StringBuffer();

	public SystemPrintStream(OutputStream out, JTextComponent component) {
		super(out);

		this.component = component;
	}

	@Override
	public void write(byte[] buf, int off, int len) {
		final String message = new String(buf, off, len);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				sb.append(message);
				component.setText(sb.toString());
			}
		});
	}

}
