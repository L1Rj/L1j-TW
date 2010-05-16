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
package net.l1j;

/**
 * This class used to be the starter class, since LS/GS split, it only retains server mode
 */
public class Server {
	// constants for the server mode
	public static final int MODE_NONE = 0;
	public static final int MODE_GAMESERVER = 1;
	public static final int MODE_LOGINSERVER = 2;

	public static int serverMode = MODE_NONE;
}
