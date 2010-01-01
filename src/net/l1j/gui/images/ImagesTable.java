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
package net.l1j.gui.images;

import java.awt.Image;
import java.util.Map;

import javax.swing.ImageIcon;

import javolution.util.FastMap;

public class ImagesTable {
	private static final String IMAGES_DIRECTORY = "data/images/";

	private static final Map<String, Image> IMAGES = new FastMap<String, Image>();

	private static final Map<String, ImageIcon> ICONS = new FastMap<String, ImageIcon>();

	public static Image getImage(String name) {
		if (!IMAGES.containsKey(name)) {
			IMAGES.put(name, new ImageIcon(IMAGES_DIRECTORY + name).getImage());
		}
		return IMAGES.get(name);
	}

	public static ImageIcon getIcon(String name) {
		if (!ICONS.containsKey(name)) {
			ICONS.put(name, new ImageIcon(IMAGES_DIRECTORY + name));
		}
		return ICONS.get(name);
	}
}
