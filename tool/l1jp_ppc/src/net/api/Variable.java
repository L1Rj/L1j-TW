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
package net.api;

public class Variable
{
	/** 封包名稱 */
	private String name;
	/** 封包位址 */ 	
	private int address;
	/** 封包註解 */
	private String note; 	
	
	/** @param name the name to set */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/** @return the name */
	public String getName()
	{
		return name;
	}

	/** @param address the address to set */
	public void setAddress(int address)
	{
		this.address = address;
	}

	/** @return the address */
	public int getAddress()
	{
		return address;
	}

	/** @param note the note to set */
	public void setNote(String note)
	{
		this.note = "\t/** " + note + " */\r\n";
	}

	/** @return the note */
	public String getNote()
	{
		return note;
	}
	
	@Override
	public String toString()
	{
		String s = Integer.toHexString(address);
		s = s.length() == 1 ? "0x0" + s : "0x" + s;
		return note + "\tpublic final static int " + name + " = " + s + ";\r\n";
	}
}