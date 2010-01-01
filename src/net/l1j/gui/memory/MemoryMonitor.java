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
package net.l1j.gui.memory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 監視記憶體使用量
 * 
 * @author dexc
 */
public class MemoryMonitor extends JPanel {

	private static final long serialVersionUID = 1L;

	private static JPanel _instance;

	public static JPanel getInstance() {
		if (_instance == null) {
			_instance = MemoryMonitor.start();
			_instance.setBounds(new Rectangle(560, 57, 110, 225));
		}
		return _instance;
	}

	public static JCheckBox dateStampCB = new JCheckBox("Memory");

	public MemoryStart surf;

	private JPanel controls;

	private JTextField tf;

	public MemoryMonitor() {
		setLayout(new BorderLayout());
		add(surf = new MemoryStart());

		controls = new JPanel();
		controls.setPreferredSize(new Dimension(100, 80));
		Font font = new Font("Arial Unicode MS", Font.PLAIN, 10);

		JLabel label = new JLabel("Sample Rate");
		label.setFont(font);
		label.setForeground(new Color(45, 45, 45));
		controls.add(label);

		tf = new JTextField("1000");
		tf.setPreferredSize(new Dimension(45, 20));

		controls.add(tf);
		controls.add(label = new JLabel("ms"));
		controls.add(dateStampCB);

		dateStampCB.setFont(font);
	}

	private static JPanel start() {
		final MemoryMonitor memory = new MemoryMonitor();
		memory.surf.start();
		return memory;
	}

}
