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
package net.l1j.gui.config;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.Color;

/**
 * @author KenM
 */
public class ConfigIPTextField extends JPanel implements FocusListener {
	private static final long serialVersionUID = 1L;

	private JTextField[] _textFields;
	private List<FocusListener> _focusListeners;

	public ConfigIPTextField(String textIp) {
		super.addFocusListener(this);

		initIPTextField(textIp);

		for (int i = 0; i < _textFields.length; i++) {
			_textFields[i].addFocusListener(this);
		}
	}

	public ConfigIPTextField() {
		this("...");
	}

	/**
	 * @param value
	 */
	public ConfigIPTextField(Inet4Address value) {
		this(value.getHostAddress());
	}

	private void initIPTextField(String textIp) {
		final ActionListener nextfocusaction = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				((Component) evt.getSource()).transferFocus();
			}
		};

		this.setLayout(new GridBagLayout());
		this.setBackground(new Color(28, 28, 28));

		_textFields = new JTextField[4];

		GridBagConstraints cons = new GridBagConstraints();
		cons.anchor = GridBagConstraints.PAGE_START;
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.insets = new Insets(1, 1, 1, 1);
		cons.gridx = 0;
		cons.gridy = 0;

		MaxLengthDocument previous = null;
		String[] parts = textIp.split("\\.");
		for (int i = 0; i < 4; i++) {
			String str = parts[i];
			if (i > 0) {
				JLabel dot = new JLabel(".");
				dot.setForeground(new Color(255, 255, 255));
				dot.setFont(new Font(null, Font.BOLD, 11));
				cons.weightx = 0;
				add(dot, cons);
				cons.gridx++;
			}
			MaxLengthDocument maxDoc = new MaxLengthDocument(3);
			_textFields[i] = new JTextField(maxDoc, str, 3);
			if (previous != null) {
				previous.setNext(_textFields[i]);
			}
			previous = maxDoc;
			//cons.weightx = 1;
			add(_textFields[i], cons);
			_textFields[i].setForeground(new Color(255, 255, 255));
			_textFields[i].setFont(new Font(null, Font.BOLD, 11));
			_textFields[i].addActionListener(nextfocusaction);
			cons.gridx++;
		}
	}

	public void addFocusListener(FocusListener fl) {
		if (_focusListeners == null) {
			_focusListeners = new LinkedList<FocusListener>();
		}

		if (fl != null && !_focusListeners.contains(fl)) {
			_focusListeners.add(fl);
		}
	}

	public void removeFocusListener(FocusListener fl) {
		if (_focusListeners != null) {
			_focusListeners.remove(fl);
		}
	}

	public String getText() {
		String str = "";
		for (int i = 0; i < 4; i++) {
			if (_textFields[i].getText().length() == 0) {
				str += "0";
			} else {
				str += _textFields[i].getText();
			}
			if (i < 3) {
				str += ".";
			}
		}
		return str;
	}

	public void setText(String str) {
		try {
			// make sure string is not null; throw a NullPointerException otherwise
			str.length();

			InetAddress ip = InetAddress.getByName(str);
			byte b[] = ip.getAddress();
			for (int i = 0; i < 4; i++) {
				// byte always have a sign in Java, IP addresses aren't
				if (b[i] >= 0) {
					_textFields[i].setText(Byte.toString(b[i]));
				} else {
					_textFields[i].setText(Integer.toString(b[i] + 256));
				}
			}
			return;
		} catch (UnknownHostException ex) {
		} catch (NullPointerException npe) {
		}
		for (int i = 0; i < 4; i++) {
			_textFields[i].setText("");
		}
	}

	public void setEnabled(boolean enabled) {
		for (int i = 0; i < _textFields.length; i++) {
			if (_textFields[i] != null) {
				_textFields[i].setEnabled(enabled);
			}
		}
	}

	public boolean isEmpty() {
		for (int i = 0; i < 4; i++) {
			if (_textFields[i].getText().length() != 0) {
				return false;
			}
		}
		return true;
	}

	public boolean isCorrect() {
		for (int i = 0; i < 4; i++) {
			if (_textFields[i].getText().length() == 0) {
				return false;
			}
		}
		return true;
	}

	public void focusGained(FocusEvent event) {
		if (_focusListeners != null) {
			for (FocusListener fl : _focusListeners) {
				fl.focusGained(event);
			}
		}
	}

	public void focusLost(FocusEvent event) {
		if (isCorrect() || isEmpty()) {
			if (_focusListeners != null) {
				for (FocusListener fl : _focusListeners) {
					fl.focusLost(event);
				}
			}
		}
	}

	public class MaxLengthDocument extends PlainDocument {

		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = 1L;

		private int _max;
		private JTextField _next;

		public MaxLengthDocument(int maxLength) {
			this(maxLength, null);
		}

		public MaxLengthDocument(int maxLength, JTextField next) {
			_max = maxLength;
			setNext(next);
		}

		public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
			if (getLength() + str.length() > _max) {
				if (getNext() != null) {
					if (this.getNext().getText().length() > 0) {
						this.getNext().select(0, this.getNext().getText().length());
					} else {
						this.getNext().getDocument().insertString(0, str, a);
					}
					getNext().requestFocusInWindow();
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			} else {
				super.insertString(offset, str, a);
			}
		}

		/**
		 * @param next The next to set.
		 */
		public void setNext(JTextField next) {
			_next = next;
		}

		/**
		 * @return Returns the next.
		 */
		public JTextField getNext() {
			return _next;
		}
	}
}
