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

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.Writer;
import java.net.Inet4Address;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class ConfigProperty extends ConfigComment {
	private String _propname;
	private Object _value;
	private ConfigValueType _type;
	private JComponent _component;

	/**
	 * @param name
	 * @param value
	 * @param type
	 * @param comments
	 */
	public ConfigProperty(String name, Object value, ConfigValueType type, String comments) {
		super(comments);
		if (!type.getType().isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException("Value Instance Type doesn't match the type argument.");
		}
		_propname = name;
		_type = type;
		_value = value;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return _propname;
	}

	/**
	 * @return Returns the name.
	 */
	public String getDisplayName() {
		return _propname;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		_propname = name;
	}

	/**
	 * @return Returns the value.
	 */
	public Object getValue() {
		return _value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		_value = value;
	}

	/**
	 * @return Returns the type.
	 */
	public ConfigValueType getType() {
		return _type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(ConfigValueType type) {
		_type = type;
	}

	public JComponent getValueComponent() {
		if (_component == null) {
			_component = createValueComponent();
		}
		return _component;
	}

	public JComponent createValueComponent() {
		switch (this.getType()) {
			case BOOLEAN:
				boolean bool = (Boolean) this.getValue();
				JCheckBox checkBox = new JCheckBox();
				checkBox.setSelected(bool);
				return checkBox;
			case IPv4:
				return new ConfigIPTextField((Inet4Address) this.getValue());
			case DOUBLE:
			case INTEGER:
			case STRING:
			default:
				String val = this.getValue().toString();
				JTextField textField = new JTextField(val);
				textField.setForeground(new Color(255, 255, 255));
				textField.setFont(new Font(null, Font.BOLD, 11));
				textField.setColumns(Math.max(15, 10));
				return textField;
		}
	}

	public void save(Writer writer) throws IOException {
		String value;
		if (this.getValueComponent() instanceof JCheckBox) {
			value = ((JCheckBox) this.getValueComponent()).isSelected() + "";
		} else if (this.getValueComponent() instanceof ConfigIPTextField) {
			value = ((ConfigIPTextField) this.getValueComponent()).getText();
		} else if (this.getValueComponent() instanceof JTextField) {
			value = ((JTextField) this.getValueComponent()).getText();
		} else {
			throw new IllegalStateException("Unhandled component value");
		}

		StringBuilder sb = new StringBuilder();
		sb.append('#');
		sb.append(this.getComments().replace("\r\n", "\r\n#"));
		sb.append("\r\n");
		sb.append(this.getName());
		sb.append(" = ");
		sb.append(value);
		sb.append("\r\n");
		sb.append("\r\n");
		writer.write(sb.toString());

	}
}
