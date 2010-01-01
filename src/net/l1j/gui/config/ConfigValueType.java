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

import java.net.Inet4Address;
import java.util.NoSuchElementException;

public enum ConfigValueType {
	BOOLEAN(Boolean.class), DOUBLE(Double.class), INTEGER(Integer.class), IPv4(Inet4Address.class), STRING(String.class);

	private final Class<?> _type;

	private ConfigValueType(Class<?> type) {
		_type = type;
	}

	public Class<?> getType() {
		return _type;
	}

	public static ConfigValueType firstTypeMatch(Object value) {
		for (ConfigValueType vt : ConfigValueType.values()) {
			if (vt.getType() == value.getClass()) {
				return vt;
			}
		}
		throw new NoSuchElementException("No match for: " + value.getClass().getName());
	}
}
